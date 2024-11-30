package com.example.logiXpert.service;

import com.example.logiXpert.dto.OfficeDto;

public interface OfficeService {
    OfficeDto addOffice(OfficeDto officeDto);

    OfficeDto updateOffice(OfficeDto officeDto);

    OfficeDto getOfficeById(Integer id);

    void deleteOffice(Integer id);
}