package com.orirock.appliedwebterminal;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class WebConfig {
    public static int webPort = 25590;
    public static boolean requireAuth = false;
    public static String username = "admin";
    public static String password = "password";

    public static void load(File configFile) {
        Configuration config = new Configuration(configFile);

        webPort = config.getInt("webPort", "general", webPort, 1024, 65535,
            "Web服务器监听的端口号");
        requireAuth = config.getBoolean("requireAuth", "security", requireAuth,
            "是否启用基本身份验证");
        username = config.getString("username", "security", username,
            "Web界面登录用户名");
        password = config.getString("password", "security", password,
            "Web界面登录密码");

        if (config.hasChanged()) {
            config.save();
        }
    }
}
