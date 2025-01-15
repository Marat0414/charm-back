package kz.muradaliev.charm.back.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.muradaliev.charm.back.service.ContentService;

import java.io.IOException;

@WebServlet("/content/*")
public class ContentController extends HttpServlet {

    public static final ContentService contentService = ContentService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String contentPath = req.getRequestURI().replace("/content", "");
        resp.setContentType("application/octet-stream");
        try {
            contentService.download(contentPath, resp.getOutputStream());
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }
}
