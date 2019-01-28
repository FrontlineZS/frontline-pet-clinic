package zs.frontline.frontlinepetclinic.model;

import lombok.Data;

import java.time.LocalDate;


@Data
public class Pet {

    private LocalDate birthDate;
    private PetType type;
    private Owner owner;
}
