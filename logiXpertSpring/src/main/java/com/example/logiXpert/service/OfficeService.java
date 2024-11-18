package com.example.logiXpert.service;

import com.example.logiXpert.model.Office;

public interface OfficeService {
    Office addOffice(Office office);

    Office updateOffice(Office office);

    Office getOfficeById(Integer id);

    void deleteOffice(Integer id);
}
