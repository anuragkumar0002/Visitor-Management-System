package io.bootify.visitor_managment_system.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VisitorDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @VisitorEmailUnique
    private String email;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    @VisitorPhoneUnique
    private String phone;

    @NotNull
    @Size(max = 255)
    @VisitorIdNumberUnique
    private String idNumber;

    private AddressDTO address;

}
