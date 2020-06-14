package homework;

import java.security.MessageDigest;
import java.util.Arrays;
import java.io.*;

class Hashing {
	
	public static byte[] generate(String msg) throws Exception
	{
	   MessageDigest md = MessageDigest.getInstance("SHA-1");
	   byte[] message = msg.getBytes();
	   md.update(message);
	   byte[] mdbytes = md.digest();
	   return(mdbytes);
	}
	
	public static Boolean verify(byte[] hashValue, String msg) throws Exception
    {
	   MessageDigest md = MessageDigest.getInstance("SHA-1");
	   byte[] msgBytes = msg.getBytes();
	   md.update(msgBytes);
	   byte[] mdBytes = md.digest();
	  
	   if (MessageDigest.isEqual(hashValue, mdBytes))
		   return true;
	   else
		   return false;
	}
}

public class Integrity {	
	public static void main (String args[]) throws Exception
	{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter the strings: ");
		String str = br.readLine();
		System.out.println("The string you entered is : " + str);
		System.out.println();
		
		byte hashValue[]= Hashing.generate(str);
		System.out.println("The message digest of your input is : " + Arrays.toString(hashValue));
		
		if (Hashing.verify(hashValue, str))
		   System.out.println("Message is valid and not corrupted");
		else
			System.out.println("Message has been corrupted");
	
	}
}

