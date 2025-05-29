package com.clinic.ms_especialistas.controller;

import com.clinic.ms_especialistas.client.IEspecialista;
import com.clinic.ms_especialistas.model.Especialista;
import com.clinic.ms_especialistas.service.EspecialistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
public class EspecialistaController implements IEspecialista {

    @Autowired
    private EspecialistaService especialistaService;

    @Override
    public ResponseEntity<List<Especialista>> findEspecialistasByFiltro(
            String trackingId,
            Map<String, Object> filtros
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(especialistaService.findEspecialistasByFiltro(
                trackingId,
                filtros
        ));
    }
}
