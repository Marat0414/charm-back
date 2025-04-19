package kz.muradaliev.charm.back.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.muradaliev.charm.back.dto.ProfileFilter;
import kz.muradaliev.charm.back.mapper.RequestToProfileFilterMapper;
import kz.muradaliev.charm.back.service.ProfileService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static kz.muradaliev.charm.back.utils.UrlUtils.PROFILES_URL;
import static kz.muradaliev.charm.back.utils.UrlUtils.getJspPath;

@WebServlet(PROFILES_URL)
@Slf4j
public class ProfilesController extends HttpServlet {
    private final ProfileService service = ProfileService.getInstance();
    private final RequestToProfileFilterMapper requestToProfileFilterMapper = RequestToProfileFilterMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        ProfileFilter filter = requestToProfileFilterMapper.map(req);
        req.setAttribute("profiles", service.findAll(filter));
        req.setAttribute("filter", filter);
        req.getRequestDispatcher(getJspPath(PROFILES_URL)).forward(req, res);
    }
}
