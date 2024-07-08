package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

public class UserForm {

    @NotBlank(message="Name field  should not blank.")
    @Size(min=3,max=35,message="minimum 3 and maximum 35 characters are required.")
    private String name;

    @Email(message="Invalid Email address")
    @NotBlank(message="Email is required")
    //@Pattern("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min=8, message ="minimun 8 charactes are required")
    private String password;

    @Size(min=10, max=10, message="Please enter a valid phone number")
    private String phoneNumber;

    @NotBlank(message="About is required")
    private String about;
}
