package com.example.frases7cm1.frase.aplicacion.impl;

import com.example.frases7cm1.frase.aplicacion.FraseServicio;
import com.example.frases7cm1.frase.dominio.entidades.Frase;
import com.example.frases7cm1.frase.dominio.repositorio.FraseRepositorio;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FraseServicioImpl implements FraseServicio {

    private static final Logger logger = LoggerFactory.getLogger(FraseServicioImpl.class);

    private final FraseRepositorio dao;

    @Autowired
    public FraseServicioImpl(FraseRepositorio dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public Frase obtenerFraseAleatoria() {
        List<Frase> frases = (List<Frase>) dao.findAll();
        if (frases.isEmpty()) return null;
        int index = (int) (Math.random() * frases.size());
        return frases.get(index);
    }

    @Override
    @Transactional
    public Frase guardarFrase(Frase frase) {
        return dao.save(frase);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Frase> obtenerFrases() {
        return (List<Frase>) dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Frase obtenerFraseById(Long id) {
        return dao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminarFrase(Long id) {
        dao.deleteById(id);
    }

    @Override
    public ByteArrayInputStream reporteDeFrasesPDF(List<Frase> listaDeFrases) {
        Document documento = new Document();
        ByteArrayOutputStream salida = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(documento, salida);
            documento.open();

            Font tipoLetra = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLUE);
            Paragraph parrafo = new Paragraph("Lista de Frases", tipoLetra);
            parrafo.setAlignment(Paragraph.ALIGN_CENTER);
            documento.add(parrafo);
            documento.add(Chunk.NEWLINE);

            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLUE);
            PdfPTable tabla = new PdfPTable(4);

            // Agregar encabezados de la tabla
            Stream.of("ID Frase", "Frase", "Autor", "Fecha de Creación")
                    .forEach(headerTitle -> {
                        PdfPCell encabezadoTabla = new PdfPCell();
                        Font headFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

                        encabezadoTabla.setBackgroundColor(BaseColor.CYAN);
                        encabezadoTabla.setHorizontalAlignment(Element.ALIGN_CENTER);
                        encabezadoTabla.setVerticalAlignment(Element.ALIGN_CENTER);
                        encabezadoTabla.setBorderWidth(2);
                        encabezadoTabla.setPhrase(new Phrase(headerTitle, headFont));
                        tabla.addCell(encabezadoTabla);
                    });

            // Agregar datos de las frases
            for (Frase frase : listaDeFrases) {
                // Celda ID Frase
                PdfPCell celdaIdFrase = new PdfPCell(new Phrase(String.valueOf(frase.getIdFrase()), textFont));
                celdaIdFrase.setPadding(1);
                celdaIdFrase.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaIdFrase.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaIdFrase);

                // Celda Frase
                PdfPCell celdaFrase = new PdfPCell(new Phrase(String.valueOf(frase.getTextoFrase()), textFont));
                celdaFrase.setPadding(1);
                celdaFrase.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaFrase.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaFrase);

                // Celda Autor
                PdfPCell celdaAutor = new PdfPCell(new Phrase(String.valueOf(frase.getAutor()), textFont));
                celdaAutor.setPadding(1);
                celdaAutor.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaAutor.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaAutor);

                // Celda Fecha
                PdfPCell celdaFecha = new PdfPCell(new Phrase(String.valueOf(frase.getFechaCreacion()), textFont));
                celdaFecha.setPadding(1);
                celdaFecha.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaFecha.setHorizontalAlignment(Element.ALIGN_LEFT);
                tabla.addCell(celdaFecha);
            }

            documento.add(tabla);
            documento.close();

        } catch (DocumentException e) {
            logger.error("Error al generar el PDF de frases", e);
        }

        return new ByteArrayInputStream(salida.toByteArray());
    }
}