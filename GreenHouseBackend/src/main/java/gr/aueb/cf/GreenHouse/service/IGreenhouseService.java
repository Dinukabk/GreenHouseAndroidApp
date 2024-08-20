package gr.aueb.cf.agronitor.service;

import gr.aueb.cf.agronitor.dto.GreenhouseDTO;
import gr.aueb.cf.agronitor.model.Greenhouse;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;

import java.util.List;

public interface IGreenhouseService {
    Greenhouse insertGreenhouse(Greenhouse greenhouse);
    Greenhouse updateGreenhouse(Long id, String greenhouseNewName) throws EntityNotFoundException;
    void deleteGreenhouse(Long id) throws EntityNotFoundException;
    Greenhouse getGreenhouseById(Long id) throws EntityNotFoundException;
    List<Greenhouse> getGreenhouseByName(String greenhouseName) throws EntityNotFoundException;
    List<Greenhouse> getGreenhouseByUserId(Long id) throws EntityNotFoundException;
}
