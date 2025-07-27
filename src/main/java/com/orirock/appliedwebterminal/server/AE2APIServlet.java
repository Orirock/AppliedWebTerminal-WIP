package com.orirock.appliedwebterminal.server;

import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingGrid;
import appeng.api.storage.data.IAEItemStack;
import com.google.gson.Gson;
import com.orirock.appliedwebterminal.tiles.TileWebConnector;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AE2APIServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final TileWebConnector connector;

    public AE2APIServlet(TileWebConnector connector) {
        this.connector = connector;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String path = req.getPathInfo();
        resp.setContentType("application/json");

        if (path == null || path.equals("/")) {
            resp.getWriter().println("{\"status\":\"running\"}");
            return;
        }

        if (path.equals("/items")) {
            IGrid grid = connector.getGrid();
            if (grid != null) {
                List<String> items = getNetworkItems(grid);
                resp.getWriter().println(gson.toJson(items));
            } else {
                resp.sendError(503, "Not connected to AE network");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path.equals("/craft")) {
            String itemName = req.getParameter("item");
            int amount = Integer.parseInt(req.getParameter("amount"));

            EntityPlayerMP player = getFirstPlayer();
            if (player != null) {
                boolean success = requestCrafting(itemName, amount, player);
                resp.getWriter().println("{\"success\":" + success + "}");
            } else {
                resp.sendError(503, "No players online");
            }
        }
    }

    private List<String> getNetworkItems(IGrid grid) {
        List<String> items = new ArrayList<>();
        for (IAEItemStack stack : grid.getStorageGrid().getItemInventory().getStorageList()) {
            if (stack != null && stack.getItemStack() != null) {
                items.add(stack.getItemStack().getDisplayName());
            }
        }
        return items;
    }

    private boolean requestCrafting(String itemName, int amount, EntityPlayerMP player) {
        IGrid grid = connector.getGrid();
        if (grid != null) {
            ICraftingGrid crafting = grid.getCraftingGrid();
            for (IAEItemStack stack : grid.getStorageGrid().getItemInventory().getStorageList()) {
                if (stack != null && stack.getItemStack() != null &&
                    stack.getItemStack().getDisplayName().equals(itemName)) {
                    return crafting.beginCraftingJob(
                        player.worldObj, grid, player, stack, null, amount, null) != null;
                }
            }
        }
        return false;
    }

    private EntityPlayerMP getFirstPlayer() {
        if (MinecraftServer.getServer() != null) {
            return (EntityPlayerMP) MinecraftServer.getServer()
                .getConfigurationManager().playerEntityList.get(0);
        }
        return null;
    }
}
