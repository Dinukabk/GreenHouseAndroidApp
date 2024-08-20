package gr.aueb.cf.agronitor.updater;

import gr.aueb.cf.agronitor.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MeasurementsUpdaterService {
    private final MeasurementsUpdater measurementsUpdater;

    @Autowired
    public MeasurementsUpdaterService(GreenhouseRepository greenhouseRepository,
                                      TemperatureRepository temperatureRepository,
                                      HumidityRepository humidityRepository,
                                      SoilHydrationRepository soilHydrationRepository,
                                      UVRadiationRepository uvRadiationRepository) {
        this.measurementsUpdater = new MeasurementsUpdater(greenhouseRepository,
                                                           temperatureRepository,
                                                           humidityRepository,
                                                           soilHydrationRepository,
                                                           uvRadiationRepository);
    }

    @Transactional
    @Scheduled(fixedRate = 1800000)
    public void updateMeasurements() {
        measurementsUpdater.updateGreenhouseMeasurements();
    }
}
