//TSOTRAS STEFANOS
//ICSD13189

import java.net.*;
import java.rmi.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefanos
 */
public class BankServer  extends UnicastRemoteObject implements BankInterface{

    
    private HashMap<Integer,BankListener> clients;
    BankListener client;
    private String msg;
    
    public static void main(String[] args) {
        //RMISecurityManager security = new RMISecurityManager();
        //System.setSecurityManager(security);
        try{
        
        //ANOIGMA SUNDESHS STHN PORTA 1098
        BankServer server = new BankServer();    
            
        Registry r = java.rmi.registry.LocateRegistry.createRegistry(1098);
     
        Naming.rebind("//localhost:1098/BankServer", server);
        System.out.println("Waiting new Messages");
        
        
        } catch (RemoteException ex) {
        ex.printStackTrace();
        } catch (MalformedURLException ex) {
        ex.printStackTrace();
        }
        
    }
    
    public BankServer() throws RemoteException{
        super();
        
        //H HASH MAP AYTH KRATAEI SAN KLEIDI TO ID TOU XRHSTH
        //KAI SAN TIMH TO ANTIKEIMENO TOU ETSI WSTE NA KSERW
        //MPORW NA VRW EXWNTAS TO ID POIOS SYGKEKRIMENOS XRHSTHS
        //EINAI KAI NA TOU STELNW MYNHMA
        clients = new HashMap<Integer,BankListener>();
    }
    
    //H METHODOS AYTH EPISTREFEI TO ANTIKEIMENO TOU XRHSTH ME ID 
    public BankListener ReturnClient(int ID){
        for(Map.Entry<Integer,BankListener> entry:clients.entrySet()){
            if(entry.getKey().equals(ID)){
                return entry.getValue();
            }
        }
        return null;
    }
    

    @Override
    public int validation(int PIN, int ID,BankListener clie) throws RemoteException {
        
        //AN YPARXEI STO HASH MAP EXEI KANEI VALIDATION
        //ENHMERWSH XRHSTH ME ANALOGO MYNHMA KAI KWDIKO LATHOUS
        client = ReturnClient(ID);
        if(client != null){
            msg = "Validation has already been done! Error Code :";
            client.update(msg);
            return 0;
        }
        
        //Establishing connection with dbserver
        try {
            
            //SYNDESH ME TON SERVER THS BASHS
            DBInterface look_up = (DBInterface) Naming.lookup("//localhost/DBServer");
            
            //EPISTROFH TOU ARITHMOU LOGARIASMOU APTH BASH
            int result = look_up.getAccountNumber(PIN,ID);
            
            //PERIPTWSHS ANALOGA ME THN EPISTROFH
            //KAI ENHMERWSH TOY XRHSTH
            if(result == 0){
                msg = "Something went wrong";
                clie.update(msg);
                return 1;
            }else if(result == 1){
                msg = "Wrong PIN on specified ID! Error Code :";
                clie.update(msg);
                return 2;
            }else{
                clients.put(ID, clie);
                msg = "Success! Your Account Number is :";
                clie.update(msg);
                return result;
            }
            
            
        } catch (NotBoundException ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return 1;
    }

    @Override
    public synchronized boolean deposit(int amount, int ID) throws RemoteException {

        
        //Establishing connection with dbserver
        try {
            DBInterface look_up = (DBInterface) Naming.lookup("//localhost/DBServer");
            
            boolean result = look_up.setBalance(ID,amount,false);
            
            client = ReturnClient(ID);
            
            if(!result){
                msg = "Something went wrong";
                client.update(msg);
                return false;
            }else{
                msg = "Success";
                client.update(msg);
                return result;
            }
            
            
        } catch (NotBoundException ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
        
    }

    @Override
    public synchronized boolean withdraw(int amount, int ID) throws RemoteException {

        
        //Establishing connection with dbserver
        try {
            DBInterface look_up = (DBInterface) Naming.lookup("//localhost/DBServer");
            
            boolean result = look_up.setBalance(ID,amount,true);
            client = ReturnClient(ID);
            if(!result){
                msg = "You dont have enough money in your account";
                client.update(msg);
                return false;
            }else{
                msg = "Success";
                client.update(msg);
                return result;
            }
            
            
        } catch (NotBoundException ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
        
    }

    @Override
    public int info(int ID) throws RemoteException {
        
        
        //Establishing connection with dbserver
        try {
            DBInterface look_up = (DBInterface) Naming.lookup("//localhost/DBServer");
            
            int result = look_up.getBalance(ID);
            client = ReturnClient(ID);
            if(result == 0){
                msg = "Something went wrong Error Code:";
                client.update(msg);
                return 1;
            }else{
                msg = "Your balance is";
                client.update(msg);
                return result;
            }
            
            
        } catch (NotBoundException ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return 1;
        
    }
    
    @Override
    public void exit(int ID) throws RemoteException {
        //DIAGRAFH TOU XRHSTH APTO HASHMAP
        //https://stackoverflow.com/questions/1884889/iterating-over-and-removing-from-a-map
        for(Iterator<Map.Entry<Integer,BankListener>> it = clients.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Integer,BankListener> entry = it.next();
            if(entry.getKey().equals(ID)) {
              it.remove();
            }
        }
    }
    
    
}
