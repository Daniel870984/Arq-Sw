import java . rmi . Remote ;
import java . rmi . RemoteException ;
// TODO : otros imports
public interface Collection extends Remote
{
    // MÃ© todos de la interfaz
    int number_of_books () throws RemoteException ;
    void number_of_books ( int _new_value ) throws RemoteException;
    String name_of_collection () throws RemoteException ;
    void name_of_collection ( String _new_value ) throws RemoteException;
}