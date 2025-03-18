import java.rmi.Naming;

public class Cliente {
    public static void main(String[] args) {
        try {
            // Dirección del servidor
            String hostname = "localhost";
            Collection server = (Collection) Naming.lookup("//" + hostname + "/MyCollection");

            // Invocar métodos remotos
            System.out.println("Número de libros: " + server.number_of_books());
            server.name_of_collection("Nueva Colección");
            System.out.println("Nombre de la colección: " + server.name_of_collection());
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
