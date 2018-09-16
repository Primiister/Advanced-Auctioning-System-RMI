
/**
 * ServerImpl This is where the functionality of the server is implemented
 * 
 * @author Thomas Staunton
 */

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.Semaphore;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.blocks.MethodCall;
import org.jgroups.blocks.RequestHandler;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.RspList;

import java.util.HashMap;
import java.util.Random;

public class BackendImpl extends ReceiverAdapter implements ServerInterface, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1823069411973331542L;
	

	JChannel channel = new JChannel();
	RspList<Object> rsp_list = new RspList<Object>();
	RpcDispatcher disp = new RpcDispatcher();
	HashMap<Integer, Auction> Auctions = new HashMap<Integer, Auction>(); 
	HashMap<String, Account> Users = new HashMap<String, Account>(); 
    
	Semaphore sem = new Semaphore(1); // new semaphore for thread safety

	int auctionNo = 0;

	// Implementations must have an explicit constructor
	// in order to declare the RemoteException exception

	public BackendImpl() throws Exception {
		super();
		   channel.connect("ServerCluster");
           channel.setDiscardOwnMessages(true);
    	   disp = new RpcDispatcher(channel, this);
    	   
            this.createUser("Thomas", "Tom", "password", "email");
	}
	

	/**
	 * creates a new user
	 * 
	 * @param the username, password, name and email of the client (string) and the account interface (AccountInterface)
	 * @return if the user has been created or not (boolean)
	 */
	
	
	 public String connected() throws RemoteException, Exception {
	       
	        return "Connected to backend";
	    }
	
		public boolean createUser(String username, String name, String password, String email)throws RemoteException, InterruptedException, Exception {
	        if (!Users.containsKey(username)) {
	        Account acc = new Account (username, email, name, password);
	            Users.put(username, acc);
	            return true;
	        } else {
	            return false;
	        }

	    }
	  
	  /**
		 * allows a user to log into their account
		 * 
		 * @param the username and password of the client (string) and the account interface (AccountInterface)
		 * @return if the user has been logged in or not (boolean)
		 */
	  
		public Account login(String userName, String passWord)throws  Exception {
			
        if (Users.containsKey(userName) && Users.get(userName).getPassword().equals(passWord)) {
            return Users.get(userName);
        }
        else{
            return new Account("server", "server", "server", "server");
        }
        
    }

	/**
	 * Gets the information of every currently active auction
	 * 
	 * @return Every currently active auction (string)
	 */

		public String getItems() throws RemoteException, InterruptedException, Exception {
		String output = "";

		for (int i = 1; i <= auctionNo; i++) {
			if (!(Auctions.get(i) == null)) {
				sem.acquire(); // locks critical section of code
				output += "Auction ID = " + i + Auctions.get(i).displayAuction();
				output += System.getProperty("line.separator");
				sem.release(); // releases critical section of code
			}
		}
		if (output == "") {
			return "Sorry there are currently no active auctions at present";
		} else {
			return output;
		}
	}


	/**
	 * Allows the client to place a bid
	 * 
	 * @param the
	 *            bid the client is placing, the index of the auction, the name of
	 *            the buyer and his email
	 * @return if the bid has been successful or not (string)
	 */

	public String placeBid(int bid, int auctionID, Account bidder)
			throws RemoteException, InterruptedException, Exception {
		if (!(Auctions.get(auctionID) == null)) {
			sem.acquire(); // locks critical section of code
			String output = Auctions.get(auctionID).placeBid(bid, bidder);
			sem.release(); // releases critical section of code
			return output;
		} else {
			return "The auction ID " + auctionID + " does not exist, please try again";
		}
	}

	/**
	 * Creates a new auction
	 * 
	 * @param the
	 *            starting price, the description of the product and the minimum
	 *            acceptable price for the product
	 * @return the index of the new auction (the auction number int)
	 */

		public int createAuction(int startingPrice, String description, int minimumAcceptablePrice, Account user)
				throws RemoteException, InterruptedException, Exception {
		sem.acquire(); // locks critical section of code
		auctionNo++;
		Auction auction = new Auction(startingPrice, description, minimumAcceptablePrice, user);
		Auctions.put(this.auctionNo, auction);
		sem.release(); // releases critical section of code
		return this.auctionNo;
	}

	/**
	 * gets an existing auction from the array list "Auctions"
	 * 
	 * @param the
	 *            index of the auction in the array list
	 * @return the information of the auction (but not the minimum acceptable price
	 *         as this is secret)
	 */

		public String getItem(int auctionID) throws RemoteException, InterruptedException, Exception {
		sem.acquire(); // locks critical section of code
		String output = Auctions.get(auctionID).displayAuction();
		sem.release(); // releases critical section of code
		return output;
	}

	/**
	 * closes an auction by removing it from the array list
	 * 
	 * @param the
	 *            index of the auction in the array list
	 * @return the winner of the auction if there is one, otherwise there is no
	 *         winner. Alternatively if the auction doesn't exist that will be
	 *         returned instead (string)
	 */

	public String closeAuction(int auctionID, Account User) throws RemoteException, InterruptedException {
		String output = "";
		if (!(Auctions.get(auctionID) == null) && Auctions.get(auctionID).getOwner() == User.getUserName()) {
			sem.acquire(); // locks critical section of code
			output = Auctions.get(auctionID).getWinner();
			Auctions.remove(auctionID);
			sem.release(); // releases critical section of code
			return output;
		} else {
			return "The auction ID: " + auctionID + " does not exist or you do not have the permissions to alter it, please try again";
		}
	}
	
	/**
	 * allows a client to authenticate with the server
	 * 
	 * @param the account interface (AccountInterface)
	 * @return if the user has been authenticated or not (boolean)
	 * @throws Exception 
	 * 
	 */
	
	public SealedObject authenticateStage2(int nonse) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, IOException, ClassNotFoundException, BadPaddingException, Exception {
		
		DESKeySpec desKeySpec = new DESKeySpec("xjhg6sa8".getBytes());
    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    	SecretKey skey = keyFactory.generateSecret(desKeySpec);
    	Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    	

		cipher.init(Cipher.ENCRYPT_MODE, skey);
		Client_request request = new Client_request("s", nonse);
		System.out.println("Server name = " + request.get_userName());
    	SealedObject sealedObject = new SealedObject(request, cipher); //client encrypts random number and returns
		return sealedObject;
		
	}
	
	public int authenticateStage4(Account user) throws RemoteException {
		Random rand = new Random();
		int nonse = rand.nextInt();
		user.setRandom(nonse);
		
		if (Users.containsKey(user.getUserName())) {
			Users.put(user.getUserName(), user);
			
		}
		System.out.println(nonse);
		
		return nonse;
	}
	
	
	public boolean authenticateStage5(SealedObject response, Account user) throws ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException {

		DESKeySpec desKeySpec = new DESKeySpec("xjhg6sa8".getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey skey = keyFactory.generateSecret(desKeySpec);
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skey);
		int rand = 0;
		Client_request cr = (Client_request) response.getObject(cipher);
		
		if (Users.containsKey(user.getUserName())) {
			rand = Users.get(user.getUserName()).getRandom();
		}
		
		System.out.println("Username = " + cr.get_userName()+ "Nonse = " + cr.get_nonse());
		
		if (Users.containsKey(cr.get_userName()) && cr.get_nonse() == rand) {

			return true;
		} else
			return false;
	}
	
	
	public void receive(Message msg) {

		try {
			disp.handle(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	 
	 
	 public void start() throws Exception {

	       // MethodCall call=new MethodCall(getClass().getMethod("print2"));

	        RequestOptions opts=new RequestOptions(ResponseMode.GET_ALL, 5000);

	        channel=new JChannel();

	        disp=new RpcDispatcher(channel, this);

	        channel.connect("ServerCluster");


	        for(int i=0; i < 10; i++) {

	     

	           rsp_list=disp.callRemoteMethods(null,

	                                           "printNumber",
	                                           new Object[]{i},

	                                           new Class[]{int.class},

	                                        opts);

	 

            System.out.println("Responses: " + rsp_list);

	       }

	    }
	
	
	 public void viewAccepted(View new_view) { System.out.println("** view: " + new_view); }
	
	 public static void main(String[] args) throws Exception {

	
	    Account p = new Account("", "", "", "");
	    	p =	new BackendImpl().login("Thomas", "password");
		System.out.println(p.getUserName() + "BackEndServerActivated");
    }



}