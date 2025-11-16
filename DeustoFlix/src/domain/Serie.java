package domain;

import javax.swing.ImageIcon;

public class Serie extends MediaItem {
	
	private double valoracion;
	
    public Serie(String titulo, String descripcion, Genero genero, Categoria categoria, ImageIcon imagen) {
        super(titulo, descripcion, genero, categoria, imagen);
        this.valoracion = Math.round(Math.random() * 10 * 10.0) / 10.0;
    }

    @Override
    public String getTipo() {
    	return "Serie"; 
    }
    
    public double getValoracion() {
        return valoracion;
    }
}