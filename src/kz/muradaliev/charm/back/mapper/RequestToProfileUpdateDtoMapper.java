package kz.muradaliev.charm.back.mapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import kz.muradaliev.charm.back.dto.ProfileUpdateDto;
import kz.muradaliev.charm.back.model.Gender;
import kz.muradaliev.charm.back.model.Status;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.time.LocalDate;

import static kz.muradaliev.charm.back.utils.StringUtils.isBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestToProfileUpdateDtoMapper implements Mapper<HttpServletRequest, ProfileUpdateDto> {

    private static final RequestToProfileUpdateDtoMapper INSTANCE = new RequestToProfileUpdateDtoMapper();

    public static RequestToProfileUpdateDtoMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public ProfileUpdateDto map(HttpServletRequest req) {
        return map(req, new ProfileUpdateDto());
    }

    @Override
    @SneakyThrows
    public ProfileUpdateDto map(HttpServletRequest req, ProfileUpdateDto dto) {
        String id = req.getParameter("id");
        if (!isBlank(id)) {
            dto.setId(Long.parseLong(id));
        }
        String email = req.getParameter("email");
        if (!isBlank(email)) {
            dto.setEmail(email);
        }
        dto.setName(req.getParameter("name"));
        dto.setSurname(req.getParameter("surname"));
        String birthDate = req.getParameter("birthDate");
        if (!isBlank(birthDate)) {
            dto.setBirthDate(LocalDate.parse(birthDate));
        }
        dto.setAbout(req.getParameter("about"));
        String gender = req.getParameter("gender");
        if (!isBlank(gender)) {
            dto.setGender(Gender.valueOf(gender));
        }
        String status = req.getParameter("status");
        if (!isBlank(status)) {
            dto.setStatus(Status.valueOf(status));
        }
        try {
            dto.setPhoto(req.getPart("photo"));
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }

        return dto;
    }
}