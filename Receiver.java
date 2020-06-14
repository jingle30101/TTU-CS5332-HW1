package homework;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;

public class Receiver implements Runnable{
    private Quene Quene;
    private Hashing Hashing;
    private int result = 0;
    private final String Add = "add";
    private final String Multiply = "multiply";
    private final String DEFAULT_KEY = "mydeskey";
    private final String messageDigest = "thisismsgdigest";
   

    public Receiver(Quene Quene, Hashing Hashing) {
        this.Quene = Quene;
        this.Hashing = Hashing;
        new Thread(this, "Receiver").start();
    }

    public String decrypt(byte[] ciphertext) throws Exception {
        SecretKey desKey = new SecretKeySpec(DEFAULT_KEY.getBytes(), "DES");
        Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        byte[] plaintext = SymmetricEncryption.decrypt(ciphertext, desCipher, desKey);
        String message = new String(plaintext);
        return message;
    }

    
  public String generate(String messageDigest,Byte[] hashValue) throws Exception {
	  MessageDigest md = MessageDigest.getInstance("SHA-1");
	   byte[] message = messageDigest.getBytes();
	   md.update(message);
	   byte[] mdbytes = md.digest();
	  return(messageDigest);
}

    public void printReceivedResult(String message){
        String[] str = message.split(",");
        String operator = str[0];
        int num = Integer.parseInt(str[1]);
        if(Add.equals(operator)){
            AddCalculation addCalculation = new AddCalculation();
            result = addCalculation.add(num);
        }else if(Multiply.equals(operator)){
            MultiplyCalculation multiplyCalculation = new MultiplyCalculation();
            result = multiplyCalculation.multiply(num);
        }else{
            System.out.println("Error parsing message");
            return;
        }
        System.out.println("Receiver: Received result is " + result);
    }

    @Override
    public void run() {
        for(int i = 0; i < 7; i++){
            try {
                //receive message from sender via Quene
                byte[] ciphertext = Quene.receive();
                System.out.println("Receiver: The message is received");

                //decrypt the message
                String msg = decrypt(ciphertext);
                System.out.println("Receiver: The decrypted message is " + msg);

                //print the result
                printReceivedResult(msg);

                //generate messageDigest
                
                byte[] revMsgDigest = Hashing.generate(messageDigest);
                String resultMsgDiget = Arrays.toString(revMsgDigest);
                System.out.println("Receiver: The messageDigest of result is" +  resultMsgDiget);
              //  System.out.println("Receiver: The messageDigest of result is" + Hex.encodeHexString(revMsgDigest.digest())
              //  System.out.println("Receiver: The messageDigest of result is" + base64Encode(sha1Digest(revMsgDigest)));

                //reply to sender
                Quene.reply(resultMsgDiget, result);
                System.out.println("Receiver: The response is sent");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}