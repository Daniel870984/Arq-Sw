
import java.io.Serializable;

/**
 * Clase que representa la respuesta de la ejecución de un servicio.
 */
public class Respuesta implements Serializable {
    private String mensaje;

    public Respuesta(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
    
    @Override
    public String toString() {
        return mensaje;
    }
}
