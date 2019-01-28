package zs.frontline.frontlinepetclinicdata.services;


import zs.frontline.frontlinepetclinicdata.model.Vet;

import java.util.Set;



public interface VetService {

    Vet findById(Long id);
    
    Vet save(Vet vet);
    
    Set<Vet> findAll();
}
