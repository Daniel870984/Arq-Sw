package p3b;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface BrokerInterface extends Remote {
    // API para los servidores:
    void registrarServidor(String nombreServidor, String hostRemoto) throws RemoteException;
    void altaServicio(String nombreServidor, String nomServicio, Vector listaParam, String tipoRetorno) throws RemoteException;
    void bajaServicio(String nombreServidor, String nomServicio) throws RemoteException;
    
    // API para los clientes:
    Servicios listaServicios() throws RemoteException;
    Respuesta ejecutarServicio(String nomServicio, Vector parametrosServicio) throws RemoteException;
    
    // Para la versión asíncrona (a implementar en el futuro):
    // void ejecutarServicioAsinc(String nomServicio, Vector parametrosServicio) throws RemoteException;
    // Respuesta obtenerRespuestaAsinc(String nomServicio) throws RemoteException;
}

