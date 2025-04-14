
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * Clase principal para iniciar el servidor que ofrece el servicio de resta.
 */
public class RestaServer {
    public static void main(String[] args) {
        try {
            String serverName = "restaServer";
            String brokerHost = "155.210.154.200"; // IP o hostname donde se encuentra el Broker
            RestaService restaService = new RestaServiceImpl(serverName, brokerHost);
            try {
                LocateRegistry.createRegistry(1099);
                System.out.println("Registro RMI iniciado en el puerto 1099.");
            } catch (Exception e) {
                System.out.println("El registro RMI ya está en ejecución.");
            }
            Naming.rebind(serverName, restaService);
            System.out.println("RestaServer listo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

