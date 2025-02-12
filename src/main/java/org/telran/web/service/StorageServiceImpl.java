package org.telran.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Storage;
import org.telran.web.exception.StorageNotFoundException;
import org.telran.web.repository.StorageJpaRepository;

import java.util.List;

/**
 * Implementation of StorageService.
 * Handles business logic for managing storage items.
 */
@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StorageJpaRepository storageJpaRepository;

    /**
     * Retrieves all storage items from the repository.
     *
     * @return List of Storage entities representing all storage items.
     */
    @Override
    public List<Storage> getAllStorage() {
        return storageJpaRepository.findAll();
    }

    /**
     * Retrieves a storage item by its ID.
     *
     * @param id ID of the storage item.
     * @return The found Storage entity.
     * @throws StorageNotFoundException if the storage item is not found.
     */
    @Override
    public Storage getByIdStorage(Long id) {
        return storageJpaRepository.findById(id)
                .orElseThrow(() -> new StorageNotFoundException("Storage by " + id + " not Found"));
    }

    /**
     * Creates a new storage item and saves it in the repository.
     *
     * @param storage Storage entity containing storage details.
     * @return The created Storage entity.
     */
    @Override
    public Storage createStorage(Storage storage) {
        return storageJpaRepository.save(storage);
    }
}