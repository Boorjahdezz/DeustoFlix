package domain;

import javax.swing.ImageIcon;

public class Serie extends MediaItem {
    public Serie(String titulo, String descripcion, Genero genero, Categoria categoria, ImageIcon imagen) {
        super(titulo, descripcion, genero, categoria, imagen);
    }

    @Override
    public String getTipo() {
    	return "Serie"; 
    }
}