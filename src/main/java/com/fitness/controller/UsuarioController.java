package com.fitness.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.fitness.entity.Usuario;
import com.fitness.repository.UsuarioRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/validarUsuario")
    public String validarUsuario(@RequestParam("correo") String correo,
                                 @RequestParam("password") String password,
                                 Model model,
                                 HttpServletResponse response) {
    	
        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if (usuario == null) {
            model.addAttribute("error", "El correo no está registrado.");
            return "index"; 
        }

        if (!usuario.getPassword().equals(password)) {
            model.addAttribute("error", "La contraseña es incorrecta.");
            return "index";
        }
        
        Cookie cookie = new Cookie("usuarioCorreo", correo);
        cookie.setMaxAge(7 * 60 * 60 * 24);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/dashboard";
    }
}
