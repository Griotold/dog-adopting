package com.josh.dog_adopting.vet;

import com.josh.dog_adopting.adoption.DogAdoptionEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
class Vet {

    @ApplicationModuleListener // = @Async + @Transactional + @EventListener
    void checkup(DogAdoptionEvent dogAdoptionEvent) throws Exception {
        System.out.println("got a new event [" + dogAdoptionEvent + "]");
        Thread.sleep(5_000);
    }
}
