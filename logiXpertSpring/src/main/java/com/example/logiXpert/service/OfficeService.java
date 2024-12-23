package com.example.logiXpert.service;

import com.example.logiXpert.dto.OfficeDto;

import java.util.List;

public interface OfficeService {
    OfficeDto addOffice(OfficeDto officeDto);

    OfficeDto updateOffice(OfficeDto officeDto);

    OfficeDto getOfficeById(Integer id);

    List<OfficeDto> getAllOffices();

    void deleteOffice(Integer id);
}