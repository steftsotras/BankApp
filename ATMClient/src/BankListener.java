//TSOTRAS STEFANOS
//ICSD13189


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Stefanos
 */
public interface BankListener extends Remote{
    
    public void update(String msg)throws RemoteException;
    
}
