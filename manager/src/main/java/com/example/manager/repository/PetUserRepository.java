package com.example.manager.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.manager.entities.PetUser;

@Repository
public interface PetUserRepository extends CrudRepository<PetUser, Integer>{
	Optional<PetUser> findByUsername(String username);
}
