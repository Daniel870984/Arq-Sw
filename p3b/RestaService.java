
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz remota del servicio de resta.
 */
public interface RestaService extends Remote {
    public int restar(int a, int b) throws RemoteException;
}
