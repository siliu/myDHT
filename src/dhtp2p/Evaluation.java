package dhtp2p;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import dhtp2p.peer.PeerClient;
import dhtp2p.utils.KeyHash;
/**
 * 
 * @author siliu
 * Evaluation class is to start server and send requests
 */
public class Evaluation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		System.out.println("Please input the index for this peer: ");
    	Scanner inputScanner = new Scanner(System.in);
    	String inputRaw = inputScanner.nextLine();
    	String configPath = "/Users/siliu/Documents/workspace/DHTP2PFileSharing/src/dhtp2p/utils/config.txt";
    	
		try {
	
			Peer peer = new Peer(Integer.parseInt(inputRaw),configPath);
			peer.startup();
			Thread.sleep(10);
			PeerClient pc = peer.getpClient();
			
			/*
			Random rn = new Random(Integer.parseInt(inputRaw));
			
			long starttime = System.nanoTime();
			
			for(int i=0 ; i < 1000000 ; i++){
				String dhtKey = "key" + rn.nextInt();
				String dhtValue = "value";
				pc.put(dhtKey, dhtValue);
			}
			
			long endtime = System.nanoTime();
			
			long duration = TimeUnit.MILLISECONDS.convert((endtime - starttime), TimeUnit.NANOSECONDS);;
			
			System.out.println("Sending 1M PUT requests takes : " + duration + " ms." );
			*/
			
			Timer timer = new Timer();
			Date startdate = new Date(115,9,2,10,52,00);
			
			System.out.println("Task start time: " + startdate);
		
			timer.schedule(new TimerTask() {
				public void run(){
			
					Random rn = new Random(Integer.parseInt(inputRaw));
					
					//Send 100K PUT requests
					long putstart = System.nanoTime();
					
					for(int i=0 ; i < 100000 ; i++){
						String dhtKey = "key" + rn.nextInt();
						String dhtValue = "value";
						pc.put(dhtKey, dhtValue);
						
					}
					long putend = System.nanoTime();
					
					long putduration = TimeUnit.MILLISECONDS.convert((putend - putstart), TimeUnit.NANOSECONDS);
					System.out.println("Sending 100K PUT requests takes : " + putduration + " ms." );
					
					//Send 100K GET requests
					long getstart = System.nanoTime();
					
					for(int i=0 ; i < 100000 ; i++){
						String dhtKey = "key" + rn.nextInt();
						pc.get(dhtKey);
						
					}
					long getend = System.nanoTime();
					
					long getduration = TimeUnit.MILLISECONDS.convert((getend - getstart), TimeUnit.NANOSECONDS);
					System.out.println("Sending 100K GET requests takes : " + getduration + " ms." );
					
					//Send 100K DELETE requests
					long delstart = System.nanoTime();
					
					for(int i=0 ; i < 100000 ; i++){
						String dhtKey = "key" + rn.nextInt();
						pc.delete(dhtKey);
						
					}
					long delend = System.nanoTime();
					
					long delduration = TimeUnit.MILLISECONDS.convert((delend - delstart), TimeUnit.NANOSECONDS);
					System.out.println("Sending 100K DELETE requests takes : " + delduration + " ms." );
				}

			}, startdate);
		

			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

        /*
		Random rn = new Random(4);
		
		for(int i=0 ; i < 10 ; i++){
			String dhtKey = "key" + rn.nextInt();
			System.out.println("dhtKey: " + dhtKey);
			System.out.println("index: " + KeyHash.getIndex(dhtKey, 4));
			
		}
		*/

	}

}
