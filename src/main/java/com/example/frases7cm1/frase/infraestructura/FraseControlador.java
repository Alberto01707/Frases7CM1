package com.example.frases7cm1.frase.infraestructura;

import com.example.frases7cm1.frase.aplicacion.FraseServicio;
import com.example.frases7cm1.frase.dominio.entidades.Frase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/frases")

public class FraseControlador {
    @Autowired
    private FraseServicio servicio;

    @GetMapping("/frase/aleatorio")
    @ResponseStatus(HttpStatus.OK)
    public Frase obtenerFraseAleatorio(){
        return servicio.obtenerFraseAleatoria();
    }

    @GetMapping("/frase")
    @ResponseStatus(HttpStatus.OK)
    public List<Frase> obtenerFrases(){
        return servicio.obtenerFrases();
    }

    @GetMapping("/frase/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Frase obtenerFrasePorId(@PathVariable Long id){
        return servicio.obtenerFraseById(id);
    }

    @PostMapping("/frase")
    @ResponseStatus(HttpStatus.CREATED)
    public Frase guardarFrase(@RequestBody Frase frase){
        return servicio.guardarFrase(frase);
    }

    @DeleteMapping("/frase/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPorId(@PathVariable Long id){
        servicio.eliminarFrase(id);
    }


    @PutMapping("/frase/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Frase actualizarFrase(@RequestBody Frase frase, @PathVariable Long id){
        Frase f = servicio.obtenerFraseById(id);
        f.setTextoFrase(frase.getTextoFrase());
        f.setAutor(frase.getAutor());
        f.setFechaCreacion(frase.getFechaCreacion());
        return servicio.guardarFrase(f);
    }

    @GetMapping("/frase/reporte/pdf")
    public ResponseEntity<InputStreamResource> generarReporteDeFrasePDF(){
        List<Frase> ListaDeFrases = servicio.obtenerFrases();
        ByteArrayInputStream bis = servicio.reporteDeFrasesPDF(ListaDeFrases);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte_de_frases.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));


    }

}
