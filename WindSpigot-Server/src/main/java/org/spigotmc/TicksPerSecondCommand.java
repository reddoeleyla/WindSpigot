package org.spigotmc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;

public class TicksPerSecondCommand extends Command {

	public TicksPerSecondCommand(String name) {
		super(name);
		this.description = "Gets the current ticks per second for the server";
		this.usageMessage = "/tps";
		this.setPermission("bukkit.command.tps");
	}

	private String getCenteredMessageHeader(String color, String message) {
		int length = 25 - ((message.length() + 2) / 2);
		StringBuilder res = new StringBuilder("§8§m┃");
		for (int i = 0; i < length; i++) res.append("-");
		res.append("§8┃ ").append(color).append(message).append(" §8§m┃");
		for (int i = 0; i < length; i++) res.append("-");
		res.append("§8┃");
		return res.toString();
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if (!testPermission(sender)) {
			return true;
		}

		// PaperSpigot start - Further improve tick handling
		double[] tps = org.bukkit.Bukkit.spigot().getTPS();
		String[] tpsAvg = new String[tps.length];

		for (int i = 0; i < tps.length; i++) {
			tpsAvg[i] = format(tps[i]);
		}
		
		// WindSpigot - more detailed tps cmd
		
		int entityCount = 0;
		
		for (WorldServer world : MinecraftServer.getServer().worlds) {
			entityCount = entityCount + world.entityList.size();
		}
		
		int tileEntityCount = 0;
		
		for (WorldServer world : MinecraftServer.getServer().worlds) {
			tileEntityCount = tileEntityCount + world.tileEntityList.size();
		}

		double tps0 = MinecraftServer.getServer().recentTps[0];
		String ticksPerSecond = (tps0 > 18.0 ? ChatColor.GREEN : (tps0 > 16.0 ? ChatColor.YELLOW : ChatColor.RED)).toString() + (tps0 > 20.0 ? "*" : "") + Math.min((double) Math.round(tps0 * 100.0) / 100.0, 20.0);
		sender.sendMessage(this.getCenteredMessageHeader("§c", "Performance"));
		sender.sendMessage("§r");
		sender.sendMessage("§8» §7TPS 1m§8: §r" + ticksPerSecond);
		sender.sendMessage("§8» §7Memory Usage§8: §a" + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + "§8/§c" +  (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + "MB §8(§e" + String.format("%.2f", Double.parseDouble(String.valueOf((((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024))/1000L)))) + "GB§8)");
		sender.sendMessage("§8» §7Players online§8: §a" + Bukkit.getOnlinePlayers().size());
		sender.sendMessage("§8» §7Entities§8: " + ChatColor.GREEN + entityCount);
		sender.sendMessage("§8» §7Tile entities§8: " + ChatColor.GREEN + tileEntityCount);
		sender.sendMessage("§8» §7Mob AI§8: " + (MinecraftServer.getServer().worlds.get(0).nachoSpigotConfig.enableMobAI ? ChatColor.GREEN + "✔" : ChatColor.RED + "✘"));
		sender.sendMessage("§8» §7Last tick §8(§ems§8)§8: " + ChatColor.GREEN + Math.round(MinecraftServer.getServer().getLastMspt() * 100.0) / 100.0);
		sender.sendMessage("§r");
		sender.sendMessage(this.getCenteredMessageHeader("§c", "Performance"));
		return true;
	}

	private static String format(double tps) {
		return ((tps > 18.0) ? ChatColor.GREEN : (tps > 16.0) ? ChatColor.YELLOW : ChatColor.RED) + ((tps > 21.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0);
	}
}
