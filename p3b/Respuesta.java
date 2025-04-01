package p3b;


import java.io.Serializable;

public class Respuesta implements Serializable {
    private String mensaje;

    public Respuesta(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}
