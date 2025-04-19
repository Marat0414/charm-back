package kz.muradaliev.charm.back.dto;

import kz.muradaliev.charm.back.model.Status;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileFilter {
    String emailStartWith;
    String nameStartWith;
    String surnameStartWith;
    Integer ltAge;
    Integer gteAge;
    Status status;
    String sort;
}
