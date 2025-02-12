package org.telran.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Autowired
    private StorageJpaRepository storageJpaRepository;

    /**
     * Retrieves all storage items from the repository.
     *
     * @return List of Storage entities representing all storage items.
     */
    @Override
    public List<Storage> getAllStorage() {
        logger.info("Fetching all storage items");
        List<Storage> storages = storageJpaRepository.findAll();
        logger.info("Total storage items retrieved: {}", storages.size());
        return storages;
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
        logger.info("Fetching storage item with ID: {}", id);
        return storageJpaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Storage item with ID {} not found", id);
                    return new StorageNotFoundException("Storage by " + id + " not found");
                });
    }

    /**
     * Creates a new storage item and saves it in the repository.
     *
     * @param storage Storage entity containing storage details.
     * @return The created Storage entity.
     */
    @Override
    public Storage createStorage(Storage storage) {
        logger.info("Creating new storage item");
        Storage savedStorage = storageJpaRepository.save(storage);
        logger.info("Storage item created successfully with ID: {}", savedStorage.getId());
        return savedStorage;
    }
}
