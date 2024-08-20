package gr.aueb.cf.agronitor.service;

import gr.aueb.cf.agronitor.model.Temperature;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;

import java.util.List;

public interface ITemperatureService {
    Temperature insertTemperature(Temperature temperature);
    void deleteTemperature(Long id) throws EntityNotFoundException;
    Temperature getTemperatureById(Long id) throws EntityNotFoundException;
//    Temperature getGreenhouseTempByTimestamp(Date timestamp, Long greenhouseId) throws EntityNotFoundException;
    Temperature getMaxTemperature(Long greenhouseId) throws EntityNotFoundException;
    Temperature getMinTemperature(Long greenhouseId) throws EntityNotFoundException;
    List<Temperature> getGreenhouseLastTemp(Long greenhouseId) throws EntityNotFoundException;
}
