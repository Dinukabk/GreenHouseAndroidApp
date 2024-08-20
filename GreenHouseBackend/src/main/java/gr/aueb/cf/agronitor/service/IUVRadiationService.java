package gr.aueb.cf.agronitor.service;

import gr.aueb.cf.agronitor.model.UVRadiation;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;

import java.util.List;

public interface IUVRadiationService {
    UVRadiation insertUVRadiation(UVRadiation uvRadiation);
    void deleteUVRadiation(Long id) throws EntityNotFoundException;
    UVRadiation getUVRadiationById(Long id) throws EntityNotFoundException;
//    UVRadiation getGreenhouseUVByTimestamp(Date timestamp, Long greenhouseId) throws EntityNotFoundException;
    UVRadiation getMaxUVRadiation(Long greenhouseId) throws EntityNotFoundException;
    UVRadiation getMinUVRadiation(Long greenhouseId) throws EntityNotFoundException;
    List<UVRadiation> getGreenhouseLastUV(Long greenhouseId) throws EntityNotFoundException;
}