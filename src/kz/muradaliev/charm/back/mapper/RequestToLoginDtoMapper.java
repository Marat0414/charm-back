package kz.muradaliev.charm.back.mapper;


import jakarta.servlet.http.HttpServletRequest;
import kz.muradaliev.charm.back.dto.LoginDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestToLoginDtoMapper implements Mapper<HttpServletRequest, LoginDto> {

    private static final RequestToLoginDtoMapper INSTANCE = new RequestToLoginDtoMapper();

    public static RequestToLoginDtoMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public LoginDto map(HttpServletRequest req) {
        return map(req, new LoginDto());
    }

    @Override
    public LoginDto map(HttpServletRequest req, LoginDto dto) {
        dto.setEmail(req.getParameter("email"));
        dto.setPassword(req.getParameter("password"));
        return dto;
    }
}
