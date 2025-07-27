package com.orirock.appliedwebterminal;

import appeng.api.AEApi;
import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingGrid;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IItemList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AE2Helper {
    public static List<String> getNetworkItemNames(IGrid grid) {
        List<String> items = new ArrayList<>();

        if (grid != null) {
            IStorageGrid storage = grid.getCache(IStorageGrid.class);
            if (storage != null) {
                IItemList<IAEItemStack> itemInventory = storage.getItemInventory().getStorageList();
                for (IAEItemStack stack : itemInventory) {
                    if (stack != null && stack.getItemStack() != null) {
                        items.add(stack.getItemStack().getDisplayName());
                    }
                }
            }
        }

        return items;
    }

    public static boolean requestCrafting(IGrid grid, EntityPlayer player, String itemName, int amount) {
        if (grid != null) {
            ICraftingGrid crafting = grid.getCache(ICraftingGrid.class);
            if (crafting != null) {
                // 在网络上查找匹配的物品
                IStorageGrid storage = grid.getCache(IStorageGrid.class);
                for (IAEItemStack stack : storage.getItemInventory().getStorageList()) {
                    if (stack != null && stack.getItemStack() != null &&
                        stack.getItemStack().getDisplayName().equals(itemName)) {
                        return crafting.beginCraftingJob(
                            player.worldObj, grid, player, stack, null, amount, null) != null;
                    }
                }
            }
        }
        return false;
    }
}
