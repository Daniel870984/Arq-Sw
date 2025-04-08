
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * Clase principal para iniciar el Broker.
 */
public class BrokerServer {
    public static void main(String[] args) {
        try {
            // Se crea la instancia del Broker y se exporta
            BrokerInterface broker = new BrokerImpl();
            // Se intenta iniciar el registro RMI en el puerto 1099
            try {
                LocateRegistry.createRegistry(1099);
                System.out.println("Registro RMI iniciado en el puerto 1099.");
            } catch (Exception e) {
                System.out.println("El registro RMI ya está en ejecución.");
            }
            // Se enlaza el Broker con el nombre "broker"
            Naming.rebind("broker", broker);
            System.out.println("Broker listo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

