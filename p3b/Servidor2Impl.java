package p3b;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Servidor2Impl extends UnicastRemoteObject implements Servidor2Interface {
    
    protected Servidor2Impl() throws RemoteException {
        super();
    }

    @Override
    public Respuesta metodoTonto() throws RemoteException {
        System.out.println("Servidor 2: Método tonto ejecutado.");
        return new Respuesta("Respuesta del método tonto de Servidor 2");
    }
}

