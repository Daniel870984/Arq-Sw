
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz remota del servicio de suma.
 */
public interface SumaService extends Remote {
    public int sumar(int a, int b) throws RemoteException;
    public int multiplicar(int a, int b) throws RemoteException; // Nuevo método para multiplicar

}

