package domain;

public enum Genero {
    ACCION,
    DRAMA,
    COMEDIA,
    TERROR,
    ROMANCE,
    THRILLER,
    FANTASIA,
    CIENCIA_FICCION,
    ANIMACION,
    CRIMEN,
    MUSICAL,
    AVENTURA,
    MISTERIO,
    ESPIONAJE;

    public static Genero fromString(String value) {
        try {
            return Genero.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            System.err.println("Genero no válido: " + value);
            return null; // o puedes retornar un default, por ejemplo ACCION
        }
    }
}
