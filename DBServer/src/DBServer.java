//TSOTRAS STEFANOS
//ICSD13189


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stefanos
 */
public class DBServer extends UnicastRemoteObject implements DBInterface{

    
    private Connection conn;
    private Statement stat;
    
    public static void main(String[] args) throws RemoteException {
        DBServer server;
                
        try {
            server = new DBServer ();

            Registry r = java.rmi.registry.LocateRegistry.createRegistry(1099);

            Naming.rebind("//localhost/DBServer", server);
            System.out.println("Waiting new Messages");
            
            //System.out.println(server.getAccountNumber(0));
            
            
        } catch (RemoteException ex) {
        ex.printStackTrace();
        } catch (MalformedURLException ex) {
        ex.printStackTrace();
        }
        
        
    }
    
    public DBServer()throws RemoteException{
        super();
        try{
            
            //SUNDESH ME TH BASH
            Class.forName("org.sqlite.JDBC");
            
            conn = DriverManager.getConnection("jdbc:sqlite:bankDB.db");
            
            stat = conn.createStatement();
            System.out.println ("Database connection established");
            
            //DHMIOURGIA TOU PINAKA KAI EISAGWGH EGGRAFWN
            stat.executeUpdate("DROP table if exists account;");
            
            stat.executeUpdate("CREATE table account (PIN int, AccountNo int, ID int, Balance int);");
            
            stat.executeUpdate("INSERT INTO account(PIN, AccountNo, ID, Balance) VALUES ('100','1000','3212','10000')");
            stat.executeUpdate("INSERT INTO account(PIN, AccountNo, ID, Balance) VALUES ('200','2000','1122','5000')");
            stat.executeUpdate("INSERT INTO account(PIN, AccountNo, ID, Balance) VALUES ('201','2000','2451','5000')");
            stat.executeUpdate("INSERT INTO account(PIN, AccountNo, ID, Balance) VALUES ('300','3000','7891','8000')");
            stat.executeUpdate("INSERT INTO account(PIN, AccountNo, ID, Balance) VALUES ('300','3000','1966','8000')");
            
                    
            
            
        } catch (ClassNotFoundException ex) {
        Logger.getLogger(DBServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
        Logger.getLogger(DBServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }

    @Override
    public int getAccountNumber(int PIN,int ID) throws RemoteException {
        
        ResultSet records=null;
        
        try{
            //PERASMA TOU ARITHMOU LOGARIASMOU ME TO SUGKEKRIMENO PIN
            records = stat.executeQuery("SELECT AccountNo FROM account WHERE PIN='"+PIN+"' AND ID='"+ID+"'");
            
            if (records.next()) {
                return records.getInt("AccountNo");
            //AN TO RESULT SET EINAI ADEIO EPISTREFEI 1
            } else {
                return 1;
            }
        } catch (SQLException ex) {
        Logger.getLogger(DBServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //SE KATHE ALLH PERIPTWSH EPISTROFH 0
        return 0;
    }

    @Override
    public int getBalance(int ID) throws RemoteException {
        ResultSet records=null;
        
        try{
            records = stat.executeQuery("SELECT Balance FROM account WHERE ID='"+ID+"'");
            
            if (records.next()) {
                
                 return records.getInt("Balance");
                
            }else {
                return 0;
            }
        } catch (SQLException ex) {
        Logger.getLogger(DBServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
        
    }
    
    @Override
    public boolean setBalance(int ID,int amount, boolean action) throws RemoteException {
        
        ResultSet records=null;
        
        try{
            records = stat.executeQuery("SELECT Balance FROM account WHERE ID='"+ID+"'");

            if (records.next()) {
                int balance = records.getInt("Balance");
                //TO ACTION EINAI GIA NA DIAXWRHSTEI H KATATHESH(FALSE) ME THN ANALHPSH(TRUE)
                if(action){
                    if(balance < amount){
                        return false;
                    }
                    else{
                        stat.executeUpdate("UPDATE account SET Balance = '"+(balance-amount)+"'");
                        return true;
                    }
                }
                else{
                    stat.executeUpdate("UPDATE account SET Balance = '"+(amount+balance)+"'");
                    return true;
                }
            }else{
                return false;
            }
        } catch (SQLException ex) {
        Logger.getLogger(DBServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    
    
    
    
}
