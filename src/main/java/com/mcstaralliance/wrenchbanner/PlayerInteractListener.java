package com.mcstaralliance.wrenchbanner;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        String wrench = "create_wrench";
        if (!item.getType().name().equalsIgnoreCase(wrench)) {
            // 如果物品不是扳手
            return;
        }
        Player player = event.getPlayer();
        
        ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(event.getClickedBlock().getLocation());
        if (residence != null && residence.getPermissions().playerHas(player, Flags.destroy, false)) {
            return;
        }

        if (residence == null) {
            return;
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            Location loc = event.getClickedBlock().getLocation();
            int x = loc.getBlockX();
            int y = loc.getBlockY();
            int z = loc.getBlockZ();
            String pos = x + "," + y + "," + z;
            if (p.isOp()) {
                p.sendMessage("WrenchBanner 已在" + pos + "阻止疑似刷物品行为。");
                p.sendMessage("物品名: " + item.getType().name());
            }
        }
        event.setCancelled(true);
        player.sendMessage(ChatColor.RED + "请在领地内使用机械动力扳手。");
    }
}
