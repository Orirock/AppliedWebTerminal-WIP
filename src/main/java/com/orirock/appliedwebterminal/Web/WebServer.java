package com.orirock.appliedwebterminal.Web;

import com.orirock.appliedwebterminal.WebConfig;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class WebServer {
    private Server server;
    private int port = WebConfig.webPort;

    public void start() throws Exception {
        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // 添加 API Servlet
        context.addServlet(new ServletHolder(new AE2ApiServlet()), "/api/*");

        // 静态资源处理
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{"index.html"});
        resourceHandler.setResourceBase(Resource.newClassPathResource("/web").getURI().toString());

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});
        server.setHandler(handlers);

        server.start();
        System.out.println("[AE2Web] Web 服务器已启动，端口: " + port);
    }

    public void stop() throws Exception {
        if (server != null) {
            server.stop();
        }
    }
}
