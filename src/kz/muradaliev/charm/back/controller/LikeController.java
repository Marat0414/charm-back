package kz.muradaliev.charm.back.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.muradaliev.charm.back.service.LikeService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/like")
public class LikeController extends HttpServlet {
    private String servletName;

    private final LikeService likeService = LikeService.getInstance();

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.servletName = config.getServletName();
        System.out.println("Init servlet " + config.getServletName());
    }

//    @Override
//    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.service(req, resp);
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String answer = "10";

        if (id != null) {
            long l = Long.parseLong(id);
            long answerL = likeService.getLikesById(l);
            answer = answerL + "";
        }
        String userAgent = req.getHeader("User-Agent");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = resp.getWriter()) {
            writer.write("<h2>");
            writer.write("<p>");
            writer.write("Answer :" + answer);
            writer.write("</p>");

            writer.write("<p>");
            writer.write("Header userAgent :" + userAgent);
            writer.write("</p>");

            writer.write("<p>");
            writer.write("Header userAgent :" + req.getRequestURI());
            writer.write("</p>");

            writer.write("</h2>");
        }



    }

    @Override
    public void destroy() {
        System.out.println("Destroy servlet " + servletName);
    }
}
