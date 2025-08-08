package com.dhanush.SoloSync.Service;

import com.dhanush.SoloSync.Dto.AuthDTO;
import com.dhanush.SoloSync.Dto.ProfileDto;
import com.dhanush.SoloSync.Model.ProfileEntity;
import com.dhanush.SoloSync.Repository.ProfileEntityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

//changed in github but not in aws elastic bean
@Service
public class ProfileService {

    @Autowired
    ProfileEntityRepo profileEntityRepo;
    @Autowired
    EmailService emailService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;


    public ProfileDto registerProfile(ProfileDto profileDto){

        //Converting into Entity_MODEL
        ProfileEntity newEntity = toProfileEntity(profileDto);
        newEntity.setActivationToken(UUID.randomUUID().toString());
        //Encoding password with Bcry algo(rounds - 12)
        newEntity.setPassword(passwordEncoder.encode(newEntity.getPassword()));
        //SAVING THE ENTITY INTO DB
        newEntity = profileEntityRepo.save(newEntity);
        //Activation link creation
        String activationLink = "http://solosync.us-east-1.elasticbeanstalk.com/api/v1.0/activate?token="+newEntity.getActivationToken();
        String subject = "Active your SoloSync account";
        String body = "Check on the following link to activate your account: "+activationLink;
        //Sending Email
        emailService.sendEmail(newEntity.getEmail(),body,subject);
        return toProfileDto(newEntity);
    }

    public ProfileEntity toProfileEntity(ProfileDto profileDto){
        return ProfileEntity.builder()
                .id(profileDto.getId())
                .fullname(profileDto.getFullname())
                .email(profileDto.getEmail())
                .password(profileDto.getPassword())
                .profileImageUrl(profileDto.getProfileImageUrl())
                .createdAt(profileDto.getCreatedAt())
                .updatedAt(profileDto.getUpdatedAt())
                .build();
    }

    public ProfileDto toProfileDto(ProfileEntity entity){
        return ProfileDto.builder()
                .id(entity.getId())
                .fullname(entity.getFullname())
                .email(entity.getEmail())
                .profileImageUrl(entity.getProfileImageUrl())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public boolean activateProfile(String activationToken){
        return profileEntityRepo.findByActivationToken(activationToken)
                .map(profile -> {
                    profile.setIsActive(true);
                    profileEntityRepo.save(profile);
                    return true;
                }).orElse(false);
    }

    public boolean isAccountActivate(String email){
        return profileEntityRepo.findByEmail(email)
                .map(ProfileEntity::getIsActive)
                .orElse(false);
    }

    public ProfileEntity getCurrentProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return profileEntityRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("profile not found with email: "+email));
    }

    public ProfileDto getPublicProfile(String email){
        ProfileEntity currentUser = null;
        if(email == null){
            currentUser = getCurrentProfile();
        }
        else{
            currentUser = profileEntityRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("profile not found with email: "+email));
        }
        return toProfileDto(currentUser);
    }


    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(),authDTO.getPassword()));
            //Generate the JWT token
            String token = jwtService.GenerateToken(authDTO.getEmail());
            return Map.of(
                    "token",token,
                    "user",getPublicProfile(authDTO.getEmail())
            );
        }
        catch (Exception e){
            throw  new RuntimeException("Invalid email or password");
        }
    }
}
