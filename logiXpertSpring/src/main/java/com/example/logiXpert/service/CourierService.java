package com.example.logiXpert.service;

import com.example.logiXpert.dto.CourierDto;

public interface CourierService {
    CourierDto addCourier(CourierDto courierDto);

    CourierDto updateCourier(CourierDto courierDto);

    CourierDto getCourierById(Integer id);

    void deleteCourier(Integer id);
}
