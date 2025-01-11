package org.telran.web.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telran.web.entity.Storage;
import org.telran.web.service.StorageService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @GetMapping
    public List<Storage> getAll(){
        return storageService.getAllStorage();
    }

    @GetMapping("/{id}")
    public Storage getById(@PathVariable(name = "id") Long id){
        return storageService.getByIdStorage(id);
    }

    @PostMapping
    public Storage create(@RequestBody @Valid Storage storage){
        return storageService.createStorage(storage);
    }
}
