package com.grupod.docintelia.controller;

import com.grupod.docintelia.dto.RolDTO;
import com.grupod.docintelia.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/rol")
public class RolController {

    @Autowired
    private RolService rolService;


    @GetMapping("/get-all")
    public List<RolDTO> getAllRol() {
        return rolService.getAllRol();
    }


}


