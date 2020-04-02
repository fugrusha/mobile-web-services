package com.udemycourse.mobileappws.io.repository;

import com.udemycourse.mobileappws.io.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);
}
