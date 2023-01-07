package com.codingdojo.benjamin.controladores;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codingdojo.benjamin.modelos.Event;
import com.codingdojo.benjamin.modelos.State;
import com.codingdojo.benjamin.modelos.User;
import com.codingdojo.benjamin.servicios.AppService;

@Controller
public class ControladorUsuario {

	@Autowired
	private AppService servicio;
	
	@GetMapping("/")
	public String index(@ModelAttribute("nuevoUsuario") User user, Model model) {
		
		model.addAttribute("states", State.States);
		return "index.jsp";
	}
	@GetMapping("/dashboard")
	public String dashboard(HttpSession session, @ModelAttribute("event") Event event, Model model) {
		//revisamos inicio de session
		User usuario_en_sesion = (User)session.getAttribute("user_session");
		
		if(usuario_en_sesion == null) {
			return "redirect:/";
		}
		// revisamos inicio de session
		
		// mandamos estados
		model.addAttribute("states", State.States);
		
		// obtenemos usuarios en session
		User myUser = servicio.find_user(usuario_en_sesion.getId());
		model.addAttribute("user", myUser); // mandamos usuario
		
		//Mandamos dos listas de eventos
		String miEstado = usuario_en_sesion.getState();
		List<Event> eventos_miestado = servicio.eventos_estado(miEstado);
		List<Event> eventos_fueraestado = servicio.eventos_fuera(miEstado);
		
		model.addAttribute("eventos_miestado", eventos_miestado);
		model.addAttribute("eventos_fueraestado", eventos_fueraestado);
		
		return "dashboard.jsp";
	}
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		
		session.removeAttribute("user_session");
		return "redirect:/";
		
	}
	
	
	
	@PostMapping("/register")
	public String create(@Valid @ModelAttribute("nuevoUsuario") User nuevoUsuario,
						 BindingResult result, HttpSession session, Model model) {
		
		servicio.register(nuevoUsuario, result);
		if(result.hasErrors()) {
			model.addAttribute("states", State.States);
			return "index.jsp";
		}else {
			session.setAttribute("user_session", nuevoUsuario);
			return "redirect:/dashboard";
		}
	}
	@PostMapping("/login")
	public String login (@RequestParam(value="email") String email, 
						@RequestParam(value="password") String password, 
						RedirectAttributes redirectAttributes,
						HttpSession session) {
		User usuario_login = servicio.login(email, password);
		
		if(usuario_login == null) {
			redirectAttributes.addFlashAttribute("error_login", "El correo/password son incorrectos");
				return "redirect:/";
			}else {
				session.setAttribute("user_session", usuario_login);
				return "redirect:/dashboard";
			}
		}
	
}
