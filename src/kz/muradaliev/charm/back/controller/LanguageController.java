package kz.muradaliev.charm.back.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/lang")
public class LanguageController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lang = req.getParameter("lang");
        System.out.println("Cookie header:" + req.getHeader("cookie"));
        Cookie cookie = new Cookie("lang", "en");
//        cookie.setMaxAge(100);.
        if("ru".equals(lang)){
            cookie.setValue("ru");
        }
        resp.addCookie(cookie);
        String referer = req.getHeader("referer"); // Путь, с которого нам сделали запрос

//        resp.setHeader("Set-Cookie","lang=ru");
        resp.sendRedirect(referer);
    }
}
