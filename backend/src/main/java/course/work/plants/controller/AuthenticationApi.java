package course.work.plants.controller;

import course.work.plants.api.AuthApi;
import course.work.plants.model.TokenDTO;
import course.work.plants.model.UserDTO;
import course.work.plants.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthenticationApi implements AuthApi {

    private final SecurityService securityService;

    @Override
    public ResponseEntity<TokenDTO> signIn(UserDTO userDTO) {
        return ResponseEntity.ok(securityService.signIn(userDTO));
    }

    @Override
    public ResponseEntity<TokenDTO> signUp(UserDTO userDTO) {
        return ResponseEntity.ok(securityService.signUp(userDTO));
    }
}
