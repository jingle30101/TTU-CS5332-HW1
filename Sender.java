package homework;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Sender implements Runnable{
    private Quene Quene;
    private Hashing Hashing;
    private final String messageDigest = "thisismsgdigest";
    private final String DEFAULT_KEY = "mydeskey";
    public Sender(Quene Quene) {
        this.Quene = Quene;
        new Thread(this, "Sender").start();
    }

    public byte[] encrypt(String message) throws Exception {
        byte[] cleartext = message.getBytes();
        SecretKey desKey = new SecretKeySpec(DEFAULT_KEY.getBytes(), "DES");
        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        byte[] ciphertext = SymmetricEncryption.encrypt(cleartext, desCipher, desKey);
        return ciphertext;
    }
    
    
    public boolean verify(byte[] hashValue, String response) throws Exception {
    	MessageDigest md = MessageDigest.getInstance("SHA-1");
    	byte[] message = messageDigest.getBytes();
 	    md.update(message);
 	    byte[] mdBytes = md.digest();
 	    byte[] newresponse = response.getBytes();
 	    
 	    
 	   if (homework.Hashing.verify(message, response))
		   System.out.println("Message is valid and not corrupted");
 	   return true;
 	}
    

    public void printReceivedResult(int result){
        System.out.println("Sender: received " + result);
    }

    public void printMessage(String message){
        System.out.println("Sender: Sent " + message);
    }

    @Override
    public void run() {
        Queue<String> messages = new LinkedList<>();
        messages.add("add,4");
        messages.add("multiply,1");
        messages.add("multiply,8");
        messages.add("add,2");
        messages.add("add,3");
        messages.add("add,99");
        messages.add("multiply,53");

        while (!messages.isEmpty()) {
            String msg = messages.poll();
            String[] str = msg.split(",");
            String operator = str[0];
            int num = Integer.parseInt(str[1]);
            System.out.println("------------------------------------------------------------------------");
            System.out.println("Action: To calculate " + operator + " "+ num);

            try {
                //print
                printMessage(msg);

                //encrypt the message
                
                byte[] ciphertext = encrypt(msg);
                System.out.println("Sender: The encrypted message is "+ ciphertext);
                //send the message via Quene object
                System.out.println("Sender: The message is sent");
                Quene.send(ciphertext);

                //verify response
                System.out.println("Sender: The response is true");
                byte[] resp = Quene.responseBuffer.poll();
                String response = new String(resp);
                verify(resp, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}