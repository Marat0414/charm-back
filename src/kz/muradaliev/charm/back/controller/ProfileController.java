package kz.muradaliev.charm.back.controller;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kz.muradaliev.charm.back.dto.ProfileGetDto;
import kz.muradaliev.charm.back.dto.ProfileUpdateDto;
import kz.muradaliev.charm.back.dto.UserDetails;
import kz.muradaliev.charm.back.mapper.ProfileGetDtoToPdfMapper;
import kz.muradaliev.charm.back.mapper.RequestToProfileUpdateDtoMapper;
import kz.muradaliev.charm.back.service.ProfileService;
import kz.muradaliev.charm.back.validator.ProfileUpdateValidator;
import kz.muradaliev.charm.back.validator.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static kz.muradaliev.charm.back.utils.StringUtils.isBlank;
import static kz.muradaliev.charm.back.utils.UrlUtils.*;

@WebServlet(PROFILE_URL + "/*")
@MultipartConfig
public class ProfileController extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    private final ProfileService service = ProfileService.getInstance();

    private final RequestToProfileUpdateDtoMapper requestToProfileUpdateDtoMapper = RequestToProfileUpdateDtoMapper.getInstance();

    private final ProfileGetDtoToPdfMapper profileGetDtoToPdfMapper = ProfileGetDtoToPdfMapper.getInstance();

    private final ProfileUpdateValidator profileUpdateValidator = ProfileUpdateValidator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sId = req.getParameter("id");
        if (sId != null) {
            Optional<ProfileGetDto> optProfileGetDto = service.findById(Long.parseLong(sId));
            if (optProfileGetDto.isPresent()) {
                ProfileGetDto profileGetDto = optProfileGetDto.get();
                if (req.getRequestURI().equals("/profile/pdf")) {
                    resp.setHeader("Content-Disposition", "attachment; filename=\"resume.pdf\"");
                    resp.setContentType("application/pdf");
                    resp.setCharacterEncoding("UTF-8");
                    try (OutputStream outputStream = resp.getOutputStream()) {
                        Document pdf = new Document();
                        PdfWriter.getInstance(pdf, outputStream);
                        profileGetDtoToPdfMapper.map(profileGetDto, pdf);
                    } catch (DocumentException e) {
                        throw new IOException(e);
                    }
                } else {
                    req.setAttribute("profile", profileGetDto);
                    req.getRequestDispatcher(getJspPath(PROFILE_URL)).forward(req, resp);
                }
            } else {
                resp.sendError(SC_NOT_FOUND);
            }
        } else {
            req.setAttribute("profiles", service.findAll());
            req.getRequestDispatcher(getJspPath("/profiles")).forward(req, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ProfileUpdateDto dto = requestToProfileUpdateDtoMapper.map(req);
        ValidationResult validationResult = profileUpdateValidator.validate(dto);
        if (validationResult.isValid()) {
            service.update(dto);
            String referer = req.getHeader("referer");
            resp.sendRedirect(referer);
        } else {
            req.setAttribute("errors", validationResult.getErrors());
            doGet(req, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String sId = req.getParameter("id");
        if (!isBlank(sId) && service.delete(Long.parseLong(sId))) {
            log.info("Profile with id {} has been deleted", sId);
            UserDetails userDetails = (UserDetails) req.getSession().getAttribute("userDetails");
            if (sId.equals(userDetails.getId().toString())) {
                req.getSession().invalidate();
            }
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            resp.sendRedirect(REGISTRATION_URL);
        } else {
            resp.sendError(SC_NOT_FOUND);
        }
    }
}