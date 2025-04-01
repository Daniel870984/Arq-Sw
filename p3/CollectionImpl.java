// TODO : imports necesarios

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CollectionImpl extends UnicastRemoteObject
implements Collection
{
    // Private member variables
    private int m_number_of_books ;
    private String m_name_of_collection ;
    // Constructor
    public CollectionImpl () throws RemoteException
    {
        super () ; // Llama al constructor de UnicastRemoteObject
        m_number_of_books = 0;
        m_name_of_collection = "";
    }
    // TODO : Implementar todos los metodos de la interface remota
    public int number_of_books () throws RemoteException
    {
        return m_number_of_books;
    }

    public void number_of_books ( int _new_value ) throws RemoteException 
    {
        m_number_of_books = _new_value;
    }

    public String name_of_collection () throws RemoteException {
        return m_name_of_collection;
    }

    public void name_of_collection ( String _new_value ) throws RemoteException
    {
        m_name_of_collection = _new_value;
    }
<<<<<<< Updated upstream

    public static void main(String args[]) {
        try {
            System.setProperty("java.rmi.server.hostname", "10.1.26.0"); 
            String hostName = "10.1.26.0"; // Dirección del servidor
=======
    public static void main ( String args [])
    {
        // Nombre o IP del host donde reside el objeto servidor
        String hostName = "10.1.27.20"; // se puede usar "IPhostremoto : puerto "
        // Por defecto , RMI usa el puerto 1099
        try
        {
            // Crear objeto remoto
>>>>>>> Stashed changes
            CollectionImpl obj = new CollectionImpl();
            System.out.println(" Creado !");
            // Registrar el objeto remoto
            Naming.rebind("//" + hostName + "/MyCollection", obj);
            System.out.println(" Estoy registrado !");
        }
        catch ( Exception ex )
        {
            System.out.println(ex);
        }
    }
}