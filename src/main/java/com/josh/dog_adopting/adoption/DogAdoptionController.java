package com.josh.dog_adopting.adoption;

import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DogAdoptionController {
    private final DogRepository repository;

    DogAdoptionController(DogRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/dogs/{dogId}/adoptions")
    void adopt(@PathVariable("dogId") Integer dogId,
               @RequestBody Map<String, String> owner) {
        this.repository.findById(dogId)
                .ifPresent(dog -> {
                    var newDog = new Dog(dogId, dog.name(), dog.description(), owner.get("name"));
                    this.repository.save(newDog);
                });
    }
}

interface DogRepository extends ListCrudRepository<Dog, Integer> {

}

record Dog(@Id Integer id, String name, String description, String owner) {

}
