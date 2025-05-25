package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long>{

	Guest findByName(String string);

	List<Guest> findByNameAndEmail(String string, String string2);

	List<Guest> findByNameOrEmail(String string, String string2);

	List<Guest> findByEmailEndingWith(String string);


}
