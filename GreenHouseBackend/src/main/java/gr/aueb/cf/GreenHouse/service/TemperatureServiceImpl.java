package gr.aueb.cf.agronitor.service;

import gr.aueb.cf.agronitor.model.Temperature;
import gr.aueb.cf.agronitor.repository.TemperatureRepository;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TemperatureServiceImpl implements ITemperatureService{

    private final TemperatureRepository temperatureRepository;

    @Autowired
    public TemperatureServiceImpl(TemperatureRepository temperatureRepository) {
        this.temperatureRepository = temperatureRepository;
    }

    @Transactional
    @Override
    public Temperature insertTemperature(Temperature temperature) {
        return temperatureRepository.save(temperature);
    }

    @Transactional
    @Override
    public void deleteTemperature(Long id) {
        temperatureRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Temperature getTemperatureById(Long id) throws EntityNotFoundException {
        Temperature temperature;
        temperature = temperatureRepository.findTemperatureById(id);
        if (temperature == null) throw new EntityNotFoundException(Temperature.class, 0L);
        return temperature;
    }

//    @Override
//    public Temperature getGreenhouseTempByTimestamp(Date timestamp, Long greenhouseId) throws EntityNotFoundException {
//        Temperature temperature = temperatureRepository.findGreenhouseTempByTimestamp(timestamp, greenhouseId);
//        if (temperature == null) throw new EntityNotFoundException(Temperature.class, 0L);
//        return temperature;
//    }

    @Transactional
    @Override
    public Temperature getMaxTemperature(Long greenhouseId) throws EntityNotFoundException {
        Temperature temperature = temperatureRepository.getMaxTemperature(greenhouseId);
        if (temperature == null) throw new EntityNotFoundException(Temperature.class, 0L);
        return temperature;
    }

    @Transactional
    @Override
    public Temperature getMinTemperature(Long greenhouseId) throws EntityNotFoundException {
        Temperature temperature = temperatureRepository.getMinTemperature(greenhouseId);
        if (temperature == null) throw new EntityNotFoundException(Temperature.class, 0L);
        return temperature;
    }

    @Transactional
    @Override
    public List<Temperature> getGreenhouseLastTemp(Long greenhouseId) throws EntityNotFoundException {
        List<Temperature> temperatures = temperatureRepository.findLastTemperature(greenhouseId);
        if (temperatures.isEmpty()) throw new EntityNotFoundException(Temperature.class, 0L);
        return temperatures;
    }
}
