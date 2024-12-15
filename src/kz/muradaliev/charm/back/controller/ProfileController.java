package kz.muradaliev.charm.back.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.muradaliev.charm.back.model.Gender;
import kz.muradaliev.charm.back.model.Profile;
import kz.muradaliev.charm.back.service.ProfileService;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {
    private final ProfileService service = ProfileService.getInstance();

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        if (servletContext.getAttribute("genders") == null) {
            servletContext.setAttribute("genders", Gender.values());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sId = req.getParameter("id");
        String forwardUri = "/notFound";
        if (sId != null) {
            Optional<Profile> profile = service.findById(Long.parseLong(sId));
            if (profile.isPresent()) {
                req.setAttribute("profile", profile.get());
                forwardUri = "WEB-INF/jsp/profile.jsp";
            }

        }
        req.getRequestDispatcher(forwardUri).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sId = req.getParameter("id");
        Profile profile = new Profile();
        profile.setEmail(req.getParameter("email"));
        profile.setName(req.getParameter("name"));
        profile.setSurname(req.getParameter("surname"));
        profile.setAbout(req.getParameter("about"));
        profile.setGender(Gender.valueOf(req.getParameter("gender")));
        Long id;
        if (!sId.isBlank()) {
            id = Long.parseLong(sId);
            profile.setId(id);
            service.update(profile);
        } else {
            id = service.save(profile).getId();
        }
        resp.sendRedirect(String.format("/profile?id=%s", id));
    }
}
