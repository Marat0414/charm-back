package kz.muradaliev.charm.back.controller.rest;

import com.fasterxml.jackson.databind.DatabindException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import kz.muradaliev.charm.back.dao.ProfileDao;
import kz.muradaliev.charm.back.dto.ProfileFullUpdateDto;
import kz.muradaliev.charm.back.dto.ProfileGetDto;
import kz.muradaliev.charm.back.dto.RegistrationDto;
import kz.muradaliev.charm.back.dto.UserDetails;
import kz.muradaliev.charm.back.mapper.JsonMapper;
import kz.muradaliev.charm.back.service.ProfileService;
import kz.muradaliev.charm.back.validator.ProfileFullUpdateValidator;
import kz.muradaliev.charm.back.validator.RegistrationValidator;
import kz.muradaliev.charm.back.validator.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static jakarta.servlet.http.HttpServletResponse.SC_NO_CONTENT;
import static kz.muradaliev.charm.back.utils.StringUtils.isBlank;
import static kz.muradaliev.charm.back.utils.UrlUtils.PROFILE_URL;
import static kz.muradaliev.charm.back.utils.UrlUtils.REST_URL;

@WebServlet(REST_URL + PROFILE_URL)
@MultipartConfig
public class ProfileController extends HttpServlet {
    private static java.util.logging.Logger log = Logger.getLogger(ProfileController.class.getName());
    private final ProfileService service = ProfileService.getInstance();
    private final JsonMapper jsonMapper = JsonMapper.getInstance();
    private final ProfileFullUpdateValidator profileFullUpdateValidator = ProfileFullUpdateValidator.getInstance();
    private final RegistrationValidator registrationValidator = RegistrationValidator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (PrintWriter writer = res.getWriter()) {
            String sId = req.getParameter("id");
            Long id = sId == null ? null : Long.parseLong(sId);
            Optional<ProfileGetDto> optProfileDto = service.findById(id);
            if (optProfileDto.isPresent()) {
                jsonMapper.writeValue(writer, optProfileDto.get());
            } else {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (DatabindException ex) {
            req.setAttribute("errors", List.of(ex.getMessage()));
            res.sendError(SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try (BufferedReader reader = req.getReader()) {
            RegistrationDto dto = jsonMapper.readValue(reader, RegistrationDto.class);
            ValidationResult validationResult = registrationValidator.validate(dto);
            if (validationResult.isValid()) {
                Long id = service.save(dto);
                log.info("Profile with the email address " + dto.getEmail() + " {} has been registered with id " +  + id);
            } else {
                req.setAttribute("errors", validationResult.getErrors());
                res.sendError(SC_BAD_REQUEST);
            }
        } catch (DatabindException ex) {
            req.setAttribute("errors", List.of(ex.getLocalizedMessage(), ex.getOriginalMessage()));
            res.sendError(SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        Part json = req.getPart("json");
        if (json != null) {
            Part photo = req.getPart("photo");
            try (InputStream inputStream = json.getInputStream()) {
                ProfileFullUpdateDto dto = jsonMapper.readValue(inputStream, ProfileFullUpdateDto.class);
                if (photo != null && !isBlank(photo.getSubmittedFileName())) {
                    dto.setPhoto(photo);
                } else {
                    dto.setPhoto(null);
                }
                ValidationResult validationResult = profileFullUpdateValidator.validate(dto);
                if (validationResult.isValid()) {
                    service.update(dto);
                } else {
                    req.setAttribute("errors", validationResult.getErrors());
                    res.sendError(SC_BAD_REQUEST);
                }
            } catch (DatabindException ex) {
                req.setAttribute("errors", List.of(ex.getLocalizedMessage(), ex.getOriginalMessage()));
                res.sendError(SC_BAD_REQUEST);
            }
        } else {
            res.sendError(SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String sId = req.getParameter("id");
        if (!isBlank(sId) && service.delete(Long.parseLong(sId))) {
            UserDetails userDetails = (UserDetails) req.getSession().getAttribute("userDetails");
            if (sId.equals(userDetails.getId().toString())) {
                req.getSession().invalidate();
            }
            res.setStatus(SC_NO_CONTENT);
        } else {
            res.sendError(SC_NOT_FOUND);
        }

    }
}