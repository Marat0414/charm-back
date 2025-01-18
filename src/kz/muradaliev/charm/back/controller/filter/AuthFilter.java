package kz.muradaliev.charm.back.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.muradaliev.charm.back.dto.UserDetails;
import kz.muradaliev.charm.back.model.Role;

import java.io.IOException;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static kz.muradaliev.charm.back.utils.UrlUtils.*;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String requestURI = req.getRequestURI();
        UserDetails userDetails = (UserDetails) req.getSession().getAttribute("userDetails");
        if (!LOGIN_REST_URL.equals(requestURI) && PRIVATE_PATHS.stream().anyMatch(requestURI::startsWith)) {
            if (userDetails == null) {
                res.sendError(SC_UNAUTHORIZED);
            } else if ((!requestURI.startsWith(REST_URL) &&
                    userDetails.getId().toString().equals(req.getParameter("id"))) ||
                    userDetails.getRole() == Role.ADMIN
            ) {
                filterChain.doFilter(req, res);
            } else {
                String message =
                        String.format("User with id %s and role %s try to use %s endpoint with query parameter %s",
                                userDetails.getId(), userDetails.getRole(), requestURI, req.getParameter("id"));
                req.setAttribute("errors", List.of(message));
                res.sendError(SC_FORBIDDEN);
            }
        } else if (userDetails != null && ENTRY_PATHS.contains(requestURI)) {
            res.sendRedirect(PROFILE_URL + "?id=" + userDetails.getId());
        } else {
            filterChain.doFilter(req, res);
        }
    }
}