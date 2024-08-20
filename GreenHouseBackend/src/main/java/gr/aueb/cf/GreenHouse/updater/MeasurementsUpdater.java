package gr.aueb.cf.agronitor.updater;

import gr.aueb.cf.agronitor.model.*;
import gr.aueb.cf.agronitor.repository.*;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public class MeasurementsUpdater {

    private final GreenhouseRepository greenhouseRepository;
    private final TemperatureRepository temperatureRepository;
    private final HumidityRepository humidityRepository;
    private final SoilHydrationRepository soilHydrationRepository;
    private final UVRadiationRepository uvRadiationRepository;

    public MeasurementsUpdater(GreenhouseRepository greenhouseRepository,
                               TemperatureRepository temperatureRepository,
                               HumidityRepository humidityRepository,
                               SoilHydrationRepository soilHydrationRepository,
                               UVRadiationRepository uvRadiationRepository) {
        this.greenhouseRepository = greenhouseRepository;
        this.temperatureRepository = temperatureRepository;
        this.humidityRepository = humidityRepository;
        this.soilHydrationRepository = soilHydrationRepository;
        this.uvRadiationRepository = uvRadiationRepository;
    }

    @Transactional
    public void updateGreenhouseMeasurements() {
        List<Greenhouse> greenhouseList = greenhouseRepository.findAll();

        for (Greenhouse greenhouse : greenhouseList) {
            String tempValue = generateTemperatureValue();
            String humValue = generateHumidityValue();
            String hydrValue = generateSoilHydrationValue();
            String uvValue = generateUVRadiationValue();
            Date timeStamp = new Date();

            Temperature temperature = new Temperature();
            temperature.setValue(tempValue);
            temperature.setTimestamp(timeStamp);
            greenhouse = greenhouseRepository.findGreenhouseById(greenhouse.getId());
            if (greenhouse != null) {
                temperature.setGreenhouse(greenhouse);
                greenhouse.addTemperature(temperature);
                temperatureRepository.save(temperature);
            }

            Humidity humidity = new Humidity();
            humidity.setValue(humValue);
            humidity.setTimestamp(timeStamp);
            humidity.setGreenhouse(greenhouse);
            greenhouse = greenhouseRepository.findGreenhouseById(greenhouse.getId());
            if (greenhouse != null) {
                humidity.setGreenhouse(greenhouse);
                greenhouse.addHumidity(humidity);
                humidityRepository.save(humidity);
            }

            SoilHydration soilHydration = new SoilHydration();
            soilHydration.setValue(hydrValue);
            soilHydration.setTimestamp(timeStamp);
            soilHydration.setGreenhouse(greenhouse);
            greenhouse = greenhouseRepository.findGreenhouseById(greenhouse.getId());
            if (greenhouse != null) {
                soilHydration.setGreenhouse(greenhouse);
                greenhouse.addSoilHydration(soilHydration);
                soilHydrationRepository.save(soilHydration);
            }

            UVRadiation uvRadiation = new UVRadiation();
            uvRadiation.setValue(uvValue);
            uvRadiation.setTimestamp(timeStamp);
            uvRadiation.setGreenhouse(greenhouse);
            greenhouse = greenhouseRepository.findGreenhouseById(greenhouse.getId());
            if (greenhouse != null) {
                uvRadiation.setGreenhouse(greenhouse);
                greenhouse.addUVRadiation(uvRadiation);
                uvRadiationRepository.save(uvRadiation);
            }
        }
    }

    private String generateTemperatureValue() {
        double min = 20.0;
        double max = 40.0;
        double randomValue = min + Math.random() * (max - min);
        return String.format("%.2f", randomValue);
    }

    private String generateHumidityValue() {
        double min = 0.0;
        double max = 100.0;
        double randomValue = min + Math.random() * (max - min);
        return String.format("%.2f", randomValue);
    }

    private String generateSoilHydrationValue() {
        double min = 0.0;
        double max = 100.0;
        double randomValue = min + Math.random() * (max - min);
        return String.format("%.2f", randomValue);
    }

    private String generateUVRadiationValue() {
        double min = 0.0;
        double max = 100.0;
        double randomValue = min + Math.random() * (max - min);
        return String.format("%.2f", randomValue);
    }
}
