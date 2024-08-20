package gr.aueb.cf.agronitor.service;

import gr.aueb.cf.agronitor.model.Humidity;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;

import java.util.List;

public interface IHumidityService {
    Humidity insertHumidity(Humidity humidity);
    void deleteHumidity(Long id) throws EntityNotFoundException;
    Humidity getHumidityById(Long id) throws EntityNotFoundException;
//    Humidity getGreenhouseHumidityByTimestamp(Date timestamp, Long greenhouseId) throws EntityNotFoundException;
    Humidity getMaxHumidity(Long greenhouseId) throws EntityNotFoundException;
    Humidity getMinHumidity(Long greenhouseId) throws EntityNotFoundException;
    List<Humidity> getGreehouseLastHum(Long greenhouseId) throws EntityNotFoundException;

}
