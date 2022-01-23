package com.github.dcysteine.nesql.exporter.main;

import com.github.dcysteine.nesql.exporter.handler.minecraft.ItemSaver;
import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.RenderJob;
import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Exports recipes and other data to a file. */
public final class Exporter {
    private static final String FILE_EXTENSION = ".mv.db";
    private static final String FILE_PATH_FORMAT_STRING =
            "nesql" + File.separator + "NESQL-%s" + FILE_EXTENSION;

    private final File outputFile;

    public Exporter() {
        this(defaultFilenameSuffix());
    }

    public Exporter(String filenameSuffix) {
        this.outputFile =
                new File(
                        (File) FMLInjectionData.data()[6],
                        String.format(FILE_PATH_FORMAT_STRING, filenameSuffix));
    }

    private static String defaultFilenameSuffix() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
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
        Logger.chatMessage(outputFile.getAbsolutePath());
        File outputDir = outputFile.getParentFile();
        if (!outputDir.exists() && !outputDir.mkdir()) {
            Logger.chatMessage(
                    EnumChatFormatting.RED + "Could not create export directory! Aborting!");
            return;
        }
        if (outputFile.exists()) {
            Logger.chatMessage(EnumChatFormatting.RED + "Export file already exists! Aborting!");
            return;
        }

        // TODO add logging everywhere. Also log progress of render jobs?
        ImmutableMap<String, String> properties =
                ImmutableMap.of(
                        "hibernate.connection.url",
                        "jdbc:h2:file:" + outputFile.getAbsolutePath().replace(FILE_EXTENSION, ""));
        EntityManagerFactory entityManagerFactory =
                new HibernatePersistenceProvider()
                        .createEntityManagerFactory("H2", properties);

        ItemSaver itemSaver = new ItemSaver(entityManagerFactory.createEntityManager());

        if (ConfigOptions.RENDER_ICONS.get()) {
            // TODO put rendering stuff here
        }
        RenderDispatcher.INSTANCE.setRendererState(RenderDispatcher.RendererState.INITIALIZING);
        // TODO queue up render jobs here and then wait...
        RenderDispatcher.INSTANCE.addJob(
                RenderJob.ofItem(new ItemStack(Items.apple, 1)));
        RenderDispatcher.INSTANCE.addJob(
                RenderJob.ofItem(new ItemStack(Blocks.cobblestone, 1)));
        RenderDispatcher.INSTANCE.addJob(
                RenderJob.ofFluid(FluidRegistry.getFluid("water")));
        RenderDispatcher.INSTANCE.addJob(
                RenderJob.ofFluid(FluidRegistry.getFluid("lava")));
        while (!RenderDispatcher.INSTANCE.noJobsRemaining()) {
            try {
                // TODO warn that if logging ends here, then rendering probably failed
                RenderDispatcher.INSTANCE.waitUntilJobsComplete();
            } catch (InterruptedException wakeUp) {}
        }
        RenderDispatcher.INSTANCE.setRendererState(RenderDispatcher.RendererState.DESTROYING);

        itemSaver.save();
        entityManagerFactory.close();

        Logger.chatMessage(EnumChatFormatting.AQUA + "Export complete!");
    }
}
