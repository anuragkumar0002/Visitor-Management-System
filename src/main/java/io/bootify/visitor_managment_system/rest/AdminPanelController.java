package io.bootify.visitor_managment_system.rest;

import io.bootify.visitor_managment_system.model.UserDTO;
import io.bootify.visitor_managment_system.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/admin")
public class AdminPanelController {

        Logger LOGGER = LoggerFactory.getLogger(AdminPanelController.class);

        @Autowired
        private UserService userService;

        @PostMapping("/user")
        @ApiResponse(responseCode = "201")
        public ResponseEntity<Long> createUser(@RequestBody @Valid final UserDTO userDTO){
            final Long createId = userService.create(userDTO);
            return new ResponseEntity<>(createId, HttpStatus.CREATED);
        }

        @PutMapping("/markUserInactive/{userId}")
        public ResponseEntity<String> markUserInactive(@PathVariable Long userId){
                userService.markInactive(userId);
                return ResponseEntity.ok("User Marked Inactive");
        }

        @PutMapping("/markUserActive/{userId}")
        public ResponseEntity<String> markUserActive(@PathVariable Long userId){
                userService.markInactive(userId);
                return ResponseEntity.ok("User Marked Active");
        }

        @GetMapping("/allUsers")
        public ResponseEntity<List<UserDTO>> getAllUsers(){
                return ResponseEntity.ok(userService.findAll());
        }

        @PostMapping("/user-csv-upload")
        public ResponseEntity<String> uploadFileForUserCreation(@RequestParam("file") MultipartFile file){
                LOGGER.info("file available : {}", file.getName());

                return ResponseEntity.ok("Done");

        }



}
