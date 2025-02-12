package org.telran.web.service;

import org.telran.web.entity.Storage;

import java.util.List;

/**
 * Service interface for managing storage.
 * Provides methods for retrieving, creating, and managing storage items.
 */
public interface StorageService {

    /**
     * Retrieves all storage items.
     *
     * @return List of Storage entities representing all storage items.
     */
    List<Storage> getAllStorage();

    /**
     * Retrieves a storage item by its ID.
     *
     * @param id ID of the storage item.
     * @return The found Storage entity.
     */
    Storage getByIdStorage(Long id);

    /**
     * Creates a new storage item.
     *
     * @param storage Storage entity containing storage details.
     * @return The created Storage entity.
     */
    Storage createStorage(Storage storage);
}