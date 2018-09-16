
/**
 * Seller, this is the client where products are sold
 * @author Thomas Staunton
 */

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Seller {
	int nonse = 0; 
	public static void main(String[] args) {
		boolean loggedIn = false;
		Scanner sc = new Scanner(System.in);
		String input = "";


		try {

			ServerInterface si = (ServerInterface) Naming.lookup("//127.0.0.1:5000/AuctioningSystem");
			/*
			 * interface is defined and connected to the server so any methods called using
			 * this object reference will be implemented on the server
			 */

			Account a = new Account("start", "start", "start", "start");
			// AccountInterface account = (AccountInterface)
			// UnicastRemoteObject.exportObject(a, 0);
			boolean wenttocatch = false;
			do {
				System.out.println(si.connected());
				// System.out.println(si.createUser("d", "g", "h", "h", account));
				System.out.println("Welcome Seller, type logIn or createAccount");
				String userName = "";
				String name = "";
				String emailAddress = "";
				String passWord = "";
				input = sc.nextLine();
				switch (input) {
				case "createAccount":
					do {
						System.out.println("Type in your user name");
						if (sc.hasNextLine()) {
							userName = sc.nextLine();
							wenttocatch = true;

						} else {
							sc.nextInt();
							System.out.println("Enter a valid string");

						}
					} while (!wenttocatch);
					wenttocatch = false;

					do {
						System.out.println("Type in your full name");
						if (sc.hasNextLine()) {
							name = sc.nextLine();
							wenttocatch = true;

						} else {
							sc.nextInt();
							System.out.println("Enter a valid string");

						}
					} while (!wenttocatch);
					wenttocatch = false;

					do {
						System.out.println("Type in your email address");
						if (sc.hasNextLine()) {
							emailAddress = sc.nextLine();
							wenttocatch = true;

						} else {
							sc.nextInt();
							System.out.println("Enter a valid string");

						}
					} while (!wenttocatch);
					wenttocatch = false;

					do {
						System.out.println("Type in your password");
						if (sc.hasNextLine()) {
							passWord = sc.nextLine();
							wenttocatch = true;

						} else {
							sc.nextInt();
							System.out.println("Enter a valid string");

						}
					} while (!wenttocatch);
					wenttocatch = false;
					if (si.createUser(userName, name, passWord, emailAddress)) {
						System.out.println("Thank you, your account " + userName + " has been successfully created");
					} else {

						System.out.println(userName + " is taken. Please choose another user name.");
					}
					break;

				case "logIn":
					do {
						System.out.println("Type in your user name");
						if (sc.hasNextLine()) {
							userName = sc.nextLine();
							wenttocatch = true;

						} else {
							sc.nextInt();
							System.out.println("Enter a valid string");

						}
					} while (!wenttocatch);
					wenttocatch = false;

					do {
						System.out.println("Type in your password");
						if (sc.hasNextLine()) {
							passWord = sc.nextLine();
							wenttocatch = true;

						} else {
							sc.nextInt();
							System.out.println("Enter a valid string");

						}
					} while (!wenttocatch);
					wenttocatch = false;

					a = si.login(userName, passWord);

					String namew = a.getUserName();

					System.out.println(namew);

					if (namew.equals("start") || namew.equals("server")) {

						System.out.println("Login unsuccessful, please try again");
						break;

					} else {

						
						if (authenticateStage1(si) && authenticateStage3(si, a)) {
							System.out.println("Login successful. Welcome " + a.getName());
							loggedIn = true;
						
						} else {
							System.out.println("Authentication failed");
						}
					}

				default:
					System.out.println("Please enter a correct command");
					break;
				}

			} while (loggedIn == false);

			while (loggedIn) {
				System.out.println("Welcome Seller " + a.getName() + ", type Auctions, createAuction or closeAuction");
				wenttocatch = false;

				input = sc.nextLine();
				switch (input) { /*
									 * Takes the user input and depending on the response will go to the
									 * corresponding case
									 */
				case "Auctions":
					System.out.println(si.getItems()); // prints out all the currently active auctions
					break;
				case "createAuction":

					String desc = ""; // input variable declarations
					int starting = 0;
					int minimum = 0;

					do { // listens for the user input until wenttocatch goes to true, this prevents
							// errors
						System.out.println("Type in your starting price");
						if (sc.hasNextInt()) {
							starting = sc.nextInt();
							sc.nextLine(); // remove next line character from scanner
							wenttocatch = true;

						} else {
							sc.nextLine();
							System.out.println("Enter a valid Integer value");

						}
					} while (!wenttocatch);
					wenttocatch = false; // reset to false

					do { // listens for the user input until wenttocatch goes to true, this prevents
							// errors
						System.out.println("Type in your product description");
						if (sc.hasNextLine()) {
							desc = sc.nextLine();
							wenttocatch = true;

						} else {
							sc.nextInt();
							sc.nextLine();
							System.out.println("Enter a valid String");

						}
					} while (!wenttocatch);

					wenttocatch = false;
					do { // listens for the user input until wenttocatch goes to true, this prevents
							// errors
						System.out.println("Type in your product minimum price");
						if (sc.hasNextInt()) {
							minimum = sc.nextInt();
							sc.nextLine();
							wenttocatch = true;

						} else {
							sc.nextLine();
							System.out.println("Enter a valid Int");

						}
					} while (!wenttocatch);
					wenttocatch = false; // resets to false

					System.out.println("Thank you, your auction has been successfully created and your number is: "
							+ si.createAuction(starting, desc, minimum, a)); // creates a new auction
					break;

				case "closeAuction":
					do {
						System.out.println("Type in the auction ID of the auction you would like to close");
						if (sc.hasNextInt()) {
							int auctionToClose = sc.nextInt();
							System.out.println(si.closeAuction(auctionToClose, a)); // closes given auction
							sc.nextLine(); // remove next line character from scanner
							wenttocatch = true;

						} else {
							sc.nextLine();
							System.out.println("Enter a valid Integer value");

						}
					} while (!wenttocatch);
					wenttocatch = false;

				default:
					System.out.println("Please enter a correct command");
					break;
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
		} catch (NotBoundException e) {

			e.printStackTrace();
		} catch (InterruptedException e) {

			e.printStackTrace();
		} catch (InvalidKeyException e) {

			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (InvalidKeySpecException e) {

			e.printStackTrace();
		} catch (NoSuchPaddingException e) {

			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (BadPaddingException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
		sc.close(); // closes scanner
	}

	public static boolean authenticateStage1(ServerInterface si)
			throws Exception {

		DESKeySpec desKeySpec = new DESKeySpec("xjhg6sa8".getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey skey = keyFactory.generateSecret(desKeySpec);
		Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skey);

		Random rand = new Random();
		int nonse = rand.nextInt();
		SealedObject so = (SealedObject) si.authenticateStage2(nonse);
		Client_request cr = (Client_request) so.getObject(cipher);
		System.out.println("nonse = " + nonse);
		String serverName = cr.get_userName();
		System.out.println("servername = " + serverName  + "nonse = " + cr.get_nonse());
 
		//(cr.get_userName() == "Server" 
		if (cr.get_nonse() == nonse) {

			return true;
		} else
			return false;
	}
	
	public static boolean authenticateStage3(ServerInterface si, Account user)throws Exception{
		
		DESKeySpec desKeySpec = new DESKeySpec("xjhg6sa8".getBytes());
    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    	SecretKey skey = keyFactory.generateSecret(desKeySpec);
    	Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    	

		cipher.init(Cipher.ENCRYPT_MODE, skey);
		
		Client_request request = new Client_request(user.getUserName(),si.authenticateStage4(user));
    	SealedObject sealedObject = new SealedObject(request, cipher); //client encrypts random number and returns
    	
    	
    	return si.authenticateStage5(sealedObject, user);
		
	}

}
