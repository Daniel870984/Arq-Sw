import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

/**
 * Interfaz remota del Broker.
 */
public interface BrokerInterface extends Remote {
    // API para los servidores
    void registrar_servidor(String nombre_servidor, String host_remoto_IP_puerto) throws RemoteException;
    void alta_servicio(String nombre_servidor, String nom_servicio, Vector lista_param, String tipo_retorno) throws RemoteException;
    void baja_servicio(String nombre_servidor, String nom_servicio) throws RemoteException;
    
    // API para los clientes
    Servicios lista_servicios() throws RemoteException;
    Respuesta ejecutar_servicio(String nom_servicio, Vector parametros_servicio) throws RemoteException;
    void ejecutar_servicio_asinc(String nom_servicio, Vector parametros_servicio, String clientId) throws RemoteException;
    Respuesta obtener_respuesta_asinc(String nom_servicio, String clientId) throws RemoteException;
}


