//TSOTRAS STEFANOS
//ICSD13189

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Stefanos
 */
public interface BankInterface extends Remote{
    
    public int validation(int PIN,int ID,BankListener clie)throws RemoteException;
    public boolean deposit(int amount,int ID)throws RemoteException;
    public boolean withdraw(int amount,int ID)throws RemoteException;
    public int info(int ID)throws RemoteException;
    public void exit(int ID)throws RemoteException;
            
}
