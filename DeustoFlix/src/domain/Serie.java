package domain;

import javax.swing.ImageIcon;

public class Serie extends MediaItem {

    private double valoracion;

    // Constructor principal con valoracion y duracion
    public Serie(String titulo, String descripcion, Genero genero, Categoria categoria, double valoracion, int duracion) {
        super(titulo, descripcion, genero, categoria, duracion);
        this.valoracion = valoracion;
    }

    // Constructor con imagen, valoracion default 0 y duracion default 0
    public Serie(String titulo, String descripcion, Genero genero, Categoria categoria, ImageIcon imagen) {
        super(titulo, descripcion, genero, categoria, 0, imagen);
        this.valoracion = 0.0;
    }

    @Override
    public String getTipo() {
        return "Serie";
    }

    public double getValoracion() {
        return valoracion;
    }

    public void setValoracion(double valoracion) {
        this.valoracion = valoracion;
    }
}
