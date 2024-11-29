package com.example.logiXpert.service;

import com.example.logiXpert.dto.OfficeDto;
import com.example.logiXpert.exception.OfficeNotFoundException;
import com.example.logiXpert.mapper.OfficeMapper;
import com.example.logiXpert.model.Office;
import com.example.logiXpert.repository.OfficeRepository;
import com.example.logiXpert.repository.CompanyRepository;
import com.example.logiXpert.service.OfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;
    private final OfficeMapper officeMapper;
    private final CompanyRepository companyRepository;

    @Autowired
    public OfficeServiceImpl(OfficeRepository officeRepository,
                             CompanyRepository companyRepository,
                             OfficeMapper officeMapper) {
        this.officeRepository = officeRepository;
        this.companyRepository = companyRepository;
        this.officeMapper = officeMapper;
    }
    @Override
    public OfficeDto addOffice(OfficeDto officeDto) {
        Office office = officeMapper.toEntity(officeDto);

        office.setCompany(
                companyRepository.findByName(officeDto.companyName())
                        .orElseThrow(() -> new OfficeNotFoundException("Company not found"))
        );

        Office savedOffice = officeRepository.save(office);
        return officeMapper.toDto(savedOffice);
    }

    @Override
    public OfficeDto updateOffice(OfficeDto officeDto) {
        Office existingOffice = officeRepository.findById(officeDto.id())
                .orElseThrow(() -> new OfficeNotFoundException("Office with id " + officeDto.id() + " was not found"));

        existingOffice.setName(officeDto.name());
        existingOffice.setAddress(officeDto.address());
        existingOffice.setOpenHours(officeDto.openHours());

//        existingOffice.setCompany(
//                companyRepository.findByName(officeDto.companyName())
//                        .orElseThrow(() -> new OfficeNotFoundException("Company not found"))
//        );

        Office updatedOffice = officeRepository.save(existingOffice);

        return officeMapper.toDto(updatedOffice);

    }

    @Override
    public OfficeDto getOfficeById(Integer id) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new OfficeNotFoundException("Office with id " + id + " was not found"));
        return officeMapper.toDto(office);
    }

    @Override
    public void deleteOffice(Integer id) {
        if (!officeRepository.existsById(id)) {
            throw new OfficeNotFoundException("Office with id " + id + " was not found");
        }
        officeRepository.deleteOfficeById(id);
    }

    //TODO: Implement complex requests
}
