package com.orirock.appliedwebterminal.tiles;

import appeng.api.AEApi;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridBlock;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.util.AECableType;
import appeng.api.util.DimensionalCoord;
import com.orirock.appliedwebterminal.server.WebServerManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;

public class TileWebConnector extends TileEntity implements IGridHost {

    private IGridNode gridNode;
    private String username = "admin";
    private String password = "password";
    private int port = 8080;
    private boolean active = false;
    private WebServerManager webServer;

    @Override
    public IGridNode getGridNode(ForgeDirection dir) {
        if (gridNode == null) {
            gridNode = AEApi.instance().createGridNode(this);
        }
        return gridNode;
    }

    @Override
    public void onReady() {
        if (!worldObj.isRemote) {
            if (gridNode != null && gridNode.isActive() && !active) {
                startWebServer();
                active = true;
                markDirty();
            }
        }
    }

    @Override
    public void validate() {
        super.validate();
        if (!worldObj.isRemote && gridNode != null) {
            gridNode.updateState();
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (!worldObj.isRemote && active) {
            stopWebServer();
            active = false;
            markDirty();
        }
    }

    private void startWebServer() {
        if (webServer == null) {
            webServer = new WebServerManager(this);
            webServer.start();
        }
    }

    private void stopWebServer() {
        if (webServer != null) {
            webServer.stop();
            webServer = null;
        }
    }

    // IGridBlock 实现
    public double getIdlePowerUsage() {
        return 1.0;
    }

    public EnumSet<ForgeDirection> getConnectableSides() {
        return EnumSet.allOf(ForgeDirection.class);
    }

    public DimensionalCoord getLocation() {
        return new DimensionalCoord(this);
    }

    public AECableType getCableConnectionType(ForgeDirection dir) {
        return AECableType.SMART;
    }

    public void securityBreak() {
        // 安全破坏处理
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.username = compound.getString("username");
        this.password = compound.getString("password");
        this.port = compound.getInteger("port");
        this.active = compound.getBoolean("active");
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("username", username);
        compound.setString("password", password);
        compound.setInteger("port", port);
        compound.setBoolean("active", active);
    }

    // Getter方法
    public IGrid getGrid() { return gridNode != null ? gridNode.getGrid() : null; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getPort() { return port; }
    public boolean isActive() { return active; }

    // Setter方法
    public void setUsername(String username) { this.username = username; markDirty(); }
    public void setPassword(String password) { this.password = password; markDirty(); }
    public void setPort(int port) { this.port = port; markDirty(); }
}
