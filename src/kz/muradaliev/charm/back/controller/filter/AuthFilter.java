package kz.muradaliev.charm.back.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.muradaliev.charm.back.dto.UserDetails;
import kz.muradaliev.charm.back.model.Role;

import java.io.IOException;
import java.util.Set;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static kz.muradaliev.charm.back.utils.UrlUtils.LOGIN_REST_URL;
import static kz.muradaliev.charm.back.utils.UrlUtils.REST_URL;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final Set<String> PRIVATE_PATHS = Set.of("/profile", "/credentials");

    private static final Set<String> ADMIN_PATHS = Set.of("/profile");

    private static final Set<String> ENTRY_PATHS = Set.of("/login", "/registration");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        String requestURI = req.getRequestURI();
        UserDetails userDetails = (UserDetails) req.getSession().getAttribute("userDetails");
        if (!LOGIN_REST_URL.equals(requestURI) &&  PRIVATE_PATHS.stream().anyMatch(requestURI::startsWith)) {
            if (userDetails == null) {
                res.sendError(SC_UNAUTHORIZED);
            } else if ((!requestURI.startsWith(REST_URL)  && userDetails.getId().toString().equals(req.getParameter("id"))) ||
                            userDetails.getRole() == Role.ADMIN
            ) {
                filterChain.doFilter(req, res);
            } else {
                res.sendError(SC_FORBIDDEN);
            }
        } else if (userDetails != null && ENTRY_PATHS.contains(requestURI)) {
            res.sendRedirect("/profile?id=" + userDetails.getId());
        } else {
            filterChain.doFilter(req, res);
        }

    }
}