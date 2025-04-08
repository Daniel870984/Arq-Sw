
import java.io.Serializable;
import java.util.Vector;

/**
 * Clase que describe un servicio ofrecido por un servidor.
 */
public class Servicio implements Serializable {
    private String nombreServidor;
    private String nombreServicio;
    private Vector listaParametros;
    private String tipoRetorno;

    public Servicio(String nombreServidor, String nombreServicio, Vector listaParametros, String tipoRetorno) {
        this.nombreServidor = nombreServidor;
        this.nombreServicio = nombreServicio;
        this.listaParametros = listaParametros;
        this.tipoRetorno = tipoRetorno;
    }

    public String getNombreServidor() {
        return nombreServidor;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public Vector getListaParametros() {
        return listaParametros;
    }

    public String getTipoRetorno() {
        return tipoRetorno;
    }

    @Override
    public String toString() {
        return "Servicio{" +
                "nombreServicio='" + nombreServicio + '\'' +
                ", nombreServidor='" + nombreServidor + '\'' +
                ", listaParametros=" + listaParametros +
                ", tipoRetorno='" + tipoRetorno + '\'' +
                '}';
    }
}
