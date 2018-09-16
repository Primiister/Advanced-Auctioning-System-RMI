import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Auction This is the method in which auction information is stored and
 * processed.
 * 
 * @author Thomas Staunton
 */

public class Auction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int startPrice = 0;
	String description = "";
	int minimum = 0;
	int currentBid = minimum;
	String email = "";
	String name = ""; // variable declarations
	String owner = ""; 
	AccountInterface user;
	AccountInterface highestBidder; 

	/**
	 * Creates a new auction
	 * 
	 * @param the
	 *            starting price, the description of the product and the minimum
	 *            acceptable price for the product
	 * @return a new auction object
	 * @throws RemoteException 
	 */

	public Auction(int startPrice, String description, int minimum, AccountInterface user) throws RemoteException {
		this.startPrice = startPrice;
		this.description = description;
		this.minimum = minimum;
		currentBid = startPrice;
		this.user = user;
		owner = user.getUserName();

	}

	/**
	 * @return starting price of the product
	 */

	public int getStartPrice() {
		return this.startPrice;
	}

	/**
	 * @return description of the product
	 */

	public String getDescription() {
		return this.description;
	}

	/**
	 * @return the minimum bid of the product
	 */

	public int getMinBid() {
		return this.minimum;
	}

	/**
	 * @return the current highest bid of the product
	 */

	public int getCurrentBid() {
		return this.currentBid;
	}
	
	

	public String getOwner() {
		return this.owner;
	}

	/**
	 * This method is to be used when an auction is closed
	 * 
	 * @return the winner of the auction
	 */

	public String getWinner() {
		if (!(this.email == "") && !(this.name == "")) { 
			return "The winner is " + name + " email:" + email + " with a bid of: £"
					+ Integer.toString(this.currentBid);
		} else {
			return "The reserve price has not been reached on this item, there is no winner";
		}
	}

	/**
	 * Places a bid if your bid is higher than the current bid
	 * 
	 * @param the
	 *            bid that is being placed, the name of the bidder and his/her email
	 * @return the bid has been successful, or that the bid is not high enough
	 *         (string)
	 * @throws RemoteException 
	 */

	public String placeBid(int bid, AccountInterface bidder) throws RemoteException {
		if (bid > this.currentBid) { // if bid is higher than the current bid
			this.currentBid = bid;
			this.name = bidder.getName();
			this.email = bidder.getEmail();
			return "Your bid of £" + Integer.toString(bid) + " has been sucessful";
		} else {
			return "Your bid is not high enough";
		}
	}

	/**
	 * @return the winner of the auction (string)
	 */

	public String displayAuction() {
		return ", Start Price = " + Integer.toString(this.startPrice) + ", description = " + this.description + " with a current top bid of: £" + this.currentBid + " owned by " + owner;
	}

}
