package com.dhanush.SoloSync.Service;

import com.dhanush.SoloSync.Model.ProfileEntity;
import com.dhanush.SoloSync.Repository.ProfileEntityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    ProfileEntityRepo profileEntityRepo;

    //THIS METHOD WILL RESPONSIBLE FOR LOADING PROFILES FROM DB AND WHICH FURTHER USED IN AUTHENTICATION PROCESS
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        ProfileEntity entity = profileEntityRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: "+email));

        //CREATE USERDETAILS CLASS --> USERPRINCIPAL --> WILL USESFULL IN JWT
        return new UserPrincipal(entity);
    }
}
