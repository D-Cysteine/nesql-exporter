package com.github.dcysteine.nesql.exporter.main;

import com.github.dcysteine.nesql.exporter.main.config.Config;
import com.github.dcysteine.nesql.exporter.main.config.ConfigGuiFactory;
import com.github.dcysteine.nesql.exporter.main.config.ConfigOptions;
import com.github.dcysteine.nesql.exporter.util.render.Renderer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;

/** Main entry point for Not Enough SQL Exporter. */
@Mod(
        modid = NotEnoughSQLExporter.MOD_ID,
        name = NotEnoughSQLExporter.MOD_NAME,
        version = NotEnoughSQLExporter.MOD_VERSION,
        acceptableRemoteVersions = "*", // Client-side-only mod.
        dependencies = NotEnoughSQLExporter.MOD_DEPENDENCIES,
        guiFactory = ConfigGuiFactory.CLASS_NAME)
public final class NotEnoughSQLExporter {
    public static final String MOD_ID = "nesql-exporter";
    public static final String MOD_NAME = "Not Enough SQL Exporter";
    public static final String MOD_VERSION = "@version@";
    public static final String MOD_DEPENDENCIES = "required-after:NotEnoughItems;";

    @Instance(MOD_ID)
    @SuppressWarnings("unused")
    public static NotEnoughSQLExporter instance;

    @EventHandler
    @SuppressWarnings("unused")
    public void onInitialization(FMLInitializationEvent event) {
        // This mod is only intended to run in single-player mode.
        // It does nothing if run on a dedicated server, or on a client connecting to a server.
        if (event.getSide() != Side.CLIENT) {
            return;
        }
        Logger.MOD.info("Mod initialization starting...");

        ConfigGuiFactory.checkClassName();
        Config.initialize();
        Config.updateConfig();
        FMLCommonHandler.instance().bus().register(Renderer.INSTANCE);
        FMLCommonHandler.instance().bus().register(this);

        Logger.MOD.info("Mod initialization complete!");
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onServerStart(FMLServerStartingEvent event) {
        // This mod is only intended to run in single-player mode.
        // It does nothing if run on a dedicated server, or on a client connecting to a server.
        if (event.getSide() != Side.CLIENT) {
            return;
        }

        event.registerServerCommand(new ExportCommand());
        Logger.MOD.info("Export command registered!");
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onClientConnected(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        if (ConfigOptions.AUTO_EXPORT_ON_CONNECT.get()) {
            Logger.MOD.info("Automatically exporting...");
            new Thread(new Exporter()::exportReportException).start();
        }
    }
}
