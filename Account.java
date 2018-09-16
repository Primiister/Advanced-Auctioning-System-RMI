import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Account implements AccountInterface, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7402192614473766200L;
	/**
	 * 
	 */

	private String userID;
    private String name;
    private String email; 
    private String password;

    protected Account() throws RemoteException {
        userID = "";
        name = "";
        password = "";
    }

    // Get Login ID
    public String getUserName() throws RemoteException {
        return userID;
    }

    // Get Name
    public String getName() throws RemoteException {
        return name;
    }
    
 // Get Name
    public String getEmail() throws RemoteException {
        return email;
    }

    // Get Password
    public String getPassword() throws RemoteException {
        return password;
    }

    // Set Login ID
    public void setUserName(String login) throws RemoteException {
        userID = login;
    }

    // Set Name
    public void setName(String n) throws RemoteException {
        name = n;
    }

    // Set Password
    public void setPassword(String pw) throws RemoteException {
        password = pw;
    }
    
    public SealedObject random(int rand) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, IOException {
    	DESKeySpec desKeySpec = new DESKeySpec("xjhg6sa8".getBytes());
    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    	SecretKey skey = keyFactory.generateSecret(desKeySpec);
    	
    	Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    	cipher.init(Cipher.ENCRYPT_MODE, skey); 
  
    	Client_request request = new Client_request(this.userID, rand);
    	
    	SealedObject sealedObject = new SealedObject(request, cipher);
    	
    	return sealedObject; 
    }

}