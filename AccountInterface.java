/**
 * ServerInterface This is the interface to the accounts
 * 
 * @author Thomas Staunton
 */
import java.io.IOException;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

public interface AccountInterface extends Serializable, Remote{

	/**
	 * Gets the username of the current user
	 * 
	 * @return The username of the current user (string)
	 */

    public String getUserName() throws RemoteException;

    /**
	 * Gets the full name of the current user
	 * 
	 * @return The name of the current user (string)
	 */
    
    public String getName() throws RemoteException;
    
    /**
   	 * Gets the email address of the current user
   	 * 
   	 * @return The email address of the current user (string)
   	 */
    
    public String getEmail() throws RemoteException;
    
    /**
   	 * Gets the password of the current user
   	 * 
   	 * @return The password of the current user (string)
   	 */

    public String getPassword() throws RemoteException;
    
    /**
   	 * Sets the password of the current user
   	 * 
   	 * @param The new username (String)
   	 * 
   	 * @void
   	 */

    public void setUserName(String userName) throws RemoteException;
    
    /**
   	 * Sets the name of the current user
   	 * 
   	 * @param The new name (String)
   	 * 
   	 * @void
   	 */

    public void setName(String name) throws RemoteException;
    
    /**
   	 * Sets the password of the current user
   	 * 
   	 * @param The new password (String)
   	 * 
   	 * @void
   	 */
    
    public void setPassword(String password) throws RemoteException;
    

    /**
   	 * Sends the encrypted challenge to the server
   	 * 
   	 * @param The challenge number (int)
   	 * 
   	 * @return a sealed object containing the the encrypted challenge
   	 */
    
  
}

