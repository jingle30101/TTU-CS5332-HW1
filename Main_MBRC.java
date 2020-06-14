package homework;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Queue;
import static java.lang.Thread.sleep;

class Quene {
//	private int maxCount;
//	private int messageCount;
//	private int responseCount;
//	
//	Queue<byte[]> messageBuffer;
//	Queue<byte[]> responseBuffer;
//	
//	
//	public Quene(){
//		this.maxCount = 1;
//		this.messageCount = 0;
//		this.responseCount = 0;
//		
//	}
//
//	

    Queue<byte[]> messageBuffer;
    Queue<byte[]> responseBuffer;

    private boolean messageBufferFull;
    private boolean responseBufferFull;

    public Quene(){
        this.messageBuffer =  new LinkedList<>();
        this.responseBuffer = new LinkedList<>();
        this.messageBufferFull = false;
        this.responseBufferFull = false;
    }
//	public synchronized void send(byte[] ciphertext) throws InterruptedException {
//	messageBuffer.add(ciphertext);
//	messageCount++;
//	
//
//	while (responseCount == 0) {
//		try {
//			wait();
//		} catch (InterruptedException e) {
//			System.out.println(ciphertext + " is placed in queue buffer,");
//		}
//	}
//	 responseCount = 0;
//	}
//
    synchronized void send(byte[] ciphertext) throws InterruptedException {
        messageBuffer.add(ciphertext);
        messageBufferFull = true;
        notify();
        while(!responseBufferFull){
            try {
                System.out.println("Wait reply");
                wait();
            } catch(InterruptedException e) {
                System.out.println("InterruptedException caught");
            }
        }
        responseBufferFull = false;
    }
//	synchronized byte[] receive() throws InterruptedException {
//  while(messageCount == 0){
//      try {
//          wait();
//
//      } catch(InterruptedException e) {
//    
//          System.out.println( " desCipher is received,");
//      }
//  }
//
//  byte[] ciphertext  = messageBuffer.remove();
//  messageCount--;
//  notify();
//  sleep(100);
//  return ciphertext;
//}

    synchronized byte[] receive() throws InterruptedException {
        while(!messageBufferFull){
            try {
                System.out.println("Wait receive");
                wait();

            } catch(InterruptedException e) {
                System.out.println("InterruptedException caught");
            }
        }

        byte[] ciphertext  = messageBuffer.poll();
        messageBufferFull = false;
        notify();
        //暂停一下receiver, 让后续结果跑完, 这样for循环不至于结束太快
        sleep(100);
        return ciphertext;
    }
//	synchronized void reply(String messageDigest, int result) throws InterruptedException {
//	String response = messageDigest + ',' + result;
//    byte[] res = response.getBytes();
//    responseBuffer.add(res);
//    responseCount = maxCount;
//    notify();
//    responseCount--;
//}

    synchronized void reply(String messageDigest, int result) throws InterruptedException {
        String response = messageDigest + ',' + result;
        byte[] resp = response.getBytes();
        responseBuffer.add(resp);
        responseBufferFull = true;
        notify();
    }
    


}
public class Main_MBRC {
	public static void main(String[] args) {
		System.out.println("Program: To perform addicalculation and multiplycalculation operation on integer 10.");
		Quene q = new Quene();
		Sender s = new Sender(q);
		Receiver r = new Receiver(q, null);
		r.run();
		s.run();
		
	}
}