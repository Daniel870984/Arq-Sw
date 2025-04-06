
import java.io.Serializable;
import java.util.Vector;

/**
 * Clase que encapsula una lista de servicios.
 */
public class Servicios implements Serializable {
    private Vector<Servicio> servicios;

    public Servicios(Vector<Servicio> servicios) {
        this.servicios = servicios;
    }

    public Vector<Servicio> getServicios() {
        return servicios;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Servicio s : servicios) {
            sb.append(s.toString()).append("\n");
        }
        return sb.toString();
    }
}

