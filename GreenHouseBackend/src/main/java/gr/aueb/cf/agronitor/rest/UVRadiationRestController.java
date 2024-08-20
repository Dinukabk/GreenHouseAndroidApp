package gr.aueb.cf.agronitor.rest;

import gr.aueb.cf.agronitor.dto.UVRadiationDTO;
import gr.aueb.cf.agronitor.model.Greenhouse;
import gr.aueb.cf.agronitor.model.UVRadiation;
import gr.aueb.cf.agronitor.service.IGreenhouseService;
import gr.aueb.cf.agronitor.service.IUVRadiationService;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.agronitor.service.util.LoggerUtil;
import gr.aueb.cf.agronitor.validator.UVRadiationValidator;
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
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("api/greenhouses/measurements")
public class UVRadiationRestController {

    private final IUVRadiationService uvRadiationService;
    private final UVRadiationValidator uvRadiationValidator;
    private final IGreenhouseService greenhouseService;
    private final MessageSource messageSource;
    private MessageSourceAccessor accessor;

    @Autowired
    public UVRadiationRestController(IUVRadiationService uvRadiationService, UVRadiationValidator uvRadiationValidator,
                                     IGreenhouseService greenhouseService, MessageSource messageSource) {
        this.uvRadiationService = uvRadiationService;
        this.uvRadiationValidator = uvRadiationValidator;
        this.greenhouseService = greenhouseService;
        this.messageSource = messageSource;
    }

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    @Operation(summary =  "Get uv radiation by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UV radiation reading found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UVRadiationDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "UV radiation reading not found",
                    content = @Content)})
    @RequestMapping(value = "/rad/{radId}", method = RequestMethod.GET)
    public ResponseEntity<UVRadiationDTO> getTempById(@PathVariable("radId") Long radId) {
        UVRadiation uvRadiation;
        try {
            uvRadiation = uvRadiationService.getUVRadiationById(radId);
            UVRadiationDTO uvRadiationDTO = new UVRadiationDTO(uvRadiation.getId(),
                                                               uvRadiation.getTimestamp(),
                                                               uvRadiation.getValue(),
                                                               uvRadiation.getGreenhouse().getId());
            return new ResponseEntity<>(uvRadiationDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Insert a new uv radiation reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "UV radiation reading successfully added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UVRadiationDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content)})
    @RequestMapping(value = "/rad/greenhouse/{greenhouseId}", method = RequestMethod.POST)
    public ResponseEntity<UVRadiationDTO> addUVRadiation(@PathVariable("greenhouseId") Long greenhouseId,
                                                         @RequestBody UVRadiationDTO dto, BindingResult bindingResult) {
        uvRadiationValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            LoggerUtil.getCurrentLogger().warning(accessor.getMessage("invalid input"));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Greenhouse greenhouse = greenhouseService.getGreenhouseById(greenhouseId);
            UVRadiation uvRadiation = new UVRadiation(dto.getTimestamp(), dto.getValue(), greenhouseId);
            uvRadiation.setGreenhouse(greenhouse);
            greenhouse.addUVRadiation(uvRadiation);
            UVRadiation savedUVRadiation = uvRadiationService.insertUVRadiation(uvRadiation);
            UVRadiationDTO uvRadiationDTO = map(savedUVRadiation);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}")
                    .buildAndExpand(uvRadiationDTO.getId()).toUri();
            return ResponseEntity.created(location).body(uvRadiationDTO);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get greenhouse max uv radiation reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UV radiation reading found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UVRadiationDTO.class))}),
            @ApiResponse(responseCode = "404", description = "UV radiation reading not found",
                    content = @Content)})
    @RequestMapping(value = "/rad/max/greenhouse/{greenhouseId}", method = RequestMethod.GET)
    public ResponseEntity<UVRadiationDTO> getMaxUVRadiation(@PathVariable("greenhouseId") Long greenhouseId) {
        try {
            UVRadiation uvRadiation = uvRadiationService.getMaxUVRadiation(greenhouseId);
            UVRadiationDTO uvRadiationDTO = new UVRadiationDTO(uvRadiation.getId(),
                                                               uvRadiation.getTimestamp(),
                                                               uvRadiation.getValue(),
                                                               uvRadiation.getGreenhouse().getId());
            return new ResponseEntity<>(uvRadiationDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get greenhouse min uv radiation reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UV radiation reading found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UVRadiationDTO.class))}),
            @ApiResponse(responseCode = "404", description = "UV radiation reading not found",
                    content = @Content)})
    @RequestMapping(value = "/rad/min/greenhouse/{greenhouseId}", method = RequestMethod.GET)
    public ResponseEntity<UVRadiationDTO> getMinUVRadiation(@PathVariable("greenhouseId") Long greenhouseId) {
        try {
            UVRadiation uvRadiation = uvRadiationService.getMinUVRadiation(greenhouseId);
            UVRadiationDTO uvRadiationDTO = new UVRadiationDTO(uvRadiation.getId(),
                                                               uvRadiation.getTimestamp(),
                                                               uvRadiation.getValue(),
                                                               uvRadiation.getGreenhouse().getId());
            return new ResponseEntity<>(uvRadiationDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get greenhouse last uv radiation reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UV radiation reading found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UVRadiationDTO.class))}),
            @ApiResponse(responseCode = "404", description = "UV radiation reading not found",
                    content = @Content)})
    @RequestMapping(value = "/rad/last/greenhouse/{greenhouseId}", method = RequestMethod.GET)
    public ResponseEntity<UVRadiationDTO> getGreenhouseLastTemp(@PathVariable("greenhouseId") Long greenhouseId) {
        try {
            List<UVRadiation> uvRadiationList = uvRadiationService.getGreenhouseLastUV(greenhouseId);
            UVRadiation uvRadiation = uvRadiationList.get(0);
            UVRadiationDTO uvRadiationDTO = new UVRadiationDTO(uvRadiation.getId(),
                                                               uvRadiation.getTimestamp(),
                                                               uvRadiation.getValue(),
                                                               uvRadiation.getGreenhouse().getId());
            return new ResponseEntity<>(uvRadiationDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    private methods
    private UVRadiationDTO map(UVRadiation uvRadiation) {
        UVRadiationDTO uvRadiationDTO = new UVRadiationDTO();
        uvRadiationDTO.setId(uvRadiation.getId());
        uvRadiationDTO.setTimestamp(uvRadiation.getTimestamp());
        uvRadiationDTO.setValue(uvRadiation.getValue());
        uvRadiationDTO.setGreenhouseId(uvRadiation.getGreenhouse().getId());
        return uvRadiationDTO;
    }
}
