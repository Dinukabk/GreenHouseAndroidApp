package gr.aueb.cf.agronitor.rest;

import gr.aueb.cf.agronitor.dto.MeasurementsDTO;
import gr.aueb.cf.agronitor.model.Humidity;
import gr.aueb.cf.agronitor.model.SoilHydration;
import gr.aueb.cf.agronitor.model.Temperature;
import gr.aueb.cf.agronitor.model.UVRadiation;
import gr.aueb.cf.agronitor.service.IHumidityService;
import gr.aueb.cf.agronitor.service.ISoilHydrationService;
import gr.aueb.cf.agronitor.service.ITemperatureService;
import gr.aueb.cf.agronitor.service.IUVRadiationService;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.agronitor.service.util.LoggerUtil;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/greenhouses/measurements/")
public class MeasurementsRestController {

    private final ITemperatureService temperatureService;
    private final IHumidityService humidityService;
    private final ISoilHydrationService soilHydrationService;
    private final IUVRadiationService uvRadiationService;
    private final MessageSource messageSource;
    private MessageSourceAccessor accessor;

    @Autowired

    public MeasurementsRestController(ITemperatureService temperatureService,
                                      IHumidityService humidityService,
                                      ISoilHydrationService soilHydrationService,
                                      IUVRadiationService uvRadiationService,
                                      MessageSource messageSource) {
        this.temperatureService = temperatureService;
        this.humidityService = humidityService;
        this.soilHydrationService = soilHydrationService;
        this.uvRadiationService = uvRadiationService;
        this.messageSource = messageSource;
    }

    @PostConstruct
    private void init() {
        accessor = new MessageSourceAccessor(messageSource, Locale.getDefault());
    }

    @Operation(summary = "Get all measurements for a greenhouse")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Measurements found",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MeasurementsDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Measurement readings not found",
                    content = @Content)})
    @RequestMapping(value = "/{greenhouseId}", method = RequestMethod.GET)
    public ResponseEntity<MeasurementsDTO> getAllMeasurements(@PathVariable("greenhouseId") Long greenhouseId) {
        try {
            String minTemp = temperatureService.getMinTemperature(greenhouseId).getValue();
            System.out.println("minTemp = " + minTemp);
            String maxTemp = temperatureService.getMaxTemperature(greenhouseId).getValue();
            List<Temperature> temperatureList = temperatureService.getGreenhouseLastTemp(greenhouseId);
            String currentTemp = temperatureList.get(0).getValue();

            String minHum = humidityService.getMinHumidity(greenhouseId).getValue();
            String maxHum = humidityService.getMaxHumidity(greenhouseId).getValue();
            List<Humidity> humidityList = humidityService.getGreehouseLastHum(greenhouseId);
            String currentHum = humidityList.get(0).getValue();

            String minHydr = soilHydrationService.getMinSoilHydration(greenhouseId).getValue();
            String maxHydr = soilHydrationService.getMaxSoilHydration(greenhouseId).getValue();
            List<SoilHydration> soilHydrationList = soilHydrationService.getGreenhouseLastSoilHyd(greenhouseId);
            String currentHydr = soilHydrationList.get(0).getValue();

            String minUV = uvRadiationService.getMinUVRadiation(greenhouseId).getValue();
            String maxUV = uvRadiationService.getMaxUVRadiation(greenhouseId).getValue();
            List<UVRadiation> uvRadiationList = uvRadiationService.getGreenhouseLastUV(greenhouseId);
            String currentUV = uvRadiationList.get(0).getValue();

            MeasurementsDTO measurementsDTO = new MeasurementsDTO();
            measurementsDTO.setCurrentTemp(currentTemp);
            measurementsDTO.setCurrentHum(currentHum);
            measurementsDTO.setCurrentHydr(currentHydr);
            measurementsDTO.setCurrentUV(currentUV);
            measurementsDTO.setMinTemp(minTemp);
            measurementsDTO.setMaxTemp(maxTemp);
            measurementsDTO.setMinHum(minHum);
            measurementsDTO.setMaxHum(maxHum);
            measurementsDTO.setMinHydr(minHydr);
            measurementsDTO.setMaxHydr(maxHydr);
            measurementsDTO.setMinUV(minUV);
            measurementsDTO.setMaxUV(maxUV);
            return new ResponseEntity<>(measurementsDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            LoggerUtil.getCurrentLogger().warning(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
