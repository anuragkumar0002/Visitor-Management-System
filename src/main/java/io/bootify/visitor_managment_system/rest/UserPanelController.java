package io.bootify.visitor_managment_system.rest;

import io.bootify.visitor_managment_system.domain.Visit;
import io.bootify.visitor_managment_system.model.VisitDTO;
import io.bootify.visitor_managment_system.service.VisitService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserPanelController {

    @Autowired
    private VisitService visitService;

    @PutMapping("/approveEntry/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> approveEntry(@PathVariable Long visitId){
        visitService.approve(visitId);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/rejectEntry/{visitId}")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<Void> rejectEntry(@PathVariable Long visitId){
        visitService.reject(visitId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("getAllPendingRequests")
    @ApiResponse(responseCode = "200")
     public ResponseEntity<List<VisitDTO>> getAllPendingRequests(){
        visitService.findAllWaitingRequests();
        return ResponseEntity.ok().build();
    }
}
