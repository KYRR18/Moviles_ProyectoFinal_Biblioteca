package org.sorasakistans.moviles_proyectofinal_biblioteca;

import java.util.ArrayList;
import java.util.List;

public class Estanteria {
    private int id;
    private String titulo;
    private List<Libro> libros;

    public Estanteria(int id, String titulo) {
        this.id     = id;
        this.titulo = titulo;
        this.libros = new ArrayList<>(); // empieza vacía
    }

    public int getId()           { return id; }
    public String getTitulo()    { return titulo; }
    public List<Libro> getLibros() { return libros; }

    /** Permite añadir un libro a la estantería sin reemplazar la lista completa */
    public void addLibro(Libro libro) {
        this.libros.add(libro);
    }

    /** Reemplaza toda la lista de libros (útil al cargar desde la API) */
    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
}
