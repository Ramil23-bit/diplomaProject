package org.telran.web.service;

import org.telran.web.entity.Storage;

import java.util.List;

public interface StorageService {

    List<Storage> getAllStorage();

    Storage getByIdStorage(Long id);

    Storage createStorage(Storage storage);
}
