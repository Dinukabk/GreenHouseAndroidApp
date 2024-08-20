package gr.aueb.cf.agronitor.service;

import gr.aueb.cf.agronitor.model.SoilHydration;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;

import java.util.List;

public interface ISoilHydrationService {
    SoilHydration insertSoilHydration(SoilHydration soilHydration);
    void deleteSoilHydration(Long id) throws EntityNotFoundException;
    SoilHydration getSoilHydrationById(Long id) throws EntityNotFoundException;
//    SoilHydration getGreenhouseSoilHydrationByTimestamp(Date timestamp, Long greenhouseId) throws EntityNotFoundException;
    SoilHydration getMaxSoilHydration(Long greenhouseId) throws EntityNotFoundException;
    SoilHydration getMinSoilHydration(Long greenhouseId) throws EntityNotFoundException;
    List<SoilHydration> getGreenhouseLastSoilHyd(Long greenhouseId) throws EntityNotFoundException;
}
