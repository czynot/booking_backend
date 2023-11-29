package com.booking.slots.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlotService {
    @Autowired
    private SlotRepository slotRepository;

    public Slot getSlotById(Integer id){
        return slotRepository.getById(id);
    }

    public List<Slot> getAllSlots(){
        return slotRepository.findAll();
    }


}
