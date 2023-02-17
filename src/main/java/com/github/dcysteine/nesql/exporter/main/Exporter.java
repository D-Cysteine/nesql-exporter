package com.github.dcysteine.nesql.exporter.main;

import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.plugin.ExporterState;
import com.github.dcysteine.nesql.exporter.plugin.PluginExporter;
import com.github.dcysteine.nesql.exporter.registry.PluginRegistry;
import com.github.dcysteine.nesql.exporter.util.render.RenderDispatcher;
import com.github.dcysteine.nesql.exporter.util.render.Renderer;
import com.github.dcysteine.nesql.sql.Metadata;
import com.github.dcysteine.nesql.sql.Plugin;
import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.FMLInjectionData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import org.hibernate.jpa.HibernatePersistenceProvider;

import jakarta.persistence.EntityManagerFactory;
import java.io.File;
import java.util.Map;

/** Exports recipes and other data to a file. */
public final class Exporter {
    private static final String REPOSITORY_PATH_FORMAT_STRING = "nesql" + File.separator + "%s";
    private static final String DATABASE_FILE_PATH = "nesql-db";
    private static final String IMAGE_DIRECTORY_PATH = "image";

    private final String repositoryName;
    private final File repositoryDirectory;
    private final File databaseFile;
    private final File imageDirectory;

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
        this.imageDirectory = new File(repositoryDirectory, IMAGE_DIRECTORY_PATH);

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
        } finally {
            // If we crash, stop rendering things.
            RenderDispatcher.INSTANCE.setRendererState(RenderDispatcher.RendererState.ERROR);
        }
    }

    private void export() {
        Logger.chatMessage(EnumChatFormatting.AQUA + "Exporting data to:");
        Logger.chatMessage(repositoryDirectory.getAbsolutePath());
        if (repositoryDirectory.exists()) {
            Logger.chatMessage(
                    EnumChatFormatting.RED + String.format(
                            "Cannot create repository \"%s\"; it already exists!", repositoryName));
            return;
        }
        if (!repositoryDirectory.mkdirs()) {
            Logger.chatMessage(
                    EnumChatFormatting.RED
                            + String.format("Failed to create repository \"%s\"!", repositoryName));
            return;
        }

        ImmutableMap<String, String> properties =
                ImmutableMap.of(
                        "hibernate.connection.url",
                        "jdbc:hsqldb:file:" + databaseFile.getAbsolutePath());
        // Append this to the path if we need cached tables.
        // Note: this seems to 3x or 4x the time needed to export. It should drastically decrease
        // server start-up time, at the cost of slightly increasing query time.
        //+ ";hsqldb.default_table_type=CACHED"
        // Append this if we need to use lobs (this will set min lob size to 1KB).
        //+ ";hsqldb.lob_compressed=true;hsqldb.lob_file_scale=1"
        EntityManagerFactory entityManagerFactory =
                new HibernatePersistenceProvider()
                        .createEntityManagerFactory("NESQL", properties);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        boolean renderingImages = ConfigOptions.RENDER_ICONS.get();
        if (renderingImages) {
            Logger.chatMessage(EnumChatFormatting.AQUA + "Initializing renderer.");

            if (!imageDirectory.exists() && !imageDirectory.mkdirs()) {
                Logger.chatMessage(EnumChatFormatting.RED + "Could not create image directory!");
                Logger.chatMessage(EnumChatFormatting.RED + "Skipping rendering!");
                RenderDispatcher.INSTANCE.setRendererState(RenderDispatcher.RendererState.ERROR);
                renderingImages = false;
            } else {
                Renderer.INSTANCE.preinitialize(imageDirectory);
                RenderDispatcher.INSTANCE.setRendererState(
                        RenderDispatcher.RendererState.INITIALIZING);
            }

            if (Loader.isModLoaded("bugtorch")) {
                Logger.chatMessage(EnumChatFormatting.RED + "BugTorch mod appears to be loaded;");
                Logger.chatMessage(
                        EnumChatFormatting.RED + "enchanted items might not render correctly!");
            }
        }

        Logger.chatMessage(EnumChatFormatting.AQUA + "Initializing plugins.");
        ExporterState exporterState = new ExporterState(entityManager);
        PluginRegistry registry = new PluginRegistry();
        Map<Plugin, PluginExporter> activePlugins = registry.initialize(exporterState);
        Logger.chatMessage(EnumChatFormatting.AQUA + "Active plugins:");
        activePlugins.keySet().forEach(
                plugin -> Logger.chatMessage("  " + EnumChatFormatting.YELLOW + plugin.getName()));

        Logger.chatMessage(EnumChatFormatting.AQUA + "Exporting data...");
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(new Metadata(activePlugins.keySet()));

        registry.initializePlugins();
        registry.processPlugins();
        registry.postProcessPlugins();

        Logger.chatMessage(EnumChatFormatting.AQUA + "Data exported!");
        Logger.chatMessage(EnumChatFormatting.AQUA + "Committing database...");
        transaction.commit();

        Logger.chatMessage(EnumChatFormatting.AQUA + "Compacting database...");
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("SHUTDOWN COMPACT").executeUpdate();
        // No need to commit the transaction; SHUTDOWN closes everything already.

        entityManager.close();
        entityManagerFactory.close();
        Logger.chatMessage(EnumChatFormatting.AQUA + "Commit complete!");

        if (renderingImages) {
            Logger.chatMessage(EnumChatFormatting.AQUA + "Waiting for rendering to finish...");
            Logger.MOD.info("Remaining render jobs: " + RenderDispatcher.INSTANCE.getJobCount());
            try {
                RenderDispatcher.INSTANCE.waitUntilJobsComplete();
            } catch (InterruptedException wakeUp) {}
            RenderDispatcher.INSTANCE.setRendererState(RenderDispatcher.RendererState.DESTROYING);
            Logger.chatMessage(EnumChatFormatting.AQUA + "Rendering complete!");
        }
        Logger.chatMessage(EnumChatFormatting.AQUA + "Export complete!");
    }
}
