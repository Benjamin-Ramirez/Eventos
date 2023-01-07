package com.codingdojo.benjamin.modelos;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message="El campo de nombre es obligatorio")
	@Size(min=2, max=30, message="Nombre debe tener entre 2 y 30 caracteres")
	private String name;
	
	@NotEmpty(message="El campo de email es obligatorio")
	@Email(message="Ingrese un correo electronico valido")
	private String email;
	
	
	@NotEmpty(message="El campo de password es obligatorio")
	@Size(min=6, max=128, message="Password debe tener entre 6 y 128 caracteres")
	private String password;
	
	@Transient // no me guarda en la base de dato pero si lo obtiene y lo valida
	@NotEmpty(message="El campo de confirmacion es obligatorio")
	@Size(min=6, max=128, message="Las Password no coinciden")
	private String confirm;
	
	@Column(updatable=false)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date created_at;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date updated_at;
	
	@NotEmpty(message="El campo de locacion es obligatorio")
	private String location;
	
	@NotEmpty(message="El campo de estado es obligatorio")
	private String state;
	

	@OneToMany(mappedBy="planner", fetch=FetchType.LAZY)
	private List<Event> eventsPlanned; // eventos que el usuario creo

	
	@OneToMany(mappedBy="author", fetch=FetchType.LAZY)
	private List<Message> messages;
	
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name="users_has_events",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="event_id")
			)
	private List<Event> eventsAttending;
	
	
	public User() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@PrePersist
	protected void onCreate(){
		this.created_at= new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updated_at = new Date();
	}

	public List<Event> getEventsPlanned() {
		return eventsPlanned;
	}

	public void setEventsPlanned(List<Event> eventsPlanned) {
		this.eventsPlanned = eventsPlanned;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<Event> getEventsAttending() {
		return eventsAttending;
	}

	public void setEventsAttending(List<Event> eventsAttending) {
		this.eventsAttending = eventsAttending;
	}
	
	
	
}
