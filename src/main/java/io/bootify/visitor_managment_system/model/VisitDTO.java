package io.bootify.visitor_managment_system.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VisitDTO {

    private Long id;


    private VisitStatus status;

    private LocalDateTime inTime;

    private LocalDateTime outTIme;

    @NotNull
    @Size(max = 255)
    private String purpose;

    @Size(max = 255)
    private String urlOfImage;

    @NotNull
    private Integer noOfPeople;

    @NotNull
    private Long visitor;

//    @VisitFlatUnique
    private String flatNumber;
//
//    @VisitUserUnique
    private String userName;

}
