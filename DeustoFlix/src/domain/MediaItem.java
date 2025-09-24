package domain;

import javax.swing.ImageIcon;

public abstract class MediaItem {
    protected String titulo;
    protected String descripcion;
    protected Genero genero;
    protected Categoria categoria;
    protected ImageIcon imagen;

    public MediaItem(String titulo, String descripcion, Genero genero, Categoria categoria, ImageIcon imagen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.genero = genero;
        this.categoria = categoria;
        this.imagen = imagen;
    }

    public String getTitulo() {
    	return titulo;
    }
    public String getDescripcion() {
    	return descripcion;
    }
    public Genero getGenero() {
    	return genero; 
    }
    public Categoria getCategoria() { 
    	return categoria; 
    }
    public ImageIcon getImagen() { 
    	return imagen; 
    }
    public abstract String getTipo();
}