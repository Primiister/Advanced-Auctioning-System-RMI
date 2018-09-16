/**
 * Server, The RMI server deals with requests from the two client
 * programs and maintain the state of the ongoing auctions.
 * @author Thomas Staunton
 */
import org.jgroups.*;
import java.rmi.Naming;	//Import naming classes to bind to RMIregistry
import java.rmi.registry.LocateRegistry;

public class FrontendServer {
	
		   public FrontendServer()  {
			   
		     try {
		    	
		    	LocateRegistry.createRegistry(0); //creates new RMI registry
		    //    JChannel channel = new JChannel();
		       	ServerInterface si = (ServerInterface) new FrontendImpl(); 
		    	Naming.rebind("rmi://127.0.0.1:5000/AuctioningSystem", si); //rebinds to this address
		       	System.out.println("Server Activated");
		     } 
		     catch (Exception e) {
		       System.out.println("Server Error: " + e);
		     }
		   }

		   public static void main(String args[]) throws Exception {
				new FrontendServer(); //starts new server
		
		   }

	
	
		


}