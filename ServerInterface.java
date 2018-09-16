
/**
 * ServerInterface This is the interface to the server
 * 
 * @author Thomas Staunton
 */

import java.io.IOException;
import java.rmi.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public interface ServerInterface extends Remote {

	/**
	 * Gets the information of every currently active auction
	 * 
	 * @return Every currently active auction (string)
	 */

	public String getItems() throws RemoteException, InterruptedException;

	/**
	 * gets an existing auction from the array list "Auctions"
	 * 
	 * @param the
	 *            index of the auction in the array list
	 * @return the information of the auction (but not the minimum acceptable price
	 *         as this is secret)
	 */

	public String getItem(int auctionID) throws RemoteException, InterruptedException;

	/**
	 * Allows the client to place a bid
	 * 
	 * @param the
	 *            bid the client is placing, the index of the auction, the name of
	 *            the buyer and his email
	 * @return if the bid has been successful or not (string)
	 */

	public String placeBid(int bid, int auctionID, AccountInterface bidder)
			throws RemoteException, InterruptedException;

	/**
	 * Creates a new auction
	 * 
	 * @param the
	 *            starting price, the description of the product and the minimum
	 *            acceptable price for the product
	 * @return the index of the new auction (the auction number int)
	 */

	public int createAuction(int startingPrice, String description, int minimumAcceptablePrice, AccountInterface user)
			throws RemoteException, InterruptedException;

	/**
	 * closes an auction by removing it from the array list
	 * 
	 * @param the
	 *            index of the auction in the array list
	 * @return the winner of the auction if there is one, otherwise there is no
	 *         winner. Alternatively if the auction doesn't exist that will be
	 *         returned instead (string)
	 */

	public String closeAuction(int auctionID, AccountInterface User) throws RemoteException, InterruptedException;

	public boolean addUser(String userName, String name, String passWord, AccountInterface account)throws RemoteException, InterruptedException;

	public AccountInterface login(String userName, String passWord, AccountInterface account)throws RemoteException, InterruptedException;
	
	public boolean authenticate(AccountInterface user) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, IOException, ClassNotFoundException, BadPaddingException;

}
