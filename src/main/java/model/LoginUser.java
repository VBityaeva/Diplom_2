package model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
    private String email;
    private String password;
}