package pe.edu.vallegrande.vgmsuser.infraestructure.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebController {

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam(name = "token", required = false) String token, Model model) {
        log.info("Serving reset password page for token: {}", token != null ? token.substring(0, 8) + "..." : "null");
        
        if (token != null) {
            model.addAttribute("token", token);
        }
        
        return "reset-password-form"; // Nombre del template Thymeleaf
    }
}
