package gr.aueb.cf.agronitor.service;

import gr.aueb.cf.agronitor.dto.UserDTO;
import gr.aueb.cf.agronitor.model.User;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.agronitor.service.exceptions.EntityAlreadyExistsException;

import java.util.List;

public interface IUserService {
    User insertUser(UserDTO userDTO) throws EntityAlreadyExistsException;
    User updateUser(UserDTO userDTO) throws EntityNotFoundException;
    void deleteUser(Long id) throws EntityNotFoundException;
    List<User> getUsersByUsername(String username) throws EntityNotFoundException;
    User getUserById(Long id) throws EntityNotFoundException;
    boolean usernameAlreadyExists(String username) throws EntityAlreadyExistsException;
    User userIsValid(String username, String password) throws EntityNotFoundException;
}
