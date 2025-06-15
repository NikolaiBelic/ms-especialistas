package com.clinic.ms_especialistas.client;

import com.clinic.ms_especialistas.model.Especialista;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/especialistas")
public interface IEspecialista {

    @PostMapping("/filtro")
    public ResponseEntity<List<Especialista>> findEspecialistasByFiltro(
            @RequestHeader(value = "Tracking-Id") String trackingId,
            @RequestBody Map<String, Object> filtros
    );

    @GetMapping("/saludar")
    ResponseEntity<String> saludar();
}