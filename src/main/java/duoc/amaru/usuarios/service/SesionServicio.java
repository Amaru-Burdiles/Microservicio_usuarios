package duoc.amaru.usuarios.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class SesionServicio {
    // Id de usuarios logeados
    private Set<Long> sesiones = new HashSet<>();

    public boolean isLoggedIn(Long id) {
        return sesiones.contains(id);
    }

    public boolean logIn(Long id) {
        return sesiones.add(id);
    }

    public boolean logOut(Long id) {
        return sesiones.remove(id);
    }
}
