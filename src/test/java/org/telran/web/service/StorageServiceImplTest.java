package org.telran.web.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telran.web.entity.Storage;
import org.telran.web.exception.StorageNotFoundException;
import org.telran.web.repository.StorageJpaRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class StorageServiceImplTest {

    @Mock
    private StorageJpaRepository storageJpaRepository;

    @InjectMocks
    private StorageServiceImpl storageService;

    @Test
    public void testGetAllStorages() {
        List<Storage> storages = Arrays.asList(
                new Storage(1L, 100L),
                new Storage(2L, 200L));

        when(storageJpaRepository.findAll()).thenReturn(storages);

        List<Storage> result = storageService.getAllStorage();
        assertEquals(2, result.size());
        assertEquals(100L, result.get(0).getAmount());
        assertEquals(200L, result.get(1).getAmount());
    }


    @Test
    public void getByIdWhenStorageExists() {
        Long storageId = 3333333L;
        Storage storageExpected = new Storage();
        storageExpected.setId(storageId);
        storageExpected.setAmount(100L);

        when(storageJpaRepository.findById(storageId))
                .thenReturn(Optional.of(storageExpected));

        Storage storageActual = storageService.getByIdStorage(storageId);

        assertEquals(storageExpected.getId(), storageActual.getId());
        assertEquals(storageExpected.getAmount(), storageActual.getAmount());
    }

    @Test
    public void getByIdWhenStorageNotExists() {
        Long id = 4444444L;
        when(storageJpaRepository.findById(id))
                .thenThrow(new StorageNotFoundException("Storage not found"));

        assertThrows(StorageNotFoundException.class,
                () -> storageService.getByIdStorage(id));
    }

    @Test
    void createStorage() {
        Storage newStorage = new Storage(null, 300L);
        Storage savedStorage = new Storage(1L, 300L);

        when(storageJpaRepository.save(any(Storage.class))).thenReturn(savedStorage);

        Storage result = storageService.createStorage(newStorage);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(300L, result.getAmount());
    }


}