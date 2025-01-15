package kz.muradaliev.charm.back.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


public class RegistrationDto {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
