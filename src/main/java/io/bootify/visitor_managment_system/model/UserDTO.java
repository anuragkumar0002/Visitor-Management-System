package io.bootify.visitor_managment_system.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.jackson.Jacksonized;


@Getter
@Setter
@Data
public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    @UserEmailUnique
    private String email;

    @NotNull
    @Size(max = 255)
    @UserPhoneUnique
    private String phone;

    @NotNull
    @Size(max = 255)
    private String role;

    @NotNull
    private UserStatus status;

    private String flatNumber;

    private AddressDTO address;

}
