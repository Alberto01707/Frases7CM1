package com.example.frases7cm1.frase.dominio.entidades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="Frase")

public class Frase implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long idFrase;
    @Column(nullable = false, length = 100)
    private String autor;
    @Column(nullable = false, length = 500)
    private String textoFrase;
    @Column
    @Temporal(TemporalType.DATE)  // ← CORRECTO
    private Date fechaCreacion;

    @PrePersist
        public void perPersist(){
        fechaCreacion = new Date();
    }

    @PreUpdate
    public void perUpdate(){
        fechaCreacion = new Date();
    }


}
