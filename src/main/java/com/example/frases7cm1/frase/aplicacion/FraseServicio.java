package com.example.frases7cm1.frase.aplicacion;

import com.example.frases7cm1.frase.dominio.entidades.Frase;
import com.itextpdf.text.pdf.qrcode.ByteArray;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface FraseServicio {

    public Frase obtenerFraseAleatoria();
    public Frase guardarFrase(Frase frase);
    public List<Frase> obtenerFrases();
    public Frase obtenerFraseById(Long id);
    public void eliminarFrase(Long id);

    public ByteArrayInputStream reporteDeFrasesPDF(List<Frase> ListaDeFrases);

}
