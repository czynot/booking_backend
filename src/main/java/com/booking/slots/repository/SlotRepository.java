package com.booking.slots.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Integer> {
    @NotNull
    List<Slot> findAll();
}
