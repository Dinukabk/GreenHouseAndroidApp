package gr.aueb.cf.agronitor.service;

import gr.aueb.cf.agronitor.dto.GreenhouseDTO;
import gr.aueb.cf.agronitor.model.Greenhouse;
import gr.aueb.cf.agronitor.repository.GreenhouseRepository;
import gr.aueb.cf.agronitor.service.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GreenhouseServiceImpl implements IGreenhouseService {

    private final GreenhouseRepository greenhouseRepository;

    @Autowired
    public GreenhouseServiceImpl(GreenhouseRepository greenhouseRepository) {
        this.greenhouseRepository = greenhouseRepository;
    }

    @org.springframework.transaction.annotation.Transactional
    @Override
    public Greenhouse insertGreenhouse(Greenhouse greenhouse) {
        return greenhouseRepository.save(greenhouse);
    }

    @org.springframework.transaction.annotation.Transactional
    @Override
    public Greenhouse updateGreenhouse(Long id, String greenhouseNewName) throws EntityNotFoundException {
        Greenhouse greenhouse = greenhouseRepository.findGreenhouseById(id);
        if (greenhouse == null) throw new EntityNotFoundException(Greenhouse.class, 0L);
        greenhouse.setGreenhouseName(greenhouseNewName);
        return greenhouseRepository.save(greenhouse);
    }

    @Transactional
    @Override
    public void deleteGreenhouse(Long id) {
        greenhouseRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Greenhouse getGreenhouseById(Long id) throws EntityNotFoundException {
        Greenhouse greenhouse = greenhouseRepository.findGreenhouseById(id);
        if (greenhouse == null) throw new EntityNotFoundException(Greenhouse.class, 0L);
        return greenhouse;
    }

    @Transactional
    @Override
    public List<Greenhouse> getGreenhouseByName(String greenhouseName) throws EntityNotFoundException {
        List<Greenhouse> greenhouses;
        greenhouses = greenhouseRepository.findGreenhouseByGreenhouseNameStartingWith(greenhouseName);
        if (greenhouses.size() == 0) throw new EntityNotFoundException(Greenhouse.class, 0L);
        return greenhouses;
    }

    @Transactional
    @Override
    public List<Greenhouse> getGreenhouseByUserId(Long id) throws EntityNotFoundException {
        List<Greenhouse> greenhouses;
        greenhouses = greenhouseRepository.findGreenhousesByUserId(id);
        if (greenhouses.size() == 0) throw new EntityNotFoundException(Greenhouse.class, 0L);
        return greenhouses;
    }

    //    Private methods
    private static Greenhouse convertToNewGreenhouse(GreenhouseDTO dto) {
        return new Greenhouse(dto.getGreenhouseName(), dto.getUserId());
    }
}
