package com.example.logiXpert.controller;


import com.example.logiXpert.dto.OfficeDto;
import com.example.logiXpert.service.OfficeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/office")
public class OfficeController {
    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<OfficeDto> getOfficeById (@PathVariable("id") Integer id) {
        OfficeDto office = officeService.getOfficeById(id);
        return new ResponseEntity<>(office, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<OfficeDto> addOffice(@RequestBody OfficeDto officeDto) {
        OfficeDto newOffice = officeService.addOffice(officeDto);
        return new ResponseEntity<>(newOffice, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<OfficeDto> updateOffice(@RequestBody OfficeDto officeDto) {
        OfficeDto updatedOffice = officeService.updateOffice(officeDto);
        return new ResponseEntity<>(updatedOffice, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOffice(@PathVariable("id") Integer id) {
        officeService.deleteOffice(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

