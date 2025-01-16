package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Storage;
import org.telran.web.exception.StorageNotFoundException;
import org.telran.web.repository.StorageJpaRepository;

import java.util.List;

@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StorageJpaRepository storageJpaRepository;
    @Override
    public List<Storage> getAllStorage() {
        return storageJpaRepository.findAll();
    }

    @Override
    public Storage getByIdStorage(Long id) {
        return storageJpaRepository.findById(id)
                .orElseThrow(() -> new StorageNotFoundException("Storage by " + id +" not Found"));
    }

    @Override
    public Storage createStorage(Storage storage) {
        return storageJpaRepository.save(storage);
    }

}
