package com.clinic.ms_especialistas.repository;

import com.clinic.ms_especialistas.model.Especialista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EspecialistaRepository extends JpaRepository<Especialista, UUID> {
    @Query(value = """
            SELECT *
            FROM CLINIC_ESPECIALISTA
            WHERE DELETE_TS IS NULL
        """, nativeQuery = true)
    public List<Especialista> getEspecialistas();
}
