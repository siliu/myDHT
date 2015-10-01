package dhtp2p;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import dhtp2p.peer.PeerClient;
import dhtp2p.utils.KeyHash;

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
			
			Timer timer = new Timer();
			Date startdate = new Date(115,9,1,10,40,00);
			
			System.out.println("Task start time: " + startdate);
		
			timer.schedule(new TimerTask() {
				public void run(){
			
					Random rn = new Random(Integer.parseInt(inputRaw));
					
					long starttime = System.nanoTime();
					
					for(int i=0 ; i < 10 ; i++){
						String dhtKey = "key" + rn.nextInt();
						String dhtValue = "value";
						pc.put(dhtKey, dhtValue);
						
					}
					
					long endtime = System.nanoTime();
					
					long duration = endtime - starttime;
					
					System.out.println("Sending 10 put requests takes : " + duration + "ns ." );
	
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
