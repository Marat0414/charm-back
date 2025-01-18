package kz.muradaliev.charm.back.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.muradaliev.charm.back.dto.ProfileGetDto;
import kz.muradaliev.charm.back.dto.RegistrationDto;
import kz.muradaliev.charm.back.mapper.RequestToRegistrationDtoMapper;
import kz.muradaliev.charm.back.model.Profile;
import kz.muradaliev.charm.back.service.ProfileService;
import kz.muradaliev.charm.back.validator.RegistrationValidator;
import kz.muradaliev.charm.back.validator.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static kz.muradaliev.charm.back.utils.UrlUtils.*;

@WebServlet("/registration2")
public class RegistrationController extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);
    private final ProfileService service = ProfileService.getInstance();

    private final RequestToRegistrationDtoMapper requestToRegistrationDtoMapper = RequestToRegistrationDtoMapper.getInstance();

    private final RegistrationValidator registrationValidator = RegistrationValidator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher(getJspPath(REGISTRATION_URL)).forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        RegistrationDto dto = requestToRegistrationDtoMapper.map(req);
        ValidationResult validationResult = registrationValidator.validate(dto);
        if (validationResult.isValid()) {
            Long id = service.save(dto);
            log.info("Profile with the email address {} has been registered with id {}", dto.getEmail(), id);
            res.sendRedirect(LOGIN_URL);
        } else {
            req.setAttribute("errors", validationResult.getErrors());
            doGet(req, res);
        }
    }
}