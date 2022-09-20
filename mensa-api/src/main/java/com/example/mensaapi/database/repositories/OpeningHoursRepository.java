package com.example.mensaapi.database.repositories;

import com.example.mensaapi.database.entities.OpeningHours;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OpeningHoursRepository extends CrudRepository<OpeningHours, Integer> {
    Optional<List<OpeningHours>> findByCanteenIdEqualsOrderByWeekday(Integer canteenId);
    @Transactional
    List<OpeningHours> deleteByCanteenId(Integer canteendId);
}