package course.work.plants.service;

import course.work.plants.model.TokenDTO;
import course.work.plants.model.UserDTO;

public interface SecurityService {

    TokenDTO signUp(UserDTO userDTO);
    TokenDTO signIn(UserDTO userDTO);
}
