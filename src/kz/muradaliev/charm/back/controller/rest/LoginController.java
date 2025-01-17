package kz.muradaliev.charm.back.controller.rest;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.*;
import static kz.muradaliev.charm.back.utils.UrlUtils.LOGIN_REST_URL;

@WebServlet(LOGIN_REST_URL)
public class LoginController extends HttpServlet {

    private final ProfileService service = ProfileService.getInstance();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final LoginValidator loginValidator = LoginValidator.getInstance();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try (BufferedReader reader = req.getReader()) {
            LoginDto dto = objectMapper.readValue(reader, LoginDto.class);
            ValidationResult validationResult = loginValidator.validate(dto);
            if (validationResult.isValid()) {
                Optional<UserDetails> userDetailsOpt = service.getUserDetails(dto.getEmail());
                if (userDetailsOpt.isPresent()) {
                    UserDetails userDetails = userDetailsOpt.get();
                    req.getSession().setAttribute("userDetails", userDetails);
                } else {
                    resp.sendError(SC_NOT_FOUND);
                }
            } else {
                req.setAttribute("errors", validationResult.getErrors());
                resp.sendError(SC_BAD_REQUEST);
            }

        } catch (DatabindException ex) {
            req.setAttribute("errors", ex.getMessage());
            resp.sendError(SC_BAD_REQUEST);
        }
    }

}
