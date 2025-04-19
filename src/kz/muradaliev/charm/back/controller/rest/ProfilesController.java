package kz.muradaliev.charm.back.controller.rest;

import com.fasterxml.jackson.databind.DatabindException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.muradaliev.charm.back.dto.ProfileFilter;
import kz.muradaliev.charm.back.mapper.JsonMapper;
import kz.muradaliev.charm.back.mapper.RequestToProfileFilterMapper;
import kz.muradaliev.charm.back.service.ProfileService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static kz.muradaliev.charm.back.utils.UrlUtils.PROFILES_URL;
import static kz.muradaliev.charm.back.utils.UrlUtils.REST_URL;

@WebServlet(REST_URL + PROFILES_URL)
@Slf4j
public class ProfilesController extends HttpServlet {

    private final ProfileService service = ProfileService.getInstance();
    private final JsonMapper jsonMapper = JsonMapper.getInstance();
    private final RequestToProfileFilterMapper requestToProfileFilterMapper = RequestToProfileFilterMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter writer = res.getWriter()) {
            ProfileFilter filter = requestToProfileFilterMapper.map(req);
            jsonMapper.writeValue(writer, service.findAll(filter));
        } catch (DatabindException ex) {
            req.setAttribute("errors", List.of(ex.getMessage()));
            res.sendError(SC_BAD_REQUEST);
        }
    }
}
