package com.codingdojo.benjamin.servicios;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.codingdojo.benjamin.modelos.Event;
import com.codingdojo.benjamin.modelos.Message;
import com.codingdojo.benjamin.modelos.User;
import com.codingdojo.benjamin.repositorios.RepositorioEventos;
import com.codingdojo.benjamin.repositorios.RepositorioMensajes;
import com.codingdojo.benjamin.repositorios.RepositorioUsuarios;

@Service
public class AppService {

	@Autowired
	private RepositorioUsuarios repositorio_usuarios;
	
	@Autowired
	private RepositorioEventos repositorio_eventos;
	
	@Autowired
	private RepositorioMensajes repositorio_mensajes;
	
	
public User register(User nuevoUsuario, BindingResult result) {
		
		String nuevoEmail = nuevoUsuario.getEmail(); //Obtenemos el correo
		User existeUsuario = repositorio_usuarios.findByEmail(nuevoEmail); //NULL o Objeto User
		
		//Verificando que el correo no exista
		if(existeUsuario != null) {
			result.rejectValue("email", "Unique", "El correo ya está registrado en nuestra BD");
		}
		
		//Comparando las contraseñas
		String contra = nuevoUsuario.getPassword();
		String confirmacion = nuevoUsuario.getConfirm();
		if(! contra.equals(confirmacion)) {
			result.rejectValue("confirm", "Matches", "Las contraseñas no coinciden");
		}
		
		if(!result.hasErrors()) {
			//Encriptamos contraseña
			String contra_encr = BCrypt.hashpw(nuevoUsuario.getPassword(), BCrypt.gensalt());
			nuevoUsuario.setPassword(contra_encr);
			//Guardo usuario
			return repositorio_usuarios.save(nuevoUsuario);
		}else {
			return null;
		}
		
		
	}


	public User login(String email, String password) {
		
		//Buscamos que el correo esté en la BD
		User existeUsuario = repositorio_usuarios.findByEmail(email); //NULL o Objeto Usuario
		if(existeUsuario == null) {
			return null;
		}
		
		//Comparamos contraseñas encriptadas
		if(BCrypt.checkpw(password, existeUsuario.getPassword())) {
			return existeUsuario;
		} else {
			return null;
		}
		
	}
	
	public User find_user(Long id) {
		return repositorio_usuarios.findById(id).orElse(null);
	}
	public Event find_event(Long id) {
		return repositorio_eventos.findById(id).orElse(null);
	}
	
	public Event save_event(Event Newvent) {
		return repositorio_eventos.save(Newvent);
	}
	
	// regresa en mi estado
	public List<Event> eventos_estado(String estado){
		return repositorio_eventos.findByState(estado);
	}
	
	// regresa los que esta fuera del estado
	public List<Event> eventos_fuera(String estado){
		return repositorio_eventos.findByStateIsNot(estado);
	}
	// unir persona a evento
	
	public void join_event(Long user_id, Long event_id) {
		User myUsuario = find_user(user_id);
		Event evento = find_event(event_id);
		
		myUsuario.getEventsAttending().add(evento);
		repositorio_usuarios.save(myUsuario);
	}
	// quitar evento a asistente
	public void remove_event(Long user_id, Long event_id) {
		
		User myUsuario = find_user(user_id);
		Event evento = find_event(event_id);
		
		myUsuario.getEventsAttending().remove(evento);
		repositorio_usuarios.save(myUsuario);
	}

	public Message save_mensaje(Message nuevoMensaje) {
		return repositorio_mensajes.save(nuevoMensaje);
	}
	public void delete_event(Long event_id) {
		repositorio_eventos.deleteById(event_id);
	}


	
}
