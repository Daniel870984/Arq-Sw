
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.rmi.Naming;

/**
 * Implementación del servicio de resta.
 * Al iniciar, se registra en el Broker dando de alta el servicio "restar".
 */
public class RestaServiceImpl extends UnicastRemoteObject implements RestaService {

    private String serverName;
    private String brokerHost;

    protected RestaServiceImpl(String serverName, String brokerHost) throws RemoteException {
        super();
        this.serverName = serverName;
        this.brokerHost = brokerHost;
        registrarEnBroker();
    }

    /**
     * Registra el servidor y el servicio "restar" en el Broker.
     */
    private void registrarEnBroker() {
        try {
            BrokerInterface broker = (BrokerInterface) Naming.lookup("rmi://" + brokerHost + "/broker");
            String hostInfo = "172.20.0.11";
            broker.registrar_servidor(serverName, hostInfo);
            // Se registra el servicio "restar" con dos parámetros de tipo entero
            Vector params = new Vector();
            params.add("int");
            params.add("int");
            broker.alta_servicio(serverName, "restar", params, "int");
            System.out.println("Servicio 'restar' registrado en el Broker.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int restar(int a, int b) throws RemoteException {
        System.out.println("Ejecutando restar(" + a + ", " + b + ")");
        return a - b;
    }
}
