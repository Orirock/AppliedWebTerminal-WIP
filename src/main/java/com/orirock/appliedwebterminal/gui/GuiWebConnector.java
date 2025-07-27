package com.orirock.appliedwebterminal.gui;

import com.orirock.appliedwebterminal.tiles.TileWebConnector;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiWebConnector extends GuiScreen {

    private TileWebConnector tile;
    private GuiTextField usernameField;
    private GuiTextField passwordField;

    public GuiWebConnector(TileWebConnector tile) {
        this.tile = tile;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();

        this.usernameField = new GuiTextField(0, this.fontRendererObj,
            width / 2 - 100, height / 2 - 40, 200, 20);
        this.usernameField.setText(tile.getUsername());
        this.usernameField.setFocused(true);

        this.passwordField = new GuiTextField(1, this.fontRendererObj,
            width / 2 - 100, height / 2 - 10, 200, 20);
        this.passwordField.setText(tile.getPassword());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(fontRendererObj, "Web连接器配置", width / 2, height / 2 - 70, 0xFFFFFF);
        this.drawString(fontRendererObj, "用户名:", width / 2 - 100, height / 2 - 55, 0xA0A0A0);
        this.drawString(fontRendererObj, "密码:", width / 2 - 100, height / 2 - 25, 0xA0A0A0);

        this.usernameField.drawTextBox();
        this.passwordField.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.usernameField.textboxKeyTyped(typedChar, keyCode);
        this.passwordField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.usernameField.mouseClicked(mouseX, mouseY, mouseButton);
        this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        tile.setUsername(usernameField.getText());
        tile.setPassword(passwordField.getText());
        tile.markDirty();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
