package com.example.logiXpert.service;

import com.example.logiXpert.model.Courier;

public interface CourierService {
    Courier addCourier(Courier courier);

    Courier updateCourier(Courier courier);

    Courier getCourierById(Integer id);

    void deleteCourier(Integer id);
}
