package domain;

import javax.swing.ImageIcon;

public abstract class MediaItem {

    // --- NUEVO CAMPO ID (Necesario para favoritos) ---
    protected int id;
    // -------------------------------------------------

    private String titulo;
    private String descripcion;
    private Genero genero;
    private Categoria categoria;
    private int duracion; // duración en minutos
    private ImageIcon imagen;

    // Constructor con duracion
    public MediaItem(String titulo, String descripcion, Genero genero, Categoria categoria, int duracion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.genero = genero;
        this.categoria = categoria;
        this.duracion = duracion;
        this.imagen = null; // imagen opcional
    }

    // Constructor con duracion e imagen
    public MediaItem(String titulo, String descripcion, Genero genero, Categoria categoria, int duracion, ImageIcon imagen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.genero = genero;
        this.categoria = categoria;
        this.duracion = duracion;
        this.imagen = imagen;
    }

    // --- NUEVOS GETTER Y SETTER PARA ID ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    // --------------------------------------

    // Getters y setters antiguos (intactos)
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public ImageIcon getImagen() {
        return imagen;
    }

    public void setImagen(ImageIcon imagen) {
        this.imagen = imagen;
    }

    public abstract String getTipo();
}