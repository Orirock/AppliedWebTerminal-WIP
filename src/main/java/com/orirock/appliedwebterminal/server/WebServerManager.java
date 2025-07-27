package com.orirock.appliedwebterminal.server;

import com.orirock.appliedwebterminal.tiles.TileWebTerminal;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class WebServerManager {
    private final TileWebTerminal terminal;
    private Server server;

    public WebServerManager(TileWebTerminal terminal) {
        this.terminal = terminal;
    }

    public void start() {
        new Thread(() -> {
            try {
                server = new Server(terminal.getPort());

                ServletContextHandler context = new ServletContextHandler();
                context.setContextPath("/");

                // 添加API Servlet
                context.addServlet(new ServletHolder(new AE2APIServlet(terminal)), "/api/*");

                server.setHandler(context);
                server.start();
                server.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void stop() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
