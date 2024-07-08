package com.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.validators.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContactForm {

    @NotBlank(message = "Name is required")
    @Size(min=3,max=35,message="minimum 3 and maximum 35 characters are required.")
    private String name;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    //@Pattern()
    private String email;

    @NotBlank(message="Phone number is required")
    @Pattern(regexp="^[0-9]{10}$", message="Invalid phone number")
    private String phoneNumber;

    @NotBlank(message="Address is required")
    private String  address;

    private String description;
    private boolean favorite;
    private String websiteLink;
    private String linkedInLink;
    @ValidFile
    private MultipartFile contactImage;
    private String picture;
}
