package gr.aueb.cf.agronitor.rest;

import gr.aueb.cf.agronitor.dto.TemperatureDTO;
import gr.aueb.cf.agronitor.model.Greenhouse;
import gr.aueb.cf.agronitor.model.Temperature;
import gr.aueb.cf.agronitor.service.IGreenhouseService;
import gr.aueb.cf.agronitor.service.ITemperatureService;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.agronitor.service.util.LoggerUtil;
import gr.aueb.cf.agronitor.validator.TemperatureValidator;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/greenhouses/measurements")
public class TemperatureRestController {

    private final ITemperatureService temperatureService;
    private final TemperatureValidator temperatureValidator;
    private final IGreenhouseService greenhouseService;
    private final MessageSource messageSource;
    private MessageSourceAccessor accessor;

    @Autowired
    public TemperatureRestController(ITemperatureService temperatureService, TemperatureValidator temperatureValidator,
                                     IGreenhouseService greenhouseService, MessageSource messageSource) {
        this.temperatureService = temperatureService;
        this.temperatureValidator = temperatureValidator;
        this.greenhouseService = greenhouseService;
        this.messageSource = messageSource;
    }

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    @Operation(summary =  "Get temperature by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Temperature reading found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TemperatureDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Temperature reading not found",
                    content = @Content)})
    @RequestMapping(value = "/temp/{tempId}", method = RequestMethod.GET)
    public ResponseEntity<TemperatureDTO> getTempById(@PathVariable("tempId") Long tempId) {
        Temperature temperature;
        try {
            temperature = temperatureService.getTemperatureById(tempId);
            TemperatureDTO temperatureDTO = new TemperatureDTO(temperature.getId(),
                                                               temperature.getTimestamp(),
                                                               temperature.getValue(),
                                                               temperature.getGreenhouse().getId());
            return new ResponseEntity<>(temperatureDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @Operation(summary = "Insert a new temperature reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Temperature reading successfully added",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = TemperatureDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
            content = @Content)})
    @RequestMapping(value = "/temp/greenhouse/{greenhouseId}", method = RequestMethod.POST)
    public ResponseEntity<TemperatureDTO> addTemperature(@PathVariable("greenhouseId") Long greenhouseId,
                                                         @RequestBody TemperatureDTO dto, BindingResult bindingResult) {
        temperatureValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            LoggerUtil.getCurrentLogger().warning(accessor.getMessage("invalid input"));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Greenhouse greenhouse = greenhouseService.getGreenhouseById(greenhouseId);
            Temperature temperature = new Temperature(dto.getTimestamp(), dto.getValue(), greenhouseId);
            temperature.setGreenhouse(greenhouse);
            greenhouse.addTemperature(temperature);
            Temperature savedTemperature = temperatureService.insertTemperature(temperature);
            TemperatureDTO temperatureDTO = map(savedTemperature);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}")
                    .buildAndExpand(temperatureDTO.getId()).toUri();
            return ResponseEntity.created(location).body(temperatureDTO);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Get greenhouse max temperature reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Temperature reading found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TemperatureDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Temperature reading not found",
                    content = @Content)})
    @RequestMapping(value = "/temp/max/greenhouse/{greenhouseId}", method = RequestMethod.GET)
    public ResponseEntity<TemperatureDTO> getMaxTemperature(@PathVariable("greenhouseId") Long greenhouseId) {
        try {
            Temperature temperature = temperatureService.getMaxTemperature(greenhouseId);
            TemperatureDTO temperatureDTO = new TemperatureDTO(temperature.getId(),
                                                               temperature.getTimestamp(),
                                                               temperature.getValue(),
                                                               temperature.getGreenhouse().getId());
            return new ResponseEntity<>(temperatureDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get greenhouse min temperature reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Temperature reading found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TemperatureDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Temperature reading not found",
                    content = @Content)})
    @RequestMapping(value = "/temp/min/greenhouse/{greenhouseId}", method = RequestMethod.GET)
    public ResponseEntity<TemperatureDTO> getMinTemperature(@PathVariable("greenhouseId") Long greenhouseId) {
        try {
            Temperature temperature = temperatureService.getMinTemperature(greenhouseId);
            TemperatureDTO temperatureDTO = new TemperatureDTO(temperature.getId(),
                                                               temperature.getTimestamp(),
                                                               temperature.getValue(),
                                                               temperature.getGreenhouse().getId());
            return new ResponseEntity<>(temperatureDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get greenhouse last temperature reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Temperature reading found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TemperatureDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Temperature reading not found",
                    content = @Content)})
    @RequestMapping(value = "/temp/last/greenhouse/{greenhouseId}", method = RequestMethod.GET)
    public ResponseEntity<TemperatureDTO> getGreenhouseLastTemp(@PathVariable("greenhouseId") Long greenhouseId) {
        try {
            List<Temperature> temperatureList = temperatureService.getGreenhouseLastTemp(greenhouseId);
            Temperature temperature = temperatureList.get(0);
            TemperatureDTO temperatureDTO = new TemperatureDTO(temperature.getId(),
                                                               temperature.getTimestamp(),
                                                               temperature.getValue(),
                                                               temperature.getGreenhouse().getId());
            return new ResponseEntity<>(temperatureDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


//    Private methods
    private TemperatureDTO map(Temperature temperature) {
        TemperatureDTO temperatureDTO = new TemperatureDTO();
        temperatureDTO.setId(temperature.getId());
        temperatureDTO.setTimestamp(temperature.getTimestamp());
        temperatureDTO.setValue(temperature.getValue());
        temperatureDTO.setGreenhouseId(temperature.getGreenhouse().getId());
        return temperatureDTO;
    }
}
