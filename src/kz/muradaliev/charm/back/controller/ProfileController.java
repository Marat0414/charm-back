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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ProfileController extends HttpServlet {
    private final ProfileService service = ProfileService.getInstance();

    private final RequestToProfileUpdateDtoMapper requestToProfileUpdateDtoMapper = RequestToProfileUpdateDtoMapper.getInstance();

    private final ProfileGetDtoToPdfMapper profileGetDtoToPdfMapper = ProfileGetDtoToPdfMapper.getInstance();

    private final ProfileUpdateValidator profileUpdateValidator = ProfileUpdateValidator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String sId = req.getParameter("id");
        Long id = sId == null ? null : Long.parseLong(sId);
        Optional<ProfileGetDto> optProfileGetDto = service.findById(id);
        if (optProfileGetDto.isPresent()) {
            ProfileGetDto profileGetDto = optProfileGetDto.get();
            if (req.getRequestURI().equals(PROFILE_URL + PDF_URL)) {
                res.setHeader("Content-Disposition", "attachment; filename=\"resume.pdf\"");
                res.setContentType("application/pdf");
                try (OutputStream outputStream = res.getOutputStream()) {
                    Document pdf = new Document();
                    PdfWriter.getInstance(pdf, outputStream);
                    profileGetDtoToPdfMapper.map(profileGetDto, pdf);
                } catch (DocumentException e) {
                    throw new IOException(e);
                }
            } else {
                req.setAttribute("profile", profileGetDto);
                req.getRequestDispatcher(getJspPath(PROFILE_URL)).forward(req, res);
            }
        } else {
            res.sendError(SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        ProfileUpdateDto dto = requestToProfileUpdateDtoMapper.map(req);
        ValidationResult validationResult = profileUpdateValidator.validate(dto);
        if (validationResult.isValid()) {
            service.update(dto);
            String referer = req.getHeader("referer");
            res.sendRedirect(referer);
        } else {
            req.setAttribute("errors", validationResult.getErrors());
            doGet(req, res);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String sId = req.getParameter("id");
        if (!isBlank(sId) && service.delete(Long.parseLong(sId))) {
            log.info("Profile with id {} has been deleted", sId);
            UserDetails userDetails = (UserDetails) req.getSession().getAttribute("userDetails");
            if (sId.equals(userDetails.getId().toString())) {
                req.getSession().invalidate();
            }
            res.setStatus(HttpServletResponse.SC_NO_CONTENT);
            res.sendRedirect(REGISTRATION_URL);
        } else {
            res.sendError(SC_NOT_FOUND);
        }
    }
}