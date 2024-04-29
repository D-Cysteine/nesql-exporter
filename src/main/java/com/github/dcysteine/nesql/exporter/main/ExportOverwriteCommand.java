package com.github.dcysteine.nesql.exporter.main;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.List;

/** Command to run NESQL export, and overwrite if the specified repository already exists. */
final class ExportOverwriteCommand implements ICommand {
    @Override
    public String getCommandName() {
        return "nesqlf";
    }

    @Override
    public String getCommandUsage(ICommandSender unused) {
        return "/nesqlf [filename suffix]";
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List getCommandAliases() {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 1) {
            Logger.chatMessage("Too many parameters! Usage: " + getCommandUsage(sender));
            return;
        }

        Exporter exporter;
        if (args.length == 1) {
            exporter = new Exporter(true, args[0]);
        } else {
            exporter = new Exporter(true);
        }
        new Thread(exporter::exportReportException).start();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender unused) {
        return true;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List addTabCompletionOptions(ICommandSender unused, String[] args) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] strings, int i) {
        return false;
    }

    @Override
    public int compareTo(Object other) {
        if (other instanceof ICommand) {
            return this.getCommandName().compareTo(((ICommand) other).getCommandName());
        }

        return 0;
    }
}
