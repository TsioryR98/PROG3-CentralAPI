package com.central_fifa.dao;

import java.util.Optional;

public interface GenericDAO <Model>{
    void save(Model model);
    Optional<Model> findById(String id);
    void delete(Model model);
}
