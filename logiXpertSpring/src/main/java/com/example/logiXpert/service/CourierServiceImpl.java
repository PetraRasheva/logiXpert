package com.example.logiXpert.service;

import com.example.logiXpert.exception.CourierNotFoundException;
import com.example.logiXpert.model.Courier;
import com.example.logiXpert.repository.CourierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourierServiceImpl implements CourierService {

    private final CourierRepository courierRepository;

    @Autowired
    public CourierServiceImpl(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @Override
    public Courier addCourier(Courier courier) {
        return courierRepository.save(courier);
    }

    @Override
    public Courier updateCourier(Courier courier) {
        return courierRepository.save(courier);
    }

    @Override
    public Courier getCourierById(Integer id) {
        return courierRepository.findCourierById(id)
                .orElseThrow(() -> new CourierNotFoundException("Courier with id " + id + " was not found"));
    }

    @Override
    public void deleteCourier(Integer id) {
        courierRepository.deleteCourierById(id);
    }

    //TODO: Implement complex requests
}
