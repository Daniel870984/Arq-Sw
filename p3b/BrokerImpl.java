package p3b;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class BrokerImpl extends UnicastRemoteObject implements BrokerInterface {

    // Constructor obligatorio que lanza RemoteException
    protected BrokerImpl() throws RemoteException {
        super();
    }

    @Override
    public void registrarServidor(String nombreServidor, String hostRemoto) throws RemoteException {
        // Lógica para registrar el servidor
        System.out.println("Servidor registrado: " + nombreServidor + " en " + hostRemoto);
    }

    @Override
    public void altaServicio(String nombreServidor, String nomServicio, Vector listaParam, String tipoRetorno) throws RemoteException {
        // Lógica para dar de alta un servicio
        System.out.println("Servicio dado de alta: " + nomServicio + " por " + nombreServidor);
    }

    @Override
    public void bajaServicio(String nombreServidor, String nomServicio) throws RemoteException {
        // Lógica para dar de baja un servicio
        System.out.println("Servicio dado de baja: " + nomServicio + " por " + nombreServidor);
    }

    @Override
    public Servicios listaServicios() throws RemoteException {
        // Por el momento, se devuelve una lista vacía de servicios.
        return new Servicios();
    }

    @Override
    public Respuesta ejecutarServicio(String nomServicio, Vector parametrosServicio) throws RemoteException {
        // Lógica para ejecutar el servicio solicitado (dummy)
        System.out.println("Ejecutando servicio: " + nomServicio);
        return new Respuesta("Resultado de " + nomServicio);
    }
    
    // Métodos asíncronos se pueden implementar posteriormente.
}
