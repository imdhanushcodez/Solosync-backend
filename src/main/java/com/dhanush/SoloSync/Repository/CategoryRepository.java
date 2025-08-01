package com.dhanush.SoloSync.Repository;

import com.dhanush.SoloSync.Model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {

    //Select * from tbl_categories where profile_id = ?1
    List<CategoryEntity> findByProfileId(Long profileId);

    //select * from tbl_categories where profile_id = ? and id = ?
    Optional<CategoryEntity> findByIdAndProfileId(Long id,Long profileId);

    //select * from tbl_categories where type = ? and profileId = ?
    List<CategoryEntity> findByTypeAndProfileId(String type,Long profileId);

    boolean existsByNameAndProfileId(String name,Long profileId);

}
