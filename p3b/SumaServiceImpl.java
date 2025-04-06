import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.rmi.Naming;

/**
 * Implementación del servicio de suma.
 * Al iniciar, se registra en el Broker dando de alta el servicio "sumar".
 */
public class SumaServiceImpl extends UnicastRemoteObject implements SumaService {

    private String serverName;
    private String brokerHost;

    protected SumaServiceImpl(String serverName, String brokerHost) throws RemoteException {
        super();
        this.serverName = serverName;
        this.brokerHost = brokerHost;
        registrarEnBroker();
    }

    /**
     * Registra el servidor y el servicio "sumar" en el Broker.
     */
    private void registrarEnBroker() {
        try {
            BrokerInterface broker = (BrokerInterface) Naming.lookup("rmi://" + brokerHost + "/broker");
            // En este ejemplo se asume que el servicio se publica en la máquina local.
            String hostInfo = "172.20.0.11";
            broker.registrar_servidor(serverName, hostInfo);
            // Se registra el servicio "sumar" con dos parámetros de tipo entero
            Vector params = new Vector();
            params.add("int");
            params.add("int");
            broker.alta_servicio(serverName, "sumar", params, "int");
            System.out.println("Servicio 'sumar' registrado en el Broker.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int sumar(int a, int b) throws RemoteException {
        System.out.println("Ejecutando sumar(" + a + ", " + b + ")");
        return a + b;
    }

    @Override
    public int multiplicar(int a, int b) throws RemoteException {
        System.out.println("Ejecutando multiplicar(" + a + ", " + b + ")");
        return a * b;
    }
}
