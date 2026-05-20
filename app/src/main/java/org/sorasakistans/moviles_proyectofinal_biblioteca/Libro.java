package org.sorasakistans.moviles_proyectofinal_biblioteca;

public class Libro {
    private String titulo;
    private String autor;
    private String editorial;
    private String isbn;


    public Libro(String t, String a, String e, String i){
        this.autor = a;
        this.titulo = t;
        this.editorial = e;
        this.isbn = i;
    }
}
