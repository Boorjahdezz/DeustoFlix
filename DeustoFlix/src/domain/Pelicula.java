package domain;

import javax.swing.ImageIcon;

public class Pelicula extends MediaItem {
	
	private double valoracion;
	
    public Pelicula(String titulo, String descripcion, Genero genero, Categoria categoria, ImageIcon imagen) {
        super(titulo, descripcion, genero, categoria, imagen);
        this.valoracion = Math.round(Math.random() * 10 * 10.0) / 10.0;
    }

    public Pelicula(String titulo, String descripcion, Genero genero, Categoria categoria, double valoracion2,
            int duracion) {
        super(titulo, descripcion, genero, categoria, null);
        this.valoracion = valoracion2;
    }

    @Override
    public String getTipo() { 
    	return "Pelicula"; 
    }
    public double getValoracion() {
        return valoracion;
    }
}