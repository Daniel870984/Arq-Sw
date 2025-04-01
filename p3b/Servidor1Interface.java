package p3b;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface Servidor1Interface extends Remote {
    // Método remoto para ejecutar un servicio con parámetros.
    Respuesta ejecutarServicio(Vector parametros) throws RemoteException;
}
