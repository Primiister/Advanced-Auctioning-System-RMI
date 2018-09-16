
/**
 * Seller, this is the client where products are sold
 * @author Thomas Staunton
 */

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Buyer {

	public static void main(String[] args) {
		boolean loggedIn = false;
		Scanner sc = new Scanner(System.in);
		String input = "";

		try {

			ServerInterface si = (ServerInterface) Naming.lookup("//127.0.0.1:5000/AuctioningSystem"); /* interface is defined 
			and connected to the server so any methods called using this object reference will be
			 implemented on the server */
			
			Account a = new Account("", "", "", "");
			AccountInterface account = (AccountInterface) UnicastRemoteObject.exportObject(a, 0);
			boolean wenttocatch = false;
			System.out.println("Welcome Buyer, type logIn or createAccount");
			do {
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
                   if(si.createUser(userName, name, passWord, emailAddress)) {
					System.out.println("Thank you, your account " + userName + " has been successfully created");
                   } else {
                	   
                	   System.out.println(userName + " is taken. Please choose another user name.");
                   }
					userName = "";
					name = "";
					passWord = "";
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
					AccountInterface user = si.login(userName, passWord, account);

					if (user != null) {
						account = user;
						System.out.println("Login successful. Welcome " + account.getName());
						loggedIn = true;
					} else {
						System.out.println("Login unsuccessful, please try again");
						userName = "";
						name = "";
						passWord = "";
						break;
					}

				default:
					System.out.println("Please enter a correct command");
					break;
				}

			} while (loggedIn == false);


			while (loggedIn) {

				System.out.println("Welcome buyer " + account.getName() + ", type Auctions or placeBid");
				wenttocatch = false;

				input = sc.nextLine();
				switch (input) {/*
								 * Takes the user input and depending on the response will go to the
								 * corresponding case
								 */
				case "Auctions":
					System.out.println(si.getItems()); // prints out all the currently active auctions
					break;
				case "placeBid":

					String name = "";
					String email = "";
					int bid = 0;
					int auctionID = 0; // input variable declarations

					do {// listens for the user input until wenttocatch goes to true, this prevents
						// errors
						System.out.println("Type the ID of the auction you would like to bid on");
						if (sc.hasNextInt()) {
							auctionID = sc.nextInt();
							sc.nextLine();
							wenttocatch = true;

						} else {
							sc.nextLine();
							System.out.println("Enter a valid Int");

						}
					} while (!wenttocatch);
					wenttocatch = false;

					do {// listens for the user input until wenttocatch goes to true, this prevents
						// errors
						System.out.println("Type the bid that you would like to place");
						if (sc.hasNextInt()) {
							bid = sc.nextInt();
							sc.nextLine(); // remove next line character from scanner
							wenttocatch = true;

						} else {
							sc.nextLine();
							System.out.println("Enter a valid Integer value");

						}
					} while (!wenttocatch);
					wenttocatch = false;

					do {// listens for the user input until wenttocatch goes to true, this prevents
						// errors
						System.out.println("Type in your user name");
						if (sc.hasNextLine()) {
							name = sc.nextLine();
							wenttocatch = true;

						} else {
							sc.nextInt();
							sc.nextLine();
							System.out.println("Enter a valid String");

						}
					} while (!wenttocatch);

					do {// listens for the user input until wenttocatch goes to true, this prevents
						// errors
						System.out.println("Type in your email address");
						if (sc.hasNextLine()) {
							email = sc.nextLine();
							wenttocatch = true;

						} else {
							sc.nextInt();
							sc.nextLine();
							System.out.println("Enter a valid String");

						}
					} while (!wenttocatch);

					System.out.println(si.placeBid(bid, auctionID, account)); // places bid

					break;

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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.close(); // closes scanner
	}

}
