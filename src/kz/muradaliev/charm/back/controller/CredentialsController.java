package kz.muradaliev.charm.back.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.muradaliev.charm.back.dto.CredentialsDto;
import kz.muradaliev.charm.back.dto.ProfileGetDto;
import kz.muradaliev.charm.back.mapper.RequestToCredentialsDtoMapper;
import kz.muradaliev.charm.back.service.ProfileService;
import kz.muradaliev.charm.back.validator.CredentialsValidator;
import kz.muradaliev.charm.back.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static kz.muradaliev.charm.back.utils.StringUtils.isBlank;
import static kz.muradaliev.charm.back.utils.UrlUtils.*;

@WebServlet(CREDENTIALS_URL)
@MultipartConfig
public class CredentialsController extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(CredentialsController.class);

    private final ProfileService service = ProfileService.getInstance();

    private final RequestToCredentialsDtoMapper requestToCredentialsDtoMapper = RequestToCredentialsDtoMapper.getInstance();

    private final CredentialsValidator credentialsValidator = CredentialsValidator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String sId = req.getParameter("id");
        String forwardUri = null;
        if (sId != null) {
            Optional<ProfileGetDto> optProfileDto = service.findById(Long.parseLong(sId));
            if (optProfileDto.isPresent()) {
                req.setAttribute("profile", optProfileDto.get());
                forwardUri = getJspPath(CREDENTIALS_URL);
            }
        }
        if (forwardUri == null) {
            res.sendError(SC_NOT_FOUND);
        } else {
            req.getRequestDispatcher(forwardUri).forward(req, res);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        CredentialsDto dto = requestToCredentialsDtoMapper.map(req);
        ValidationResult validationResult = credentialsValidator.validate(dto);
        if (validationResult.isValid()) {
            service.update(dto);
            if (!isBlank(dto.getEmail())) {
                log.warn("Profile with id {} changed email to {}", dto.getId(), dto.getEmail());
            }
            res.sendRedirect(String.format(PROFILE_URL + "?id=%s", dto.getId()));
        } else {
            req.setAttribute("errors", validationResult.getErrors());
            doGet(req, res);
        }
    }
}
