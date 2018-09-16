import java.io.IOException;
import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.swing.text.View;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.blocks.MethodCall;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.RspList;

public class FrontendImpl extends ReceiverAdapter  implements ServerInterface, Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5809515705121771349L;

	/**
	* 
	*/
//	private static final long serialVersionUID = 2155674647091870170L;

	/**
	 * 
	 */

	static JChannel channel;
	//RspList<Object> rsp_list = new RspList<Object>();

    RspList            rsp_list;
	RpcDispatcher disp = new RpcDispatcher();

	public FrontendImpl() throws Exception {
		
		channel = new JChannel();
		String             props = null;
		 channel.connect("ServerCluster");
         channel.setDiscardOwnMessages(true);
  	     disp = new RpcDispatcher(channel, this);
}

	public String connected() throws RemoteException, Exception {
		RequestOptions opts = new RequestOptions(ResponseMode.GET_ALL, 5000);
		rsp_list = disp.callRemoteMethods(null, "connected", new Object[] {}, new Class[] {}, opts);
		return (String) rsp_list.getFirst();
	}

	/**
	 * creates a new user
	 * 
	 * @param the
	 *            username, password, name and email of the client (string) and the
	 *            account interface (AccountInterface)
	 * @return if the user has been created or not (boolean)
	 */

	public boolean createUser(String username, String name, String password, String email)throws RemoteException, InterruptedException, Exception{
		RequestOptions opts = new RequestOptions(ResponseMode.GET_FIRST, 5000);
		rsp_list = disp.callRemoteMethods(null, "createUser", new Object[] { username, name, password, email },
				new Class[] { String.class, String.class, String.class, String.class }, opts);
		         System.out.println(rsp_list.getFirst());
		return  (boolean) rsp_list.getFirst();
	}

	/**
	 * allows a user to log into their account
	 * 
	 * @param the
	 *            username and password of the client (string) and the account
	 *            interface (AccountInterface)
	 * @return if the user has been logged in or not (boolean)
	 */

	public Account login(String userName, String passWord)throws RemoteException, InterruptedException, Exception {
		RequestOptions opts = new RequestOptions(ResponseMode.GET_ALL, 0);
		rsp_list = disp.callRemoteMethods(null, "login", new Object[] { userName, passWord },
				new Class[] { String.class, String.class }, opts);
		 System.out.println("Done");
		return (Account) rsp_list.getFirst();
	}

	/**
	 * Gets the information of every currently active auction
	 * 
	 * @return Every currently active auction (string)
	 */

	public String getItems() throws Exception {
		RequestOptions opts = new RequestOptions(ResponseMode.GET_ALL, 5000);
		rsp_list = disp.callRemoteMethods(null, "getItems", new Object[] {}, new Class[] {}, opts);
		return (String) rsp_list.getFirst();
	}

	/**
	 * Allows the client to place a bid
	 * 
	 * @param the
	 *            bid the client is placing, the index of the auction, the name of
	 *            the buyer and his email
	 * @return if the bid has been successful or not (string)
	 */

	public String placeBid(int bid, int auctionID, Account bidder) throws Exception {
		RequestOptions opts = new RequestOptions(ResponseMode.GET_ALL, 5000);
		rsp_list = disp.callRemoteMethods(null, "placeBid", new Object[] { bid, auctionID, bidder },
				new Class[] { int.class, int.class, AccountInterface.class }, opts);
		return (String) rsp_list.getFirst();
	}

	/**
	 * Creates a new auction
	 * 
	 * @param the
	 *            starting price, the description of the product and the minimum
	 *            acceptable price for the product
	 * @return the index of the new auction (the auction number int)
	 */

	public int createAuction(int startingPrice, String description, int minimumAcceptablePrice, AccountInterface user)
			throws Exception {
		RequestOptions opts = new RequestOptions(ResponseMode.GET_ALL, 5000);
		rsp_list = disp.callRemoteMethods(null, "createAuction",
				new Object[] { startingPrice, description, minimumAcceptablePrice, user },
				new Class[] { int.class, String.class, int.class, AccountInterface.class }, opts);
		return (int) rsp_list.getFirst();
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

	public String closeAuction(int auctionID, AccountInterface User)
			throws RemoteException, InterruptedException, Exception {
		RequestOptions opts = new RequestOptions(ResponseMode.GET_ALL, 5000);
		rsp_list = disp.callRemoteMethods(null, "closeAuction", new Object[] { auctionID, User },
				new Class[] { int.class, AccountInterface.class }, opts);
		return (String) rsp_list.getFirst();
	}

	/**
	 * allows a client to authenticate with the server
	 * 
	 * @param the
	 *            account interface (AccountInterface)
	 * @return if the user has been authenticated or not (boolean)
	 * @throws Exception
	 */

	public boolean authenticate(AccountInterface user) throws Exception {
		RequestOptions opts = new RequestOptions(ResponseMode.GET_ALL, 5000);
		rsp_list = disp.callRemoteMethods(null, "authenticate", new Object[] { user },
				new Class[] { AccountInterface.class }, opts);
		return (boolean) rsp_list.getFirst();
	}

	public void receive(Message msg) {

		try {
			disp.handle(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void viewAccepted(View new_view) {
		System.out.println("** view: " + new_view);
	}

	@Override
	public String getItem(int auctionID) throws RemoteException, InterruptedException, Exception {
		return null;
	}
	
	  public void start() throws Exception {

	//        MethodCall call=new MethodCall(getClass().getMethod("print2", int.class));

	        RequestOptions opts=new RequestOptions(ResponseMode.GET_ALL, 5000);

	        channel=new JChannel();

	        disp=new RpcDispatcher(channel, this);

	        channel.connect("RpcDispatcherTestGroup");


	        for(int i=0; i < 10; i++) {

	            //Util.sleep(100);

	            rsp_list=disp.callRemoteMethods(null,

	                                            "print",

	                                            new Object[]{i},

	                                            new Class[]{int.class},

	                                            opts);

	            // Alternative: use a (prefabricated) MethodCall:

	            // call.setArgs(i);

	            // rsp_list=disp.callRemoteMethods(null, call, opts);

	            System.out.println("Responses: " + rsp_list);

	        }
	  }
	  
	public static int printNumber(int number) throws Exception {

		return number * 2;

	}

	public SealedObject authenticateStage2(int nonse)
			throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			IllegalBlockSizeException, IOException, ClassNotFoundException, BadPaddingException, Exception {
		RequestOptions opts = new RequestOptions(ResponseMode.GET_ALL, 0);
		rsp_list = disp.callRemoteMethods(null, "authenticateStage2", new Object[] { nonse }, new Class[] { int.class },
				opts);
		return (SealedObject) rsp_list.getFirst();
	}

	public int authenticateStage4(Account user) throws Exception {
		RequestOptions opts = new RequestOptions(ResponseMode.GET_ALL, 0);
		rsp_list = disp.callRemoteMethods(null, "authenticateStage4", new Object[] { user },
				new Class[] { Account.class }, opts);
		return (int) rsp_list.getFirst();
	}

	public boolean authenticateStage5(SealedObject response, Account user) throws Exception {
		RequestOptions opts = new RequestOptions(ResponseMode.GET_ALL, 0);
		rsp_list = disp.callRemoteMethods(null, "authenticateStage5", new Object[] { response, user },
				new Class[] { SealedObject.class, Account.class }, opts);
		return (boolean) rsp_list.getFirst();
	}

	public static void main(String[] args) throws Exception {
		try {

			LocateRegistry.createRegistry(0); // creates new RMI registry
			// JChannel channel = new JChannel();
			// ServerInterface si = (ServerInterface) new FrontendImpl();
			FrontendImpl impl = new FrontendImpl();
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(impl, 0);

			// System.out.println();

			Naming.rebind("rmi://127.0.0.1:5000/AuctioningSystem", stub); // rebinds to this address
			System.out.println("FrontEndServerActivated");
			// Account a = new Account("", "", "" ,"");
			// AccountInterface account = (AccountInterface)
			// UnicastRemoteObject.exportObject(a, 0);
			// stub.login("Thomas", "password", account);
		} catch (Exception e) {
			channel.disconnect();
		       System.out.println("Server Error: " + e);
		     }
		   

	   }


}