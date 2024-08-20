package gr.aueb.cf.agronitor.rest;

import gr.aueb.cf.agronitor.dto.HumidityDTO;
import gr.aueb.cf.agronitor.model.Greenhouse;
import gr.aueb.cf.agronitor.model.Humidity;
import gr.aueb.cf.agronitor.service.IGreenhouseService;
import gr.aueb.cf.agronitor.service.IHumidityService;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.agronitor.service.util.LoggerUtil;
import gr.aueb.cf.agronitor.validator.HumidityValidator;
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
public class HumidityRestController {

    private final IHumidityService humidityService;
    private final HumidityValidator humidityValidator;
    private final IGreenhouseService greenhouseService;
    private final MessageSource messageSource;
    private MessageSourceAccessor accessor;

    @Autowired
    public HumidityRestController(IHumidityService humidityService, HumidityValidator humidityValidator,
                                  IGreenhouseService greenhouseService, MessageSource messageSource) {
        this.humidityService = humidityService;
        this.humidityValidator = humidityValidator;
        this.greenhouseService = greenhouseService;
        this.messageSource = messageSource;
    }

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    @Operation(summary = "Get humidity by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Humidity reading found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HumidityDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Humidity reading not found",
                    content = @Content)})
    @RequestMapping(value = "/hum/{humId}", method = RequestMethod.GET)
    public ResponseEntity<HumidityDTO> getHumidityById(@PathVariable("humId") Long humId) {
        Humidity humidity;
        try {
            humidity = humidityService.getHumidityById(humId);
            HumidityDTO humidityDTO = new HumidityDTO(humidity.getId(),
                                                      humidity.getTimestamp(),
                                                      humidity.getValue(),
                                                      humidity.getGreenhouse().getId());
            return new ResponseEntity<>(humidityDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @Operation(summary = "Insert a new humidity reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Humidity reading successfully added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HumidityDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content)})
    @RequestMapping(value = "/hum/greenhouse/{greenhouseId}", method = RequestMethod.POST)
    public ResponseEntity<HumidityDTO> addHumidity(@PathVariable("greenhouseId") Long greenhouseId,
                                                         @RequestBody HumidityDTO dto, BindingResult bindingResult) {
        humidityValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            LoggerUtil.getCurrentLogger().warning(accessor.getMessage("invalid input"));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Greenhouse greenhouse = greenhouseService.getGreenhouseById(greenhouseId);
            Humidity humidity = new Humidity(dto.getTimestamp(), dto.getValue(), greenhouseId);
            humidity.setGreenhouse(greenhouse);
            greenhouse.addHumidity(humidity);
            Humidity savedHumidity = humidityService.insertHumidity(humidity);
            HumidityDTO humidityDTO = map(savedHumidity);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}")
                    .buildAndExpand(humidityDTO.getId()).toUri();
            return ResponseEntity.created(location).body(humidityDTO);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Get greenhouse max humidity reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Humidity reading found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HumidityDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Humidity reading not found",
                    content = @Content)})
    @RequestMapping(value = "/hum/max/greenhouse/{greenhouseId}", method = RequestMethod.GET)
    public ResponseEntity<HumidityDTO> getMaxTemperature(@PathVariable("greenhouseId") Long greenhouseId) {
        try {
            Humidity humidity = humidityService.getMaxHumidity(greenhouseId);
            HumidityDTO humidityDTO = new HumidityDTO(humidity.getId(),
                                                      humidity.getTimestamp(),
                                                      humidity.getValue(),
                                                      humidity.getGreenhouse().getId());
            return new ResponseEntity<>(humidityDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get greenhouse min humidity reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Humidity reading found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HumidityDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Humidity reading not found",
                    content = @Content)})
    @RequestMapping(value = "/hum/min/greenhouse/{greenhouseId}", method = RequestMethod.GET)
    public ResponseEntity<HumidityDTO> getMinTemperature(@PathVariable("greenhouseId") Long greenhouseId) {
        try {
            Humidity humidity = humidityService.getMinHumidity(greenhouseId);
            HumidityDTO humidityDTO = new HumidityDTO(humidity.getId(),
                                                      humidity.getTimestamp(),
                                                      humidity.getValue(),
                                                      humidity.getGreenhouse().getId());
            return new ResponseEntity<>(humidityDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get greenhouse last humidity reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Humidity reading found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = HumidityDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Humidity reading not found",
                    content = @Content)})
    @RequestMapping(value = "/hum/last/greenhouse/{greenhouseId}", method = RequestMethod.GET)
    public ResponseEntity<HumidityDTO> getGreenhouseLastHum(@PathVariable("greenhouseId") Long greenhouseId) {
        try {
            List<Humidity> humidityList = humidityService.getGreehouseLastHum(greenhouseId);
            Humidity humidity = humidityList.get(0);
            HumidityDTO humidityDTO = new HumidityDTO(humidity.getId(),
                                                      humidity.getTimestamp(),
                                                      humidity.getValue(),
                                                      humidity.getGreenhouse().getId());
            return new ResponseEntity<>(humidityDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    Private methods
    private HumidityDTO map(Humidity humidity) {
        HumidityDTO humidityDTO = new HumidityDTO();
        humidityDTO.setId(humidity.getId());
        humidityDTO.setTimestamp(humidity.getTimestamp());
        humidityDTO.setValue(humidity.getValue());
        humidityDTO.setGreenhouseId(humidity.getGreenhouse().getId());
        return humidityDTO;
    }
}
