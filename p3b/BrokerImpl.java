import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.rmi.Naming;

/**
 * Implementación del Broker. Se encarga de registrar servidores y servicios,
 * listar los servicios disponibles y ejecutar un servicio solicitándolo al servidor
 * correspondiente.
 */
public class BrokerImpl extends UnicastRemoteObject implements BrokerInterface {

    // Mapa de servidores: nombreServidor -> host remoto (IP o hostname)
    private Map<String, String> servidores;
    // Mapa de servicios: nombreServicio -> objeto Servicio
    private Map<String, Servicio> servicios;

    protected BrokerImpl() throws RemoteException {
        super();
        servidores = new HashMap<>();
        servicios = new HashMap<>();
    }

    @Override
    public synchronized void registrar_servidor(String nombre_servidor, String host_remoto_IP_puerto) throws RemoteException {
        servidores.put(nombre_servidor, host_remoto_IP_puerto);
        System.out.println("Servidor registrado: " + nombre_servidor + " en " + host_remoto_IP_puerto);
    }

    @Override
    public synchronized void alta_servicio(String nombre_servidor, String nom_servicio, Vector lista_param, String tipo_retorno) throws RemoteException {
        if (!servidores.containsKey(nombre_servidor)) {
            System.out.println("Servidor no registrado: " + nombre_servidor);
            return;
        }
        Servicio servicio = new Servicio(nombre_servidor, nom_servicio, lista_param, tipo_retorno);
        servicios.put(nom_servicio, servicio);
        System.out.println("Servicio registrado: " + nom_servicio + " del servidor " + nombre_servidor);
    }

    @Override
    public synchronized void baja_servicio(String nombre_servidor, String nom_servicio) throws RemoteException {
        Servicio servicio = servicios.get(nom_servicio);
        if (servicio != null && servicio.getNombreServidor().equals(nombre_servidor)) {
            servicios.remove(nom_servicio);
            System.out.println("Servicio dado de baja: " + nom_servicio + " del servidor " + nombre_servidor);
        } else {
            System.out.println("No se encontró el servicio o el servidor no coincide.");
        }
    }

    @Override
    public synchronized Servicios lista_servicios() throws RemoteException {
        return new Servicios(new Vector<>(servicios.values()));
    }

    @Override
    public Respuesta ejecutar_servicio(String nom_servicio, Vector parametros_servicio) throws RemoteException {
        Servicio servicio = servicios.get(nom_servicio);
        if (servicio == null) {
            return new Respuesta("Error: Servicio no encontrado");
        }
        String nombreServidor = servicio.getNombreServidor();
        String host = servidores.get(nombreServidor);
        if (host == null) {
            return new Respuesta("Error: Servidor no registrado");
        }
        try {
            // Se construye la URL RMI del servidor (se asume que el objeto remoto está
            // enlazado con el mismo nombre que el servidor).
            String url = "rmi://" + host + "/" + nombreServidor;
            
            /* 
             * Para la demostración se intenta primero buscar un servicio de suma y luego de resta.
             * En una implementación real se podría usar reflexión o un mecanismo de invocación
             * dinámica más general.
             */
            
            try {
                SumaService sumaService = (SumaService) Naming.lookup(url);
            if (nom_servicio.equalsIgnoreCase("sumar")) {
                int a = (Integer) parametros_servicio.get(0);
                int b = (Integer) parametros_servicio.get(1);
                int result = sumaService.sumar(a, b);
                return new Respuesta("Resultado: " + result);
            } else if (nom_servicio.equalsIgnoreCase("multiplicar")) {
                int a = (Integer) parametros_servicio.get(0);
                int b = (Integer) parametros_servicio.get(1);
                int result = sumaService.multiplicar(a, b);
                return new Respuesta("Resultado: " + result);
            }
            } catch (Exception e) {
                // Se ignora para probar con la siguiente interfaz
            }
            try {
                RestaService restaService = (RestaService) Naming.lookup(url);
                if (nom_servicio.equalsIgnoreCase("restar")) {
                    int a = (Integer) parametros_servicio.get(0);
                    int b = (Integer) parametros_servicio.get(1);
                    int result = restaService.restar(a, b);
                    return new Respuesta("Resultado: " + result);
                }
            } catch (Exception e) {
                // No se encontró la interfaz correspondiente
            }
            
            return new Respuesta("Error: Servicio no ejecutable");
        } catch (Exception ex) {
            ex.printStackTrace();
            return new Respuesta("Error al ejecutar el servicio: " + ex.getMessage());
        }
    }
}
