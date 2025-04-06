import java.rmi.Naming;
import java.util.Scanner;
import java.util.Vector;

public class Cliente {
    public static void main(String[] args) {
        try {
            String brokerHost = "localhost"; // IP o hostname donde se encuentra el Broker
            BrokerInterface broker = (BrokerInterface) Naming.lookup("rmi://" + brokerHost + "/broker");
            Scanner scanner = new Scanner(System.in);
            
            while (true) {
                System.out.println("Menú:");
                System.out.println("1. Listar servicios");
                System.out.println("2. Ejecutar servicio");
                System.out.println("3. Dar de alta un servicio");
                System.out.println("4. Dar de baja un servicio");
                System.out.println("5. Salir");
                System.out.print("Seleccione una opción: ");
                String opcion = scanner.nextLine();
                
                if (opcion.equals("5")) {
                    break;
                }
                
                switch (opcion) {
                    case "1":
                        System.out.println("----- Lista de servicios disponibles -----");
                        Servicios lista = broker.lista_servicios();
                        System.out.println(lista);
                        break;
                        
                    case "2":
                        System.out.print("Ingrese el nombre del servicio a ejecutar: ");
                        String nomServicio = scanner.nextLine();
                        // Asumimos que el servicio requiere dos enteros
                        System.out.print("Ingrese el primer entero: ");
                        int a = Integer.parseInt(scanner.nextLine());
                        System.out.print("Ingrese el segundo entero: ");
                        int b = Integer.parseInt(scanner.nextLine());
                        Vector parametros = new Vector();
                        parametros.add(a);
                        parametros.add(b);
                        Respuesta resp = broker.ejecutar_servicio(nomServicio, parametros);
                        System.out.println("Respuesta: " + resp);
                        break;
                        
                    case "3":
                        // Dar de alta un servicio
                        System.out.print("Ingrese el nombre del servidor para el servicio: ");
                        String nomServidorAlta = scanner.nextLine();
                        System.out.print("Ingrese el nombre del servicio a dar de alta: ");
                        String nomServicioAlta = scanner.nextLine();
                        System.out.print("Ingrese el número de parámetros que requiere el servicio: ");
                        int numParams = Integer.parseInt(scanner.nextLine());
                        Vector paramsTipos = new Vector();
                        for (int i = 0; i < numParams; i++) {
                            System.out.print("Ingrese el tipo del parámetro " + (i + 1) + ": ");
                            String tipoParam = scanner.nextLine();
                            paramsTipos.add(tipoParam);
                        }
                        System.out.print("Ingrese el tipo de retorno del servicio: ");
                        String tipoRetorno = scanner.nextLine();
                        broker.alta_servicio(nomServidorAlta, nomServicioAlta, paramsTipos, tipoRetorno);
                        System.out.println("Servicio dado de alta exitosamente.");
                        break;
                        
                    case "4":
                        // Dar de baja un servicio
                        System.out.print("Ingrese el nombre del servidor del servicio: ");
                        String nomServidorBaja = scanner.nextLine();
                        System.out.print("Ingrese el nombre del servicio a dar de baja: ");
                        String nomServicioBaja = scanner.nextLine();
                        broker.baja_servicio(nomServidorBaja, nomServicioBaja);
                        System.out.println("Servicio dado de baja exitosamente.");
                        break;
                        
                    default:
                        System.out.println("Opción no válida.");
                        break;
                }
                System.out.println(); // línea en blanco para separación
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
