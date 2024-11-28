package com.example.logiXpert.service;

import com.example.logiXpert.exception.OfficeNotFoundException;
import com.example.logiXpert.model.Office;
import com.example.logiXpert.repository.OfficeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;
    public OfficeServiceImpl(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }
    @Override
    public Office addOffice(Office office) {
        return officeRepository.save(office);
    }

    @Override
    public Office updateOffice(Office office) {
        return officeRepository.save(office);
    }

    @Override
    public Office getOfficeById(Integer id) {
        return officeRepository.findOfficeById(id)
                .orElseThrow(() -> new OfficeNotFoundException("Office with id " + id + " was not found"));
    }

    @Override
    @Transactional
    public void deleteOffice(Integer id) {
        officeRepository.deleteOfficeById(id);
    }

    //TODO: Implement complex requests
}
