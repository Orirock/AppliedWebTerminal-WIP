package com.orirock.appliedwebterminal.blocks;

import com.orirock.appliedwebterminal.AppliedWebTerminalMod;
import com.orirock.appliedwebterminal.tiles.TileWebConnector;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockWebConnector extends BlockContainer {

    public BlockWebConnector() {
        super(Material.iron);
        this.setBlockName("web_connector");
        this.setHardness(3.5f);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        GameRegistry.registerBlock(this, "web_connector");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileWebConnector();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int side,
                                    float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(x, y, z);
            if (te instanceof TileWebConnector) {
                player.openGui(AppliedWebTerminalMod.instance, 0, world, x, y, z);
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileWebConnector) {
            ((TileWebConnector) te).invalidate();
        }
        super.breakBlock(world, x, y, z, block, meta);
    }
}
