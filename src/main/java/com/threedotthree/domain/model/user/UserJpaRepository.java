package com.threedotthree.domain.model.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Integer> {
    
    User save(User user);

    Optional<User> findByUserSeqId(int userSeqId);

    Optional<User> findByNameAndRegNo(String name, String regNo);

    Optional<User> findByUserId(String userId);

}
