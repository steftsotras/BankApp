//TSOTRAS STEFANOS
//ICSD13189

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Stefanos
 */

public class ATMClient extends UnicastRemoteObject implements ActionListener,BankListener{

    
    //DHLWSH METABLHTWN
    
    //JCOMPONENTS
    private JFrame frame;
    private JPanel mainpanel,wholepanel,labelpanel;
    private JButton validation,deposit,withdrawal,information,exit;
    private JLabel label,labelstr,labelval;
    
    //ALLA
    private int ID;
    private BankInterface look_up;
    
    
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        new ATMClient();
    }

    //CONSTRUCTOR
    public ATMClient()throws RemoteException, NotBoundException, MalformedURLException{
        super();
        
        //RMISecurityManager security = new RMISecurityManager();
        //System.setSecurityManager(security);
        
        //SUNDESH
        look_up= (BankInterface) Naming.lookup("//localhost:1098/BankServer");
        
        
        //GRAFIKO PERIVALON 
        frame = new JFrame();
        
        //DHMIOYRGIA TWN APARAITHTWN PANEL
        mainpanel = new JPanel();
        wholepanel = new JPanel();
        labelpanel = new JPanel();
        
        //DHMIOYRGIA JLABEL
        label = new JLabel("Choose an Action");
        labelpanel.add(label);
         
        //DHMIOYRGIA KAI ORISMA LAYOUT
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints g = new GridBagConstraints();
        mainpanel.setLayout(gbl);
         
        //ORISMA ORIZONTIOU METAVLYTOY MHKOYS KAI TWN APOSTASEWN METAKSY TWN STOIXEIWN
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(20,20,20,20);
         
        //DHMIOURGIA TWN BUTTONS KAI ORISMOS TOYS MESA STO PANEL
        //ME BASH TO GridBagConstraints LAYOUT
        //PROSTHESH ACTIONLISTENER
        validation = new JButton("Validate Card");
        g.gridx = 0;
        g.gridy = 1;
        gbl.setConstraints(validation, g);
        mainpanel.add(validation);
        validation.addActionListener(this);
         
        deposit = new JButton("Deposit");
        g.gridx = 1;
        g.gridy = 1;
        gbl.setConstraints(deposit, g);
        mainpanel.add(deposit);
        deposit.addActionListener(this);
         
        withdrawal = new JButton("Withdraw");
        g.gridx = 0;
        g.gridy = 2;
        gbl.setConstraints(withdrawal, g);
        mainpanel.add(withdrawal);
        withdrawal.addActionListener(this);
         
        information = new JButton("Account info");
        g.gridx = 1;
        g.gridy = 2;
        gbl.setConstraints(information, g);
        mainpanel.add(information);
        information.addActionListener(this);
         
        exit = new JButton("Exit");
        g.gridx = 1;
        g.gridy = 3;
        gbl.setConstraints(exit, g);
        mainpanel.add(exit);
        exit.addActionListener(this);
         
        
        //ORISMOS LAYOUT TOU SUNOLIKOU PANEL POU PERIEXEI TA ALLA DYO ΟΡΙΣΜΟΣ LAYOUT ΤΟΥ ΣΥΝΟΛΙΚΟΥ PANEL ΠΟΥ ΠΕΡΙΕΧΕΙ ΤΑ ΑΛΛΑ ΔΥΟ
        //WS BOXLAYOUT GIA NA MPOYN TO ENA KATW APTO ALLO
        BoxLayout bl = new BoxLayout(wholepanel,BoxLayout.Y_AXIS);
        wholepanel.setLayout(bl);
        
        //DHMIOURGIA KENOY BORDER GIA THN APOSTASH TOY PRWTOY PANEL APTO ORIO TOU FRAME
        javax.swing.border.Border padding = BorderFactory.createEmptyBorder(20, 0, 0, 0);
        labelpanel.setBorder(padding);
        
        //PROSTHESH TWN PANEL STO TELIKO PANEL
        wholepanel.add(labelpanel);
        wholepanel.add(mainpanel);
        
        //PROSTHESH TOU TELIKOU PANEL STO FRAME
        frame.add(wholepanel);
        
        //SYNARTHSEIS GIA TO FRAME
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        
        labelstr = new JLabel();
        labelval = new JLabel();
        ID=0;
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        
        
        
        try {
            
            //VALIDATION
            if(e.getSource() == validation){
                
                String strID = "";
                String PIN= "";
                
                //DIAVASMA TOU PIN KAI ID THS KARTAS
                do{
                    PIN = JOptionPane.showInputDialog(frame, "Enter PIN code", "Validation", JOptionPane.PLAIN_MESSAGE);
                    strID = JOptionPane.showInputDialog(frame, "Enter card ID", "Card ID", JOptionPane.PLAIN_MESSAGE);
                }while(strID.equals("") || PIN.equals(""));
                
                //TO ID APOTHIKEUETAI GIA XRHSH STIS ALLES LEITOURGIES
                ID = Integer.valueOf(strID);
                
                //KALESMA THS SYNARTHSHS MESW THS SYNDESHS KAI APOTHIKEUSH TOU APOTELESMATOS SE LABEL
                labelval.setText(String.valueOf(look_up.validation(Integer.valueOf(PIN), ID,this)));
                
                //AN EPISTRAFEI 1 H 2 SHMAINEI OTI EITE EGINE LATHOS
                //H OTI O XRHSTHS EDWSE LATHOS PIN H ID
                if(labelval.getText().equals("2") || labelval.getText().equals("1")){
                    //ARA TO ID DEN KATAXWRHTE
                    ID =0;
                }
                
                //EMFANISH APOTELESMATOS
                JLabel[]arr = {labelstr,labelval};
                
                JOptionPane.showConfirmDialog(frame,arr, "Result",JOptionPane.OK_CANCEL_OPTION ,JOptionPane.PLAIN_MESSAGE );

                
            }
            else if(e.getSource() == deposit){
                
                //AN TO ID EINAI AKOMA 0 SHMAINEI OTI DEN EXEI GINEI SWSTH ANAGNWRHSH AKOMA
                if(ID == 0){
                    JOptionPane.showMessageDialog(frame, "Validate your card first!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    
                }
                else{
                    
                    String amount="";

                    do{
                        amount = JOptionPane.showInputDialog(frame, "Enter amount to deposit", "Deposit", JOptionPane.PLAIN_MESSAGE);
                    }while(amount.equals("") || Integer.valueOf(amount)<=0);

                    if(look_up.deposit(Integer.valueOf(amount),ID)){
                        labelval.setText("Amount deposited");
                    }else{
                        labelval.setText("");
                    }


                    JLabel[]arr = {labelstr,labelval};

                    JOptionPane.showConfirmDialog(frame,arr, "Result",JOptionPane.OK_CANCEL_OPTION ,JOptionPane.PLAIN_MESSAGE );
                }
                
            }
            else if(e.getSource() == withdrawal){
                
                if(ID == 0){
                    JOptionPane.showMessageDialog(frame, "Validate your card first!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    
                }
                else{
                    String amount="";

                    do{
                        amount = JOptionPane.showInputDialog(frame, "Enter amount to withdraw", "Withdrawal", JOptionPane.PLAIN_MESSAGE);
                    }while(amount.equals("") || Integer.valueOf(amount)<=0);


                    if(look_up.withdraw(Integer.valueOf(amount),ID)){
                        labelval.setText("Amount withdrawn");
                    }else{
                        labelval.setText("");
                    }

                    JLabel[]arr = {labelstr,labelval};

                    JOptionPane.showConfirmDialog(frame,arr, "Result",JOptionPane.OK_CANCEL_OPTION ,JOptionPane.PLAIN_MESSAGE );

                }
                
            }
            else if(e.getSource() == information){
                
                if(ID == 0){
                    JOptionPane.showMessageDialog(frame, "Validate your card first!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    
                }
                else{
                    labelval.setText(String.valueOf(look_up.info(ID)));

                    JLabel[]arr = {labelstr,labelval};

                    JOptionPane.showConfirmDialog(frame,arr, "Result",JOptionPane.OK_CANCEL_OPTION ,JOptionPane.PLAIN_MESSAGE );

                }
            }
            else if(e.getSource() == exit){
                ID = 0;
                look_up.exit(ID);
                System.exit(0);
            }
            
        } catch (RemoteException ex) {
            Logger.getLogger(ATMClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void update(String msg) throws RemoteException {
        labelstr.setText(msg);
    }
    
    
    
}
