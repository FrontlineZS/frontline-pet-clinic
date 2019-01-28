package zs.frontline.frontlinepetclinicdata.services;


import zs.frontline.frontlinepetclinicdata.model.Pet;

import java.util.Set;



public interface PetService {
    
    Pet findById(Long id);
    
    Pet save(Pet owner);
    
    Set<Pet> findAll();
}
