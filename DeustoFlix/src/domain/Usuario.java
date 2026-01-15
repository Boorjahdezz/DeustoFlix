package domain;

public class Usuario {
    private int id;
    private String nombre;
    private String gmail;
    private String contrasenya;
    private String foto;

    public Usuario() {
    }

    // Constructor completto con ID
    public Usuario(int id, String nombre, String gmail, String contrasenya, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.gmail = gmail;
        this.contrasenya = contrasenya;
        this.foto = foto;
    }

    // Constructor sin ID (para crear nuevos usuarios antes de guardarlos)
    public Usuario(String nombre, String gmail, String contrasenya, String foto) {
        this.nombre = nombre;
        this.gmail = gmail;
        this.contrasenya = contrasenya;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}