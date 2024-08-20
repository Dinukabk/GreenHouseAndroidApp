package gr.aueb.cf.agronitor.service;

import gr.aueb.cf.agronitor.model.SoilHydration;
import gr.aueb.cf.agronitor.repository.SoilHydrationRepository;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SoilHydrationServiceImpl implements ISoilHydrationService {

    private final SoilHydrationRepository soilHydrationRepository;

    @Autowired
    public SoilHydrationServiceImpl(SoilHydrationRepository soilHydrationRepository) {
        this.soilHydrationRepository = soilHydrationRepository;
    }

    @Transactional
    @Override
    public SoilHydration insertSoilHydration(SoilHydration soilHydration) {
        return soilHydrationRepository.save(soilHydration);
    }

    @Transactional
    @Override
    public void deleteSoilHydration(Long id) {
        soilHydrationRepository.deleteById(id);
    }

    @Transactional
    @Override
    public SoilHydration getSoilHydrationById(Long id) throws EntityNotFoundException {
        SoilHydration soilHydration;
        soilHydration = soilHydrationRepository.findSoilHydrationById(id);
        if (soilHydration == null) throw new EntityNotFoundException(SoilHydration.class, 0L);
        return soilHydration;
    }

    @Transactional
    @Override
    public SoilHydration getMaxSoilHydration(Long greenhouseId) throws EntityNotFoundException {
        SoilHydration soilHydration = soilHydrationRepository.getMaxSoilHydration(greenhouseId);
        if (soilHydration == null) throw new EntityNotFoundException(SoilHydration.class, 0L);
        return soilHydration;
    }

    @Transactional
    @Override
    public SoilHydration getMinSoilHydration(Long greenhouseId) throws EntityNotFoundException {
        SoilHydration soilHydration = soilHydrationRepository.getMinSoilHydration(greenhouseId);
        if (soilHydration == null) throw new EntityNotFoundException(SoilHydration.class, 0L);
        return soilHydration;
    }

    @Transactional
    @Override
    public List<SoilHydration> getGreenhouseLastSoilHyd(Long greenhouseId) throws EntityNotFoundException {
        List<SoilHydration> soilHydrations = soilHydrationRepository.findLastSoilHydration(greenhouseId);
        if (soilHydrations.isEmpty()) throw new EntityNotFoundException(SoilHydration.class, 0L);
        return soilHydrations;
    }
}
