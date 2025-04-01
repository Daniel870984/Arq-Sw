package p3b;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Servidor2Interface extends Remote {
    // Método remoto adicional para simular una funcionalidad extra.
    Respuesta metodoTonto() throws RemoteException;
}
