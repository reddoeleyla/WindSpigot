package org.bukkit.command.defaults;

import java.util.Collections;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class PluginsCommand extends BukkitCommand {
	public PluginsCommand(String name) {
	super(name);
	this.description = "Gets a list of plugins running on the server";
	this.usageMessage = "/plugins";
	this.setPermission("bukkit.command.plugins");
	this.setAliases(Collections.singletonList("pl"));
}
	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if (!testPermission(sender))
			return true;
		final int pluginSize = Bukkit.getPluginManager().getPlugins().length;
		sender.sendMessage(" ");
		sender.sendMessage("§e§lPlugins §7§o(" + pluginSize + ")");
		sender.sendMessage(" ");
//        sender.sendMessage("§7Legacy plugins §7§o(" + pluginSize + ")");
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			final TextComponent textComponent = new TextComponent(" §8» " + (plugin.isEnabled() ? "§a" : "§c") + plugin.getName());
			textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, this.createTextComponents(plugin)));
			sender.sendMessage(textComponent);
		}
//        sender.sendMessage(" ");
//        sender.sendMessage("§7Next-gen plugins §7§o(soon)"); // this will be plugins using a "newer" and "fresher" api with dependency injection and stuff like this.
		sender.sendMessage(" ");
		return true;
	}
	private BaseComponent[] createTextComponents(Plugin plugin) {
		final PluginDescriptionFile pluginDescription = plugin.getDescription();
		return new BaseComponent[]{
				new TextComponent("§7Version: " + pluginDescription.getVersion() + "\n"),
				new TextComponent("§7Author(s): " + pluginDescription.getAuthors() + "\n"),
				new TextComponent("§7Description: " + pluginDescription.getDescription()),
		};
	}
	private String getPluginList() {
		StringBuilder pluginList = new StringBuilder();
		Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
		for (Plugin plugin : plugins) {
			if (pluginList.length() > 0) {
				pluginList.append(ChatColor.WHITE);
				pluginList.append(", ");
			}
			pluginList.append(plugin.isEnabled() ? ChatColor.GREEN : ChatColor.RED);
			pluginList.append(plugin.getDescription().getName());
		}
		return "(" + plugins.length + "): " + pluginList.toString();
	}
	// Spigot Start
	@Override
	public java.util.List<String> tabComplete(CommandSender sender, String alias, String[] args)
			throws IllegalArgumentException {
		return java.util.Collections.emptyList();
	}
	// Spigot End
}
