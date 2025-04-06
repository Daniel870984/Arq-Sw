
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * Clase principal para iniciar el servidor que ofrece el servicio de suma.
 */
public class SumaServer {
    public static void main(String[] args) {
        try {
            String serverName = "sumaServer"; // Se recomienda usar un nombre único (por ejemplo, incorporando el NIA)
            String brokerHost = "localhost"; // Se debe indicar la IP o hostname del Broker
            SumaService sumaService = new SumaServiceImpl(serverName, brokerHost);
            try {
                LocateRegistry.createRegistry(1099);
                System.out.println("Registro RMI iniciado en el puerto 1099.");
            } catch (Exception e) {
                System.out.println("El registro RMI ya está en ejecución.");
            }
            Naming.rebind(serverName, sumaService);
            System.out.println("SumaServer listo.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
