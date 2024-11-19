package com.example.logiXpert.controller;


import com.example.logiXpert.model.Office;
import com.example.logiXpert.service.OfficeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/office")
public class OfficeController {
    private final OfficeService officeService;

    OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Office> getOfficeById (@PathVariable("id") Integer id) {
        Office office = officeService.getOfficeById(id);
        return new ResponseEntity<>(office, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Office> addOffice(@RequestBody Office office) {
        Office newOffice = officeService.addOffice(office);
        return new ResponseEntity<>(newOffice, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Office> updateOffice(@RequestBody Office office) {
        Office updateOffice = officeService.updateOffice(office);
        return new ResponseEntity<>(updateOffice, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOffice(@PathVariable("id") Integer id) {
        officeService.deleteOffice(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
