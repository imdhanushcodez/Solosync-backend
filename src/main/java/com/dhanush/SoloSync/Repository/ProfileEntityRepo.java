package com.dhanush.SoloSync.Repository;

import com.dhanush.SoloSync.Model.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileEntityRepo extends JpaRepository<ProfileEntity,Long> {

    //Select * from tbl_profiles where email = ?
    Optional<ProfileEntity> findByEmail(String email);

    //Select * from tbl_profiles where activation_token = ?
    Optional<ProfileEntity> findByActivationToken(String activationToken);
}
