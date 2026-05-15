package duoc.amaru.usuarios.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import duoc.amaru.usuarios.service.UsuarioServicio;

@RestController
@RequestMapping("api/v1/eco_market_spa")
public class UsuarioControlador {
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public String getPlaceholder() {
        return "Pagina principal";
    }
    
    @GetMapping("/login/{}")
}
