package com.goormthon.team15.user.repository;

import com.goormthon.team15.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByPhoneNumber(String phoneNumber);
    
    boolean existsByUsername(String username);
    
    boolean existsByPhoneNumber(String phoneNumber);
    
}
