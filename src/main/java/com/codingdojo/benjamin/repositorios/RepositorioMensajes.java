package com.codingdojo.benjamin.repositorios;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.codingdojo.benjamin.modelos.Message;

@Repository
public interface RepositorioMensajes extends CrudRepository<Message, Long> {

	List<Message> findAll();
	
}
