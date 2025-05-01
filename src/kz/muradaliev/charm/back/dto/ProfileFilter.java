package kz.muradaliev.charm.back.dto;

import kz.muradaliev.charm.back.model.Status;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import static kz.muradaliev.charm.back.utils.ConnectionManager.*;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileFilter {
    String emailStartWith;
    String nameStartWith;
    String surnameStartWith;
    Integer ltAge;
    Integer gteAge;
    Status status;
    String sort = DEFAULT_SORTED_COLUMN;
    Integer page = DEFAULT_PAGE;
    Integer pageSize = DEFAULT_PAGE_SIZE;

    public String getEmailStartWith() {
        return emailStartWith;
    }

    public void setEmailStartWith(String emailStartWith) {
        this.emailStartWith = emailStartWith;
    }

    public String getNameStartWith() {
        return nameStartWith;
    }

    public void setNameStartWith(String nameStartWith) {
        this.nameStartWith = nameStartWith;
    }

    public String getSurnameStartWith() {
        return surnameStartWith;
    }

    public void setSurnameStartWith(String surnameStartWith) {
        this.surnameStartWith = surnameStartWith;
    }

    public Integer getLtAge() {
        return ltAge;
    }

    public void setLtAge(Integer ltAge) {
        this.ltAge = ltAge;
    }

    public Integer getGteAge() {
        return gteAge;
    }

    public void setGteAge(Integer gteAge) {
        this.gteAge = gteAge;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
