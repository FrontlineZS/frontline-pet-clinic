package zs.frontline.frontlinepetclinicdata.services;


import zs.frontline.frontlinepetclinicdata.model.Owner;


public interface OwnerService extends CrudService<Owner, Long> {
    
    Owner findByLastName(String lastName);
}
