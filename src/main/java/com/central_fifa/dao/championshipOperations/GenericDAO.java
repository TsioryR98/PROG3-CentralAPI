package com.central_fifa.dao.championshipOperations;

import java.util.Optional;

public interface GenericDAO <Model>{
    Optional<Model> findById(String id);
}
