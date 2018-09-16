/****************************************************
 * Client_request.java                              *
 *                                                  *
 * This is the Client Request
 *
 *
 ****************************************************/


import java.io.*;

public class Client_request implements Serializable{
	private String userName;
	private int nonse;
	private long passcode;

    /*******************************************
     Note: nonse feild is just a random number
           So just generate a random number using
           any random nnumber generator
    *********************************************/


    /* used for key based authentication */
	public Client_request(String userName, int nonse){
		this.userName = userName;
		this.nonse = nonse;
		this.passcode = 0;
	}
	public long get_passcode(){
		return this.passcode;
	}
	
	public String get_userName(){
		return this.userName;
	}
	public int get_nonse(){
		return this.nonse;
	}
}
