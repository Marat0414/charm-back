package kz.muradaliev.charm.back.mapper;


import jakarta.servlet.http.HttpServletRequest;
import kz.muradaliev.charm.back.dto.ProfileFilter;
import kz.muradaliev.charm.back.model.Status;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

import static kz.muradaliev.charm.back.utils.StringUtils.isBlank;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestToProfileFilterMapper implements Mapper<HttpServletRequest, ProfileFilter> {

    private static final RequestToProfileFilterMapper INSTANCE = new RequestToProfileFilterMapper();

    public static RequestToProfileFilterMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public ProfileFilter map(HttpServletRequest req) {
        return map(req, new ProfileFilter());
    }

    @Override
    public ProfileFilter map(HttpServletRequest req, ProfileFilter filter) {
        String emailStartWithArg = req.getParameter("emailStartWith");
        String emailStartWith = isBlank(emailStartWithArg) ? null : emailStartWithArg;
        filter.setEmailStartWith(emailStartWith);

        String nameStartWithArg = req.getParameter("nameStartWith");
        String nameStartWith = isBlank(nameStartWithArg) ? null : nameStartWithArg;
        filter.setNameStartWith(nameStartWith);

        String surnameStartWithArg = req.getParameter("surnameStartWith");
        String surnameStartWith = isBlank(surnameStartWithArg) ? null : surnameStartWithArg;
        filter.setSurnameStartWith(surnameStartWith);

        String ltAgeArg = req.getParameter("ltAge");
        Integer ltAge = isBlank(ltAgeArg) ? null : Integer.parseInt(ltAgeArg);
        filter.setLtAge(ltAge);

        String gteAgeArg = req.getParameter("gteAge");
        Integer gteAge = isBlank(gteAgeArg) ? null : Integer.parseInt(gteAgeArg);
        filter.setGteAge(gteAge);

        String statusArg = req.getParameter("status");
        Status status = isBlank(statusArg) ? null : Status.valueOf(statusArg);
        filter.setStatus(status);

        String sortArg = req.getParameter("sort");
        List<String> profileSortableColumns =
                (List<String>) req.getServletContext().getAttribute("profileSortableColumns");
        if (profileSortableColumns.contains(sortArg)) {
            filter.setSort(sortArg);
        }

        return filter;
    }
}