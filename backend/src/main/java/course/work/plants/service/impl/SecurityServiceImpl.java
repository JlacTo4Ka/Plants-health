package course.work.plants.service.impl;

import course.work.plants.exception.BaseException;
import course.work.plants.exception.ErrorCodeEnum;
import course.work.plants.model.TokenDTO;
import course.work.plants.model.UserDTO;
import course.work.plants.model.UserModel;
import course.work.plants.repository.UserRepository;
import course.work.plants.service.SecurityService;
import course.work.plants.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private static final String TOKEN_PREFIX = "Bearer ";


    @Override
    public TokenDTO signUp(UserDTO user) {
        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new BaseException(ErrorCodeEnum.VALIDATION_ERROR, "Пользователь уже зарегестрирован");
        }

        UserModel userModel = new UserModel();
        userModel.setLogin(user.getLogin());
        userModel.setPassword(passwordEncoder.encode(user.getPassword()));
        userModel.setCreatedAt(LocalDateTime.now());
        userRepository.save(userModel);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getLogin(),
                        user.getPassword()
                )
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return new TokenDTO(TOKEN_PREFIX + token);
    }

    @Override
    public TokenDTO signIn(UserDTO user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getLogin(),
                        user.getPassword()
                )
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return new TokenDTO(TOKEN_PREFIX + token);
    }
}
