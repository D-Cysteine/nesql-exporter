package com.github.dcysteine.nesql.exporter.main;

import com.github.dcysteine.nesql.exporter.handler.minecraft.ItemSaver;
import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.processor.base.RecipeProcessor;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.RenderJob;
import com.github.dcysteine.nesql.exporter.util.render.Renderer;
import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import org.hibernate.jpa.HibernatePersistenceProvider;

import java.io.File;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/** Exports recipes and other data to a file. */
public final class Exporter {
    private static final String FILE_EXTENSION = ".mv.db";
    private static final String REPOSITORY_PATH_FORMAT_STRING = "nesql" + File.separator + "%s";
    private static final String DATABASE_FILE_PATH = "nesql-db" + FILE_EXTENSION;
    private static final String ITEM_IMAGE_PATH = "image" + File.separator + "item";
    private static final String FLUID_IMAGE_PATH = "image" + File.separator + "fluid";

    private final String repositoryName;
    private final File repositoryDirectory;
    private final File databaseFile;
    private final File itemImageDirectory;
    private final File fluidImageDirectory;

    public Exporter() {
        this(ConfigOptions.REPOSITORY_NAME.get());
    }

    public Exporter(String repositoryName) {
        this.repositoryName = repositoryName;
        this.repositoryDirectory =
                new File(
                        (File) FMLInjectionData.data()[6],
                        String.format(REPOSITORY_PATH_FORMAT_STRING, repositoryName));
        this.databaseFile = new File(repositoryDirectory, DATABASE_FILE_PATH);
        this.itemImageDirectory = new File(repositoryDirectory, ITEM_IMAGE_PATH);
        this.fluidImageDirectory = new File(repositoryDirectory, FLUID_IMAGE_PATH);

    }

    /**
     * Wrapper for {@link #export()} which will report exceptions to chat.
     *
     * <p>This is needed because exceptions thrown within threads only appear in logs.
     */
    public void exportReportException() {
        // Bit of a hack, but if we're exporting upon client connect,
        // then we need to wait for the player entity to become available.
        while (Minecraft.getMinecraft().thePlayer == null) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException wakeUp) {}
        }

        try {
            export();
        } catch (Exception e) {
            Logger.chatMessage(
                    EnumChatFormatting.RED
                            + "Something went wrong during export! Please check your logs.");
            throw e;
        }
    }

    private void export() {
        Logger.chatMessage(EnumChatFormatting.AQUA + "Exporting data to:");
        Logger.chatMessage(repositoryDirectory.getAbsolutePath());
        if (repositoryDirectory.exists()) {
            throw new RuntimeException(
                    String.format(
                            "Cannot create repository \"%s\"; it already exists!", repositoryName));
        }
        if (!repositoryDirectory.mkdirs()) {
            throw new RuntimeException(
                    String.format("Failed to create repository \"%s\"!", repositoryName));
        }

        // TODO add logging everywhere. Also log progress of render jobs?
        ImmutableMap<String, String> properties =
                ImmutableMap.of(
                        "hibernate.connection.url",
                        "jdbc:h2:file:"
                                + databaseFile.getAbsolutePath().replace(FILE_EXTENSION, ""));
        EntityManagerFactory entityManagerFactory =
                new HibernatePersistenceProvider()
                        .createEntityManagerFactory("H2", properties);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        ItemSaver itemSaver = new ItemSaver(entityManagerFactory.createEntityManager());

        if (ConfigOptions.RENDER_ICONS.get()) {
            Logger.chatMessage(EnumChatFormatting.AQUA + "Initializing renderer.");
            Renderer.INSTANCE.preinitialize(itemImageDirectory, fluidImageDirectory);
            RenderDispatcher.INSTANCE.setRendererState(RenderDispatcher.RendererState.INITIALIZING);
        }

        Logger.chatMessage(EnumChatFormatting.AQUA + "Saving data to database...");
        // TODO call processors here (and check deps? maybe do that on mod init...)
        new RecipeProcessor(entityManager).process();
        Logger.chatMessage(EnumChatFormatting.AQUA + "Saving complete!");

        if (ConfigOptions.RENDER_ICONS.get()) {
            try {
                Logger.chatMessage(EnumChatFormatting.AQUA + "Waiting for rendering to finish...");
                Logger.chatMessage(
                        EnumChatFormatting.AQUA
                                + "If you never see a \"Rendering complete!\" message after this,");
                Logger.chatMessage(
                        EnumChatFormatting.AQUA
                                + "then something probably went wrong during rendering.");
                RenderDispatcher.INSTANCE.waitUntilJobsComplete();
            } catch (InterruptedException wakeUp) {}
            RenderDispatcher.INSTANCE.setRendererState(RenderDispatcher.RendererState.DESTROYING);
            Logger.chatMessage(EnumChatFormatting.AQUA + "Rendering complete!");
        }

        Logger.chatMessage(EnumChatFormatting.AQUA + "Writing database...");
        itemSaver.save();
        entityManagerFactory.close();
        Logger.chatMessage(EnumChatFormatting.AQUA + "Export complete!");
    }
}
