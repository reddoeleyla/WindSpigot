package org.spigotmc;

import org.bukkit.Bukkit;

public class AsyncCatcher {

	public static boolean enabled = false;

	public static void catchOp(String reason) {
		if (!enabled || Bukkit.isPrimaryThread()) {
			return;
		}
		throw new IllegalStateException("Asyncronus " + reason);
	}
}
