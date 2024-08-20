package gr.aueb.cf.agronitor.service;

import gr.aueb.cf.agronitor.dto.LoggedInUserDTO;
import gr.aueb.cf.agronitor.dto.UserDTO;
import gr.aueb.cf.agronitor.model.User;
import gr.aueb.cf.agronitor.repository.UserRepository;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.agronitor.service.exceptions.EntityAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public User insertUser(UserDTO userToRegister) {
        return userRepository.save(convertToNewUser(userToRegister));
    }

    @Transactional
    @Override
    public User updateUser(UserDTO userDTO) throws EntityNotFoundException {
        User user = userRepository.findUserById(userDTO.getId());
        if (user == null) throw new EntityNotFoundException(User.class, userDTO.getId());
        return userRepository.save(convertToUpdateUser(userDTO));
    }

    @Transactional
    @Override
    public void deleteUser(Long id) throws EntityNotFoundException {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User getUserById(Long id) throws EntityNotFoundException {
        User user = userRepository.findUserById(id);
        if (user == null) throw new EntityNotFoundException(User.class, 0L);
        return user;
    }

    @Transactional
    @Override
    public List<User> getUsersByUsername(String username) throws EntityNotFoundException {
        List<User> users;
        users = userRepository.findUserByUsernameStartingWith(username);
        if (users.size() == 0) throw new EntityNotFoundException(User.class, 0L);
        return users;
    }

    @Transactional
    @Override
    public boolean usernameAlreadyExists(String username) throws EntityAlreadyExistsException {
        if (userRepository.usernameExists(username)) throw new EntityAlreadyExistsException(User.class, username);
        return false;
    }

    @Transactional
    @Override
    public User userIsValid(String username, String password) throws EntityNotFoundException{
        User user = userRepository.isUserValid(username, password);
        if (user == null) throw new EntityNotFoundException(User.class, 0L);
        return user;
    }

    //    Private Methods
    private User convertToNewUser(UserDTO dto) {
        return new User(dto.getUsername(), dto.getEmail(), dto.getPassword());
    }

    private User convertToUpdateUser(UserDTO dto) {
        return new User(dto.getId(), dto.getUsername(), dto.getEmail(), dto.getPassword()/*, dto.getGreenhouses()*/);
    }

    private LoggedInUserDTO convertToLoggedInUser(User user) {
        return new LoggedInUserDTO(user.getId(), user.getUsername());
    }
}
