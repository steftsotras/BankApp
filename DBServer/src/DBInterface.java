//TSOTRAS STEFANOS
//ICSD13189

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Stefanos
 */
public interface DBInterface extends Remote{
    
    public int getAccountNumber(int PIN,int ID)throws RemoteException;
    public int getBalance(int ID)throws RemoteException;
    public boolean setBalance(int ID,int amount,boolean action)throws RemoteException;
}
