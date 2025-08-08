package com.dhanush.SoloSync.Controller;

import com.dhanush.SoloSync.Dto.AuthDTO;
import com.dhanush.SoloSync.Dto.ProfileDto;
import com.dhanush.SoloSync.Service.ProfileService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDto> registerprofile(@RequestBody  ProfileDto profileDto)
    {
        ProfileDto registeredProfileDto = profileService.registerProfile(profileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredProfileDto);
    }

    @GetMapping("/activate")
    public ResponseEntity<?> activateAccount(@RequestParam(name = "token") String activationToken){
            boolean isActivated = profileService.activateProfile(activationToken);
            if(isActivated)
            {
                return ResponseEntity.status(HttpStatus.OK).body("Profile Activated successfully");
            }
            else
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation token not found or already Used");
            }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDTO authDTO){
        try{
            if(!profileService.isAccountActivate(authDTO.getEmail())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                        "message","Account is not active.Please activate your account first"
                ));
            }
            Map<String,Object> response = profileService.authenticateAndGenerateToken(authDTO);
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message",e.getMessage()
            ));
        }
    }


    @GetMapping("/getProfile")
    public ResponseEntity<ProfileDto> getCurrentProfile(){
        ProfileDto dto = profileService.getPublicProfile(null);
        return ResponseEntity.ok(dto);
    }
}
