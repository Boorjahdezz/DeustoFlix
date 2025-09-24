package domain;

import javax.swing.ImageIcon;

public class Pelicula extends MediaItem {
    public Pelicula(String titulo, String descripcion, Genero genero, Categoria categoria, ImageIcon imagen) {
        super(titulo, descripcion, genero, categoria, imagen);
    }

    @Override
    public String getTipo() { 
    	return "Pelicula"; 
    }
}