package com.example.logiXpert.service;

import com.example.logiXpert.dto.CourierDto;
import com.example.logiXpert.dto.RegisterCourierDto;

public interface CourierService {
    CourierDto addCourier(RegisterCourierDto registrationDto);

    CourierDto updateCourier(CourierDto courierDto);

    CourierDto getCourierById(Integer id);

    void deleteCourier(Integer id);

    CourierDto updateCourierByAdmin(CourierDto courierDto);
}
