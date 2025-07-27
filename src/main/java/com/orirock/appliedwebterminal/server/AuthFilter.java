package com.orirock.appliedwebterminal.server;

import com.orirock.appliedwebterminal.tiles.TileWebConnector;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

public class AuthFilter implements Filter {

    private final TileWebConnector connector;

    public AuthFilter(TileWebConnector connector) {
        this.connector = connector;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String credentials = new String(Base64.getDecoder().decode(
                authHeader.substring(6)), "UTF-8");
            String[] parts = credentials.split(":");
            if (parts.length == 2) {
                if (parts[0].equals(connector.getUsername()) &&
                    parts[1].equals(connector.getPassword())) {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        httpResponse.setHeader("WWW-Authenticate", "Basic realm=\"AE2 Web Terminal\"");
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
