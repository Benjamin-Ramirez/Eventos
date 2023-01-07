package com.codingdojo.benjamin.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codingdojo.benjamin.modelos.Event;
import com.codingdojo.benjamin.modelos.Message;
import com.codingdojo.benjamin.modelos.State;
import com.codingdojo.benjamin.modelos.User;
import com.codingdojo.benjamin.servicios.AppService;

@Controller
@RequestMapping("/events")
public class ControladorEventos {

	@Autowired
	private AppService servicio;
	
	
	@PostMapping("/create")
	public String create(@Valid @ModelAttribute("event") Event event, 
						BindingResult result, HttpSession session, Model model) {
		
		//revisamos inicio de session
				User usuario_en_sesion = (User)session.getAttribute("user_session");
				
				if(usuario_en_sesion == null) {
					return "redirect:/";
				}
				// revisamos inicio de session
				if(result.hasErrors()) {
					
					model.addAttribute("states", State.States);
					
					// obtenemos usuarios en session
					User myUser = servicio.find_user(usuario_en_sesion.getId());
					model.addAttribute("user", myUser); // mandamos usuario
					
					String miEstado = usuario_en_sesion.getState();
					List<Event> eventos_miestado = servicio.eventos_estado(miEstado);
					List<Event> eventos_fueraestado = servicio.eventos_fuera(miEstado);
					
					model.addAttribute("eventos_miestado", eventos_miestado);
					model.addAttribute("eventos_fueraestado", eventos_fueraestado);
					
					
					return "dashboard.jsp";
				}else {
					servicio.save_event(event);
					return "redirect:/dashboard";
				}
	}
	
	@GetMapping("/join/{event_id}")
	public String join(@PathVariable("event_id") Long event_id, HttpSession session) {
		
		User usuario_en_sesion = (User)session.getAttribute("user_session");
		
		if(usuario_en_sesion == null) {
			return "redirect:/";
		}
		servicio.join_event(usuario_en_sesion.getId(), event_id);
		return "redirect:/dashboard";
		
	}
	
	@GetMapping("/remove/{event_id}")
	public String remove(@PathVariable("event_id") Long event_id, HttpSession session) {
		
		User usuario_en_sesion = (User)session.getAttribute("user_session");
		
		if(usuario_en_sesion == null) {
			return "redirect:/";
		}
		
		servicio.remove_event(usuario_en_sesion.getId(), event_id);
		return "redirect:/dashboard";
	}

	@GetMapping("/{event_id}")
	public String show_event(@PathVariable("event_id") Long event_id, HttpSession session, @ModelAttribute("message") Message message, Model model) {
		
		User usuario_en_sesion = (User)session.getAttribute("user_session");
		
		if(usuario_en_sesion == null) {
			return "redirect:/";
		}
		
		Event evento = servicio.find_event(event_id);
		model.addAttribute("evento", evento);
		return "show.jsp";		
	}
	
	@PostMapping("/message")
	public String message(@Valid @ModelAttribute("message") Message message, BindingResult result, HttpSession session, Model model) {
		
		
		User usuario_en_sesion = (User)session.getAttribute("user_session");
		
		if(usuario_en_sesion == null) {
			return "redirect:/";
		}
		if(result.hasErrors()) {
			model.addAttribute("evento", message.getEvent());
			return "show.jsp";
		}else {
			servicio.save_mensaje(message);
			return "redirect:/events/"+message.getEvent().getId();
		}
		
		
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long event_id, Model model, HttpSession session) {
		
		User usuario_en_sesion = (User)session.getAttribute("user_session");
		
		if(usuario_en_sesion == null) {
			return "redirect:/";
		}
		Event evento = servicio.find_event(event_id);
		
		if(evento == null || !evento.getPlanner().getId().equals(usuario_en_sesion.getId())) {
			return "redirect:/dashboard";
		}
		model.addAttribute("evento", evento);
		model.addAttribute("states", State.States);
		return "edit.jsp";
	}
	
	@PutMapping("/update")
	public String update(@Valid @ModelAttribute("evento") Event evento, BindingResult result, HttpSession session, Model model) {
		
		if(result.hasErrors()) {
			model.addAttribute("states", State.States);
			return "edit,jsp";
		}
		
		Event thisEvent = servicio.find_event(evento.getId());
		evento.setAttendees(thisEvent.getAttendees());
		servicio.save_event(evento);
		return "redirect:/dashboard";
	}
	
	@DeleteMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long event_id, HttpSession session) {
		
		
		User usuario_en_sesion = (User)session.getAttribute("user_session");
		
		if(usuario_en_sesion == null) {
			return "redirect:/";
		}
		Event evento = servicio.find_event(event_id);
		
		if(evento == null || !evento.getPlanner().getId().equals(usuario_en_sesion.getId())) {
			return "redirect:/dashboard";
		}
		
		
		servicio.delete_event(event_id);
		return "redirect:/dashboard";
	}
	
	
	
	
	
	
}
