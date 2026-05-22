package org.sorasakistans.moviles_proyectofinal_biblioteca;

public class Estanteria {
    private int id;
    private String titulo;

    public Estanteria(int id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
}
