package p3b;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class Servidor1Impl extends UnicastRemoteObject implements Servidor1Interface {
    
    protected Servidor1Impl() throws RemoteException {
        super();
    }

    @Override
    public Respuesta ejecutarServicio(Vector parametros) throws RemoteException {
        System.out.println("Servidor 1: Ejecutando servicio con parámetros: " + parametros);
        return new Respuesta("Respuesta del Servicio de Servidor 1");
    }
}

