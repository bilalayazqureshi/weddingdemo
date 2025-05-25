package com.example.demo.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.WeddingEvent;

public interface WeddingEventRepository extends JpaRepository<WeddingEvent, Long>{

	WeddingEvent findByName(String string);

	List<WeddingEvent> findByNameAndLocation(String string, String string2);

	List<WeddingEvent> findByNameOrLocation(String string, String string2);

	List<WeddingEvent> findAllByDateBefore(LocalDate of);

}
