package gui.avatar;

import javax.swing.ImageIcon;

public final class UserSession {
    private static String usuario;
    private static ImageIcon avatar;

    private UserSession() {}

    public static void set(String user, ImageIcon icon) {
        usuario = user;
        avatar = icon;
    }

    public static String getUsuario() {
        return usuario;
    }

    public static ImageIcon getAvatar() {
        return avatar;
    }
}
