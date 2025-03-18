import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;


public class CollectionImpl extends UnicastRemoteObject implements Collection {
    private int m_number_of_books;
    private String m_name_of_collection;

    public CollectionImpl() throws RemoteException {
        super(); // Llama al constructor de UnicastRemoteObject
        m_number_of_books = 0;
        m_name_of_collection = "";
    }

    public int number_of_books() throws RemoteException {
        return m_number_of_books;
    }

    public String name_of_collection() throws RemoteException {
        return m_name_of_collection;
    }

    public void name_of_collection(String _new_value) throws RemoteException {
        m_name_of_collection = _new_value;
    }

    public static void main(String args[]) {
        try {
            String hostName = "localhost"; // Dirección del servidor
            CollectionImpl obj = new CollectionImpl();
            Naming.rebind("//" + hostName + "/MyCollection", obj);
            System.out.println("Objeto remoto registrado.");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
