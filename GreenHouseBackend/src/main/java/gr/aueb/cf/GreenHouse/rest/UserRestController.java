package gr.aueb.cf.agronitor.rest;

import gr.aueb.cf.agronitor.dto.LoggedInUserDTO;
import gr.aueb.cf.agronitor.dto.LoginDTO;
import gr.aueb.cf.agronitor.dto.UserDTO;
import gr.aueb.cf.agronitor.model.User;
import gr.aueb.cf.agronitor.service.IUserService;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.agronitor.service.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.agronitor.service.util.LoggerUtil;
import gr.aueb.cf.agronitor.validator.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final IUserService userService;
    private final UserValidator userValidator;
    private final MessageSource messageSource;
    private MessageSourceAccessor accessor;

    @Autowired
    public UserRestController(IUserService userService, UserValidator userValidator, MessageSource messageSource) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.messageSource = messageSource;
    }

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    @Operation(summary = "Get users by username or starting with...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "No users found matching criteria",
                    content = @Content) })
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> getUsersByUsername(@RequestParam("username") String username) {
        List<User> users;
        try {
            users = userService.getUsersByUsername(username);
            List<UserDTO> userDTOs = new ArrayList<>();
            for (User user : users) {
                userDTOs.add(new UserDTO(user.getId(),
                                         user.getUsername(),
                                         user.getEmail(),
                                         user.getPassword()));
            }
            return new ResponseEntity<>(userDTOs, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") Long userId) {
        User user;
        try {
            user = userService.getUserById(userId);
            UserDTO userDTO = new UserDTO(user.getId(),
                                          user.getUsername(),
                                          user.getEmail(),
                                          user.getPassword());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User inserted successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content)})
    @RequestMapping(value = "/users/register", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO dto, BindingResult bindingResult) {
        userValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            LoggerUtil.getCurrentLogger().warning(accessor.getMessage("empty"));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            User user = userService.insertUser(dto);
            UserDTO userDTO = map(user);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(userDTO.getId()).toUri();
            return ResponseEntity.created(location).body(userDTO);
        } catch (EntityAlreadyExistsException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Login user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content)})
    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public ResponseEntity<LoggedInUserDTO> loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            User user = userService.userIsValid(loginDTO.getUsername(), loginDTO.getPassword());
            LoggedInUserDTO loggedInUserDTO = mapLoggedInUserDTO(user);
            return new ResponseEntity<>(loggedInUserDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @Operation(summary = "Delete a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Deleted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<UserDTO> deleteUser(@PathVariable("userId") Long userId) {
        try {
            User user = userService.getUserById(userId);
            userService.deleteUser(userId);
            UserDTO userDTO = map(user);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content) })
    @RequestMapping(value = "/users/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<UserDTO> updateUser(@PathVariable("userId") Long userId,
                                              @RequestBody UserDTO dto,
                                              BindingResult bindingResult) {
        userValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            LoggerUtil.getCurrentLogger().warning(accessor.getMessage("Cannot be empty"));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            dto.setId(userId);
            User user = userService.updateUser(dto);
            UserDTO userDTO = map(user);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    Private methods
    private UserDTO map(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }

    private LoggedInUserDTO mapLoggedInUserDTO(User user) {
        LoggedInUserDTO loggedInUserDTO = new LoggedInUserDTO();
        loggedInUserDTO.setId(user.getId());
        loggedInUserDTO.setUsername(user.getUsername());
        return loggedInUserDTO;
    }
}
