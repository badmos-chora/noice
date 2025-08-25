package com.noice.userbff.repository;

import com.noice.userbff.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {
    <T> List<T> findAllBy(Class<T> type);
    <T> Optional<T> findById(Long id, Class<T> type);


}
