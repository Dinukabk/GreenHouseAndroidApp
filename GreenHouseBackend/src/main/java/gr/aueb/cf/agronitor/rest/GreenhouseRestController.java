package gr.aueb.cf.agronitor.rest;

import gr.aueb.cf.agronitor.dto.GreenhouseDTO;
import gr.aueb.cf.agronitor.dto.GreenhouseNoUserDTO;
import gr.aueb.cf.agronitor.model.Greenhouse;
import gr.aueb.cf.agronitor.model.User;
import gr.aueb.cf.agronitor.service.IGreenhouseService;
import gr.aueb.cf.agronitor.service.IUserService;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.agronitor.service.util.LoggerUtil;
import gr.aueb.cf.agronitor.validator.GreenhouseValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class GreenhouseRestController {

    private final IGreenhouseService greenhouseService;
    private final IUserService userService;
    private final GreenhouseValidator greenhouseValidator;
    private final MessageSource messageSource;
    private MessageSourceAccessor accessor;

    @Autowired
    public GreenhouseRestController(IGreenhouseService greenhouseService, IUserService userService,
                                    GreenhouseValidator greenhouseValidator,
                                    MessageSource messageSource) {
        this.greenhouseService = greenhouseService;
        this.userService = userService;
        this.greenhouseValidator = greenhouseValidator;
        this.messageSource = messageSource;
    }

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    @Operation(summary = "Get greenhouse by name or starting with...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Greenhouses Found",
                            content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GreenhouseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "No greenhouses found",
                           content = @Content)})
    @RequestMapping(path = "/greenhouses", method = RequestMethod.GET)
    public ResponseEntity<List<GreenhouseDTO>> getGreenhouseByName(@RequestParam("greenhouseName")
                                                                                            String greenhouseName) {
        List<Greenhouse> greenhouses;
        try {
            greenhouses = greenhouseService.getGreenhouseByName(greenhouseName);
            List<GreenhouseDTO> greenhousesDTO = new ArrayList<>();
            for (Greenhouse greenhouse : greenhouses) {
                greenhousesDTO.add(new GreenhouseDTO(greenhouse.getId(),
                                                     greenhouse.getGreenhouseName(),
                                                     greenhouse.getUser().getId()));
            }
            return new ResponseEntity<>(greenhousesDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get a Greenhouse by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Greenhouse found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GreenhouseDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Greenhouse not found",
                    content = @Content)})
    @RequestMapping(value = "/greenhouses/{greenhouseId}", method = RequestMethod.GET)
    public ResponseEntity<GreenhouseDTO> getGreenhouseById(@PathVariable("greenhouseId") Long greenhouseId) {
        Greenhouse greenhouse;
        try {
            greenhouse = greenhouseService.getGreenhouseById(greenhouseId);
            GreenhouseDTO greenhouseDTO = new GreenhouseDTO(greenhouse.getId(),
                                                            greenhouse.getGreenhouseName(),
                                                            greenhouse.getUser().getId());
            return new ResponseEntity<>(greenhouseDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get greenhouses by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Greenhouses  found",
                    content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = GreenhouseDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No greenhouses belong to this user",
                    content = @Content)})
    @RequestMapping(path = "/greenhouses/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<GreenhouseNoUserDTO>> findGreenhousesByUserId(@PathVariable("userId") Long userId) {
        List<Greenhouse> greenhouses;
        try {
            greenhouses = greenhouseService.getGreenhouseByUserId(userId);
            List<GreenhouseNoUserDTO> greenhouseDTOs = new ArrayList<>();
            for (Greenhouse greenhouse : greenhouses) {
                greenhouseDTOs.add(new GreenhouseNoUserDTO(greenhouse.getId(),
                                                     greenhouse.getGreenhouseName(),
                                                     greenhouse.getUser().getId()));
            }
            return new ResponseEntity<>(greenhouseDTOs, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Add a greenhouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Greenhouse created successfully",
                            content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GreenhouseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                            content = @Content)})
    @RequestMapping(value = "/greenhouses/user/{userId}", method = RequestMethod.POST)
    public ResponseEntity<GreenhouseNoUserDTO> addGreenhouse(@PathVariable("userId") Long userId,
                                                       @RequestBody GreenhouseDTO dto, BindingResult bindingResult) {
        greenhouseValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            LoggerUtil.getCurrentLogger().warning(accessor.getMessage("invalid input"));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            User user = userService.getUserById(userId);
            Greenhouse greenhouse = new Greenhouse();
            greenhouse.setGreenhouseName(dto.getGreenhouseName());
            greenhouse.setUser(user);
            user.addGreenhouse(greenhouse);
            Greenhouse savedGreenhouse = greenhouseService.insertGreenhouse(greenhouse);
            GreenhouseNoUserDTO greenhouseNoUserDTO = mapNoUser(savedGreenhouse);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("{id}").buildAndExpand(greenhouseNoUserDTO.getId()).toUri();
            return ResponseEntity.created(location).body(greenhouseNoUserDTO);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Delete a greenhouse by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Greenhouse deleted successfully",
                            content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GreenhouseDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Greenhouse not found",
                            content = @Content)})
    @RequestMapping(value = "/greenhouses/{greenhouseId}", method = RequestMethod.DELETE)
    public ResponseEntity<GreenhouseDTO> deleteGreenhouse(@PathVariable("greenhouseId") Long greenhouseId) {
        try {
            Greenhouse greenhouse = greenhouseService.getGreenhouseById(greenhouseId);
            greenhouseService.deleteGreenhouse(greenhouseId);
            GreenhouseDTO greenhouseDTO = map(greenhouse);
            return new ResponseEntity<>(greenhouseDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Update a greenhouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Greenhouse updated successfully",
                            content = { @Content(mediaType = "application/json",
                                schema = @Schema(implementation = GreenhouseDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                            content = @Content),
            @ApiResponse(responseCode = "404", description = "Greenhouse not found",
                            content = @Content) })
    @RequestMapping(value = "/greenhouses/{greenhouseId}", method = RequestMethod.PUT)
    public ResponseEntity<GreenhouseDTO> updateGreenhouse(@PathVariable("greenhouseId") Long greenhouseId,
                                                          @RequestParam String greenhouseNewName) {
        if (greenhouseNewName.equals("")) {
            LoggerUtil.getCurrentLogger().warning(accessor.getMessage("empty"));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Greenhouse greenhouse = greenhouseService.updateGreenhouse(greenhouseId, greenhouseNewName);
            GreenhouseDTO greenhouseDTO = map(greenhouse);
            return new ResponseEntity<>(greenhouseDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


//    Private methods
    private GreenhouseDTO map(Greenhouse greenhouse) {
        GreenhouseDTO greenhouseDTO = new GreenhouseDTO();
        greenhouseDTO.setId(greenhouse.getId());
        greenhouseDTO.setGreenhouseName(greenhouse.getGreenhouseName());
        greenhouseDTO.setUserId(greenhouse.getUser().getId());
        return greenhouseDTO;
    }

    private GreenhouseNoUserDTO mapNoUser(Greenhouse greenhouse) {
        GreenhouseNoUserDTO greenhouseNoUserDTO = new GreenhouseNoUserDTO();
        greenhouseNoUserDTO.setId(greenhouse.getId());
        greenhouseNoUserDTO.setGreenhouseName(greenhouse.getGreenhouseName());
        greenhouseNoUserDTO.setUserId(greenhouse.getUser().getId());
        return greenhouseNoUserDTO;
    }
}
