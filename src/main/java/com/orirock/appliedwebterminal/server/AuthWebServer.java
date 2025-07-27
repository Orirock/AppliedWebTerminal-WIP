package com.orirock.appliedwebterminal.server;

import com.orirock.appliedwebterminal.tiles.TileWebConnector;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;

public class AuthWebServer {
    private final TileWebConnector connector;
    private Server server;

    public AuthWebServer(TileWebConnector connector) {
        this.connector = connector;
    }

    public void start() throws Exception {
        server = new Server(connector.getPort());

        // 创建安全约束
        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{"user"});
        constraint.setAuthenticate(true);

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec("/*");
        mapping.setConstraint(constraint);

        // 配置安全处理器
        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        security.addConstraintMapping(mapping);
        security.setAuthenticator(new BasicAuthenticator());

        // 使用方块中的用户名和密码
        HashLoginService loginService = new HashLoginService();
        loginService.putUser(connector.getUsername(),
            Credential.getCredential(connector.getPassword()),
            new String[]{"user"});
        security.setLoginService(loginService);

        // 创建上下文
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");

        // 添加API Servlet
        context.addServlet(new ServletHolder(new AE2ApiServlet(connector)), "/api/*");

        security.setHandler(context);
        server.setHandler(security);

        server.start();
    }

    public void stop() throws Exception {
        if (server != null) {
            server.stop();
        }
    }
}
