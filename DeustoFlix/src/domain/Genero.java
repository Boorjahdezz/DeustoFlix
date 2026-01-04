package domain;

import java.awt.Color;

public enum Genero {
    ACCION(new Color(0, 80, 180)),
    DRAMA(new Color(100, 0, 100)),
    COMEDIA(new Color(255, 220, 100)),
    TERROR(new Color(160, 0, 0)),
    ROMANCE(new Color(255, 120, 180)),
    THRILLER(new Color(80, 80, 80)),
    FANTASIA(new Color(100, 0, 180)),         
    CIENCIA_FICCION(new Color(0, 150, 150)),  
    ANIMACION(new Color(255, 150, 0)),
    CRIMEN(new Color(50, 0, 0)),
    MUSICAL(new Color(200, 200, 0)),
    AVENTURA(new Color(0, 150, 50)),
    MISTERIO(new Color(60, 60, 90)),
    ESPIONAJE(new Color(30, 30, 30));

    private final Color color;

    Genero(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static Genero fromString(String value) {
        if (value == null) return DRAMA;
        try {
            // Reemplazamos espacios por guiones bajos para que "Ciencia Ficcion" coincida con "CIENCIA_FICCION"
            String normalizado = value.trim().toUpperCase().replace(" ", "_");
            return Genero.valueOf(normalizado);
        } catch (Exception e) {
            System.err.println("Genero no reconocido: " + value + ". Se asigna DRAMA.");
            return DRAMA; // Valor por defecto para no romper la carga
        }
    }
}