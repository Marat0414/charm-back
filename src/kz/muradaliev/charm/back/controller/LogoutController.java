package kz.muradaliev.charm.back.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.muradaliev.charm.back.dto.LoginDto;
import kz.muradaliev.charm.back.dto.ProfileGetDto;
import kz.muradaliev.charm.back.mapper.RequestToLoginDtoMapper;
import kz.muradaliev.charm.back.service.ProfileService;

import java.io.IOException;
import java.util.Optional;

import static kz.muradaliev.charm.back.utils.UrlUtils.LOGIN_URL;
import static kz.muradaliev.charm.back.utils.UrlUtils.LOGOUT_URL;

@WebServlet(LOGOUT_URL)
public class LogoutController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().invalidate();
        resp.sendRedirect(LOGIN_URL);
    }
}