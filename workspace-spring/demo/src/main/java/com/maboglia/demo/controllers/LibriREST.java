package com.maboglia.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maboglia.demo.entities.Libro;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api")
public class LibriREST {
    
    @GetMapping("libro")
    public Libro libro() {
        Libro libro = new Libro("Il nome della rosa", "Umberto Eco", 1980);
        return libro;
    }
    

}
