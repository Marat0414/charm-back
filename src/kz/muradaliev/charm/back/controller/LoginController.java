package kz.muradaliev.charm.back.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.muradaliev.charm.back.dto.LoginDto;
import kz.muradaliev.charm.back.dto.UserDetails;
import kz.muradaliev.charm.back.mapper.RequestToLoginDtoMapper;
import kz.muradaliev.charm.back.service.ProfileService;
import kz.muradaliev.charm.back.validator.LoginValidator;
import kz.muradaliev.charm.back.validator.ValidationResult;

import java.io.IOException;
import java.util.Optional;

import static kz.muradaliev.charm.back.utils.UrlUtils.*;

@WebServlet(LOGIN_URL)
public class LoginController extends HttpServlet {

    private final ProfileService service = ProfileService.getInstance();

    private final RequestToLoginDtoMapper requestToLoginDtoMapper = RequestToLoginDtoMapper.getInstance();

    private final LoginValidator loginValidator = LoginValidator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher(getJspPath(LOGIN_URL)).forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        LoginDto dto = requestToLoginDtoMapper.map(req);
        ValidationResult validationResult = loginValidator.validate(dto);
        if (validationResult.isValid()) {
            Optional<UserDetails> userDetailsOpt = service.login(dto);
            if (userDetailsOpt.isPresent()) {
                UserDetails userDetails = userDetailsOpt.get();
                req.getSession().setAttribute("userDetails", userDetails);
                res.sendRedirect(String.format(PROFILE_URL + "?id=%s", userDetails.getId()));
            } else {
                validationResult.add("error.password.invalid");
                req.setAttribute("errors", validationResult.getErrors());
                doGet(req, res);
            }
        } else {
            req.setAttribute("errors", validationResult.getErrors());
            doGet(req, res);
        }
    }
}