package main;

import gui.MainGuiWindow;
import gui.VentanaInicio;

// Main para inicializar la aplicacion

public class Main {
    public static void main(String[] args) {
        MainGuiWindow window = new MainGuiWindow();
        VentanaInicio inicio = new VentanaInicio();
        inicio.setVisible(true);
      //  window.setVisible(true);
    }
}