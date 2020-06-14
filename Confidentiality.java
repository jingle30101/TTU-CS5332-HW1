package homework;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.io.*;

class SymmetricEncryption
{
	static byte[] encrypt(byte s[], Cipher c, SecretKey sk) throws Exception
	{
	    c.init(Cipher.ENCRYPT_MODE, sk);
	    return c.doFinal(s);
	}

	static byte[] decrypt(byte s[], Cipher c, SecretKey sk) throws Exception
	{
	    c.init(Cipher.DECRYPT_MODE, sk);
	    return c.doFinal(s);
	}
}
		 
public class Confidentiality {
	public static void main(String args[]) throws Exception 
	{
	try{
		KeyGenerator keygen = KeyGenerator.getInstance("DES");
		SecretKey desKey = keygen.generateKey();
		
		Cipher desCipher;	
		desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); //Electronic Code Book mode
		//DES has Electronic Code Book, Ciphertext Block Chaining, Cipher FeedBack, Output Feedback modes

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the data: ");
		String str = br.readLine();
		byte[] cleartext = str.getBytes();
	   	String str1 = new String(cleartext);
	   	System.out.println("The string you entered is: " + str1);
	    
	   	byte[] ciphertext = SymmetricEncryption.encrypt(cleartext, desCipher, desKey);
	   	String str2 = new String(ciphertext);
	   	System.out.println("The encrypted data is: " + str2);

		byte[] plaintext = SymmetricEncryption.decrypt(ciphertext, desCipher, desKey);
		String str3 = new String(plaintext);
		System.out.println("The data after decryption is: " + str3);
	 	}
		catch (Exception e){
	 		e.printStackTrace();
	 	}
	}
}
