package com.codingdojo.benjamin.repositorios;

import org.springframework.data.repository.CrudRepository;

import com.codingdojo.benjamin.modelos.User;

public interface RepositorioUsuarios extends CrudRepository<User, Long> {

	User findByEmail(String email);
	
}
