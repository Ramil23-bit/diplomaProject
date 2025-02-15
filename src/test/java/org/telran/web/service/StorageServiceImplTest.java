package org.telran.web.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telran.web.entity.Storage;
import org.telran.web.exception.StorageNotFoundException;
import org.telran.web.repository.StorageJpaRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


/**
 *   Key Features:
 * - Uses `@ExtendWith(MockitoExtension.class)` to enable Mockito for unit testing.
 * - Mocks `StorageJpaRepository` to **isolate service logic** from database dependencies.
 * - Ensures **proper handling of existing and non-existing storage records**.
 * - Verifies correct **error handling** for `StorageNotFoundException`.
 */

@ExtendWith(MockitoExtension.class)
class StorageServiceImplTest {

    @Mock
    private StorageJpaRepository storageJpaRepository;

    @InjectMocks
    private StorageServiceImpl storageService;

    /**
     **Test Case:** Retrieve all storage items.
     **Expected Result:** Returns a list of storage entries.
     */
    @Test
    public void testGetAllStorages() {
        List<Storage> storages = Arrays.asList(
                new Storage(1L, 100L),
                new Storage(2L, 200L));

        when(storageJpaRepository.findAll()).thenReturn(storages);

        List<Storage> result = storageService.getAllStorage();

        assertEquals(2, result.size(), "Storage list size must be 2");
        assertEquals(100L, result.get(0).getAmount(), "First storage amount must be 100");
        assertEquals(200L, result.get(1).getAmount(), "Second storage amount must be 200");
    }

    /**
     **Test Case:** Retrieve a storage item by ID.
     **Expected Result:** Returns the correct storage entity.
     */
    @Test
    public void getByIdWhenStorageExists() {
        Long storageId = 3333333L;
        Storage storageExpected = new Storage(storageId, 100L);

        when(storageJpaRepository.findById(storageId)).thenReturn(Optional.of(storageExpected));

        Storage storageActual = storageService.getByIdStorage(storageId);

        assertEquals(storageExpected.getId(), storageActual.getId(), "Storage ID must match");
        assertEquals(storageExpected.getAmount(), storageActual.getAmount(), "Storage amount must match");
    }

    /**
     **Test Case:** Attempt to retrieve a non-existing storage item by ID.
     **Expected Result:** Throws `StorageNotFoundException`.
     */
    @Test
    public void getByIdWhenStorageNotExists() {
        Long id = 4444444L;
        when(storageJpaRepository.findById(id)).thenThrow(new StorageNotFoundException("Storage not found"));

        assertThrows(StorageNotFoundException.class, () -> storageService.getByIdStorage(id));
    }

    /**
     **Test Case:** Successfully create a new storage entry.
     **Expected Result:** The storage is saved and returned correctly.
     */
    @Test
    void createStorage() {
        Storage newStorage = new Storage(null, 300L);
        Storage savedStorage = new Storage(1L, 300L);

        when(storageJpaRepository.save(any(Storage.class))).thenReturn(savedStorage);

        Storage result = storageService.createStorage(newStorage);

        assertNotNull(result, "Result must not be null");
        assertEquals(1L, result.getId(), "Storage ID must be 1");
        assertEquals(300L, result.getAmount(), "Storage amount must be 300");
    }
}