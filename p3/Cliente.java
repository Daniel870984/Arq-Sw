import java.rmi.Naming;
// TODO : imports necesarios

public class Cliente
{
    public static void main ( String [] args ) {
        try
        {
            // Paso 1 - Obtener una referencia al objeto servidor creado anteriormente
            // Nombre del host servidor o su IP. Es dรณ nde se buscar รก al objeto remoto
            String hostname = "localhost"; // se puede usar "IP: puerto "x
            Collection server = ( Collection )Naming.lookup("//"+ hostname + "/MyCollection") ;
            // Paso 2 - Invocar remotamente los metodos del objeto servidor :
            int numberBooks = server.number_of_books();
            String nameCollection = server.name_of_collection();
            System.out.println("Hay " + numberBooks + " y el nombre la coleccion es " + nameCollection);
            
            server.number_of_books(10);
            server.name_of_collection("Raul bobolon");
            numberBooks = server.number_of_books();
            nameCollection = server.name_of_collection();
            System.out.println("Despues de los cambios: Hay " + numberBooks + " y el nombre la coleccion es " + nameCollection);
        }
        catch ( Exception ex )
        {
            System . out . println ( ex );
        }
    }
}