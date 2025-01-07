package kz.muradaliev.charm.back.dto;

import kz.muradaliev.charm.back.model.Gender;
import kz.muradaliev.charm.back.model.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileGetDto {
    private Long id;
    private String email;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private Integer age;
    private String about;
    private Gender gender;
    private Status status;

}