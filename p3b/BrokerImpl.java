import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class BrokerImpl extends UnicastRemoteObject implements BrokerInterface {

    // Mapa de servidores: nombre del servidor -> host remoto
    private Map<String, String> servidores = new HashMap<>();
    // Mapa de servicios: nombre del servicio -> objeto Servicio
    private Map<String, Servicio> servicios = new HashMap<>();
    // Mapa de solicitudes asíncronas: nombre del servicio -> objeto AsyncRequest
    private Map<String, AsyncRequest> asyncRequests = new HashMap<>();


    protected BrokerImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized void registrar_servidor(String serverName, String hostRemoto) throws RemoteException {
        this.servidores.put(serverName, hostRemoto);
        System.out.println("Servidor registrado: " + serverName + " en " + hostRemoto);
    }

    @Override
    public synchronized void alta_servicio(String serverName, String serviceName, Vector listaParametros, String tipoRetorno) throws RemoteException {
        if (!this.servidores.containsKey(serverName)) {
            System.out.println("Servidor no registrado: " + serverName);
        } else {
            // Se asume que serviceName es también el nombre del método a invocar en el objeto remoto.
            Servicio servicio = new Servicio(serverName, serviceName, listaParametros, tipoRetorno);
            this.servicios.put(serviceName, servicio);
            System.out.println("Servicio registrado: " + serviceName + " del servidor " + serverName);
        }
    }

    @Override
    public synchronized void baja_servicio(String serverName, String serviceName) throws RemoteException {
        Servicio servicio = this.servicios.get(serviceName);
        if (servicio != null && servicio.getNombreServidor().equals(serverName)) {
            this.servicios.remove(serviceName);
            System.out.println("Servicio dado de baja: " + serviceName + " del servidor " + serverName);
        } else {
            System.out.println("No se encontró el servicio o el servidor no coincide.");
        }
    }

    @Override
    public synchronized Servicios lista_servicios() throws RemoteException {
        return new Servicios(new Vector<>(this.servicios.values()));
    }

    @Override
    public Respuesta ejecutar_servicio(String serviceName, Vector parametros) throws RemoteException {
        Servicio servicio = this.servicios.get(serviceName);
        if (servicio == null) {
            return new Respuesta("Error: Servicio no encontrado");
        }

        String serverName = servicio.getNombreServidor();
        String host = this.servidores.get(serverName);
        if (host == null) {
            return new Respuesta("Error: Servidor no registrado");
        }

        try {
            // Se construye la URL RMI usando el host y el nombre del servidor.
            String url = "rmi://" + host + "/" + serverName;
            // Se obtiene el objeto remoto de forma genérica.
            Remote remoteObj = Naming.lookup(url);

            // Se extrae el nombre del método a invocar (por ejemplo, "sumar", "multiplicar", etc.)
            String methodName = servicio.getNombreServicio();

            // Se obtienen los tipos de parámetros esperados a partir de la lista registrada.
            // Se asume que la lista contiene strings como "int", "double", "String", etc.
            Vector parametrosMeta = servicio.getListaParametros();
            Class<?>[] parameterTypes = new Class<?>[parametrosMeta.size()];
            for (int i = 0; i < parametrosMeta.size(); i++) {
                String typeName = (String) parametrosMeta.get(i);
                if (typeName.equalsIgnoreCase("int")) {
                    parameterTypes[i] = int.class;
                } else if (typeName.equalsIgnoreCase("double")) {
                    parameterTypes[i] = double.class;
                } else if (typeName.equalsIgnoreCase("String")) {
                    parameterTypes[i] = String.class;
                } else {
                    parameterTypes[i] = Object.class;
                }
            }

            // Se obtiene, mediante reflexión, el método a invocar de la clase del objeto remoto.
            java.lang.reflect.Method m = remoteObj.getClass().getMethod(methodName, parameterTypes);

            // Se preparan los argumentos a partir del vector recibido (se asume que son compatibles).
            Object[] args = new Object[parametros.size()];
            for (int i = 0; i < parametros.size(); i++) {
                args[i] = parametros.get(i);
            }

            // Se invoca el método y se obtiene el resultado.
            Object result = m.invoke(remoteObj, args);
            return new Respuesta("Resultado: " + result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Respuesta("Error al ejecutar el servicio: " + e.getMessage());
        }
    }

    @Override
    public synchronized void ejecutar_servicio_asinc(String nom_servicio, Vector parametros, String clientId) throws RemoteException {
        // Evitar múltiples solicitudes para el mismo servicio sin haber recogido la respuesta previa
        if (asyncRequests.containsKey(nom_servicio)) {
            System.out.println("Error: Ya existe una solicitud asíncrona para el servicio " + nom_servicio);
            return;
        }
        // Crear la solicitud asíncrona con el clientId
        AsyncRequest request = new AsyncRequest(clientId);
        asyncRequests.put(nom_servicio, request);
    
        // Lanzar la ejecución en un hilo separado
        new Thread(() -> {
            try {
                Respuesta resp = ejecutar_servicio(nom_servicio, parametros);
                request.setRespuesta(resp);
            } catch (RemoteException e) {
                request.setRespuesta(new Respuesta("Error: " + e.getMessage()));
            }
        }).start();
    }

    @Override
    public synchronized Respuesta obtener_respuesta_asinc(String nom_servicio, String clientId) throws RemoteException {
        AsyncRequest request = asyncRequests.get(nom_servicio);
        if (request == null) {
            return new Respuesta("Error: No se realizó previamente la solicitud asíncrona para el servicio " + nom_servicio);
        }
        // Validar que el ID del cliente que solicita sea el mismo que realizó la petición
        if (!request.getClientId().equals(clientId)) {
            return new Respuesta("Error: El cliente que solicita la respuesta no es el mismo que hizo la petición");
        }
        if (request.isDelivered()) {
            return new Respuesta("Error: La respuesta ya fue entregada previamente para el servicio " + nom_servicio);
        }
        // Marcar la solicitud como entregada y eliminarla
        request.setDelivered(true);
        asyncRequests.remove(nom_servicio);
        return request.getRespuesta();
    }

}
