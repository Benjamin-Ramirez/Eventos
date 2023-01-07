package com.codingdojo.benjamin.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.codingdojo.benjamin.modelos.Event;

@Repository
public interface RepositorioEventos extends CrudRepository<Event, Long> {

	
	List<Event> findByState(String state);
	
	List<Event> findByStateIsNot(String state);
	
}
