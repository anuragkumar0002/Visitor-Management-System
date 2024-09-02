package io.bootify.visitor_managment_system.rest;

import io.bootify.visitor_managment_system.model.VisitDTO;
import io.bootify.visitor_managment_system.model.VisitorDTO;
import io.bootify.visitor_managment_system.service.VisitService;
import io.bootify.visitor_managment_system.service.VisitorService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gatekeeper")
public class GateKeeperPanelController {

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private VisitService visitService;


    @PostMapping("/createVisitor")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisitor(@RequestBody @Valid final VisitorDTO visitorDTO) {
        final Long createdId = visitorService.create(visitorDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PostMapping("/createVisit")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVisit(@RequestBody @Valid final VisitDTO visitDTO) throws BadRequestException {
        final Long createdId = visitService.create(visitDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/markEntry/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> markEntry(@PathVariable Long visitId){
        visitService.updateInTIme(visitId);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/markExit/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> makExit(@PathVariable Long visitId){
        visitService.updateOutTime(visitId);
        return ResponseEntity.ok().build();

    }
}
