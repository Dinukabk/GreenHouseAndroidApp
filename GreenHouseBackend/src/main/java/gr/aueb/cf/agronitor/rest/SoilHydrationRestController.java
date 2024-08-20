package gr.aueb.cf.agronitor.rest;

import gr.aueb.cf.agronitor.dto.SoilHydrationDTO;
import gr.aueb.cf.agronitor.model.Greenhouse;
import gr.aueb.cf.agronitor.model.SoilHydration;
import gr.aueb.cf.agronitor.service.IGreenhouseService;
import gr.aueb.cf.agronitor.service.ISoilHydrationService;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.agronitor.service.util.LoggerUtil;
import gr.aueb.cf.agronitor.validator.SoilHydrationValidator;
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
public class SoilHydrationRestController {

    private final ISoilHydrationService soilHydrationService;
    private final SoilHydrationValidator soilHydrationValidator;
    private final IGreenhouseService greenhouseService;
    private final MessageSource messageSource;
    private MessageSourceAccessor accessor;

    @Autowired
    public SoilHydrationRestController(ISoilHydrationService soilHydrationService,
                                       SoilHydrationValidator soilHydrationValidator,
                                       IGreenhouseService greenhouseService,
                                       MessageSource messageSource) {
        this. soilHydrationService = soilHydrationService;
        this.soilHydrationValidator = soilHydrationValidator;
        this.greenhouseService = greenhouseService;
        this.messageSource = messageSource;
    }

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    @Operation(summary =  "Get soil hydration by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Soil hydration reading found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SoilHydrationDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Soil hydration reading not found",
                    content = @Content)})
    @RequestMapping(value = "/hydr/{hydrId}", method = RequestMethod.GET)
    public ResponseEntity<SoilHydrationDTO> getSoilHydrationById(@PathVariable("hydrId") Long hydrId) {
        SoilHydration soilHydration;
        try {
            soilHydration = soilHydrationService.getSoilHydrationById(hydrId);
            SoilHydrationDTO soilHydrationDTO = new SoilHydrationDTO(soilHydration.getId(),
                                                                     soilHydration.getTimestamp(),
                                                                     soilHydration.getValue(),
                                                                     soilHydration.getGreenhouse().getId());
            return new ResponseEntity<>(soilHydrationDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Insert a new soil hydration reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Soil hydration reading successfully added",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SoilHydrationDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input was supplied",
                    content = @Content)})
    @RequestMapping(value = "/hydr/greenhouse/{greenhouseId}", method = RequestMethod.POST)
    public ResponseEntity<SoilHydrationDTO> addTemperature(@PathVariable("greenhouseId") Long greenhouseId,
                                                         @RequestBody SoilHydrationDTO dto, BindingResult bindingResult) {
        soilHydrationValidator.validate(dto, bindingResult);
        if (bindingResult.hasErrors()) {
            LoggerUtil.getCurrentLogger().warning(accessor.getMessage("invalid input"));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            Greenhouse greenhouse = greenhouseService.getGreenhouseById(greenhouseId);
            SoilHydration soilHydration = new SoilHydration(dto.getTimestamp(), dto.getValue(), greenhouseId);
            soilHydration.setGreenhouse(greenhouse);
            greenhouse.addSoilHydration(soilHydration);
            SoilHydration savedSoilhydration = soilHydrationService.insertSoilHydration(soilHydration);
            SoilHydrationDTO soilHydrationDTO = map(savedSoilhydration);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}")
                    .buildAndExpand(soilHydrationDTO.getId()).toUri();
            return ResponseEntity.created(location).body(soilHydrationDTO);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Operation(summary = "Get greenhouse max soil hydration reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Soil hydration reading found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SoilHydrationDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Soil hydration reading not found",
                    content = @Content)})
    @RequestMapping(value = "/hydr/max/greenhouse/{greenhouseId}", method = RequestMethod.GET)
    public ResponseEntity<SoilHydrationDTO> getMaxSoilHydration(@PathVariable("greenhouseId") Long greenhouseId) {
        try {
            SoilHydration soilHydration = soilHydrationService.getMaxSoilHydration(greenhouseId);
            SoilHydrationDTO soilHydrationDTO = new SoilHydrationDTO(soilHydration.getId(),
                                                                     soilHydration.getTimestamp(),
                                                                     soilHydration.getValue(),
                                                                     soilHydration.getGreenhouse().getId());
            return new ResponseEntity<>(soilHydrationDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get greenhouse min soil hydration reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Soil hydration reading found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SoilHydrationDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Soil hydration reading not found",
                    content = @Content)})
    @RequestMapping(value = "/hydr/min/greenhouse/{greenhouseId}", method = RequestMethod.GET)
    public ResponseEntity<SoilHydrationDTO> getMinSoilHydration(@PathVariable("greenhouseId") Long greenhouseId) {
        try {
            SoilHydration soilHydration = soilHydrationService.getMinSoilHydration(greenhouseId);
            SoilHydrationDTO soilHydrationDTO = new SoilHydrationDTO(soilHydration.getId(),
                                                                     soilHydration.getTimestamp(),
                                                                     soilHydration.getValue(),
                                                                     soilHydration.getGreenhouse().getId());
            return new ResponseEntity<>(soilHydrationDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get greenhouse last soil hydration reading")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Soil hydration reading found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SoilHydrationDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Soil hydration reading not found",
                    content = @Content)})
    @RequestMapping(value = "/hydr/last/greenhouse/{greenhouseId}", method = RequestMethod.GET)
    public ResponseEntity<SoilHydrationDTO> getGreenhouseLastHydr(@PathVariable("greenhouseId") Long greenhouseId) {
        try {
            List<SoilHydration> soilHydrations = soilHydrationService.getGreenhouseLastSoilHyd(greenhouseId);
            SoilHydration soilHydration = soilHydrations.get(0);
            SoilHydrationDTO soilHydrationDTO = new SoilHydrationDTO(soilHydration.getId(),
                                                                     soilHydration.getTimestamp(),
                                                                     soilHydration.getValue(),
                                                                     soilHydration.getGreenhouse().getId());
            return new ResponseEntity<>(soilHydrationDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    Private methods
    private SoilHydrationDTO map(SoilHydration soilHydration) {
        SoilHydrationDTO soilHydrationDTO = new SoilHydrationDTO();
        soilHydrationDTO.setId(soilHydration.getId());
        soilHydrationDTO.setTimestamp(soilHydration.getTimestamp());
        soilHydrationDTO.setValue(soilHydration.getValue());
        soilHydrationDTO.setGreenhouseId(soilHydration.getGreenhouse().getId());
        return soilHydrationDTO;
    }
}
