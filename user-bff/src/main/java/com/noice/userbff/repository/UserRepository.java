package com.noice.userbff.repository;

import com.noice.userbff.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where upper(u.email) = upper(?1) or u.phoneNumber = ?1")
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,attributePaths = {"permissions","role"})
    Optional<User> findByEmailIgnoreCaseOrPhoneNumber(@NonNull String search);
}
