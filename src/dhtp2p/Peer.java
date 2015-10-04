package dhtp2p;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import dhtp2p.peer.PeerClient;
import dhtp2p.peer.PeerServer;
import dhtp2p.utils.Address;
import dhtp2p.utils.Configuration;
import dhtp2p.utils.Worker;


public class Peer {
	
	private int port;
	private long peerIndex;
	private String config;
	private PeerServer pServer;
	private PeerClient pClient;
	
	Peer(long peerIndex, String config) throws FileNotFoundException{
		
		this.peerIndex = peerIndex;
		this.config = config;
		pClient = new PeerClient(config);
		this.port = pClient.getPeerAddress(peerIndex).getPort();
		pServer = new PeerServer(port);
	}
	
	public PeerServer getpServer() {
		return pServer;
	}

	public void setpServer(PeerServer pServer) {
		this.pServer = pServer;
	}

	public PeerClient getpClient() {
		return pClient;
	}

	public void setpClient(PeerClient pClient) {
		this.pClient = pClient;
	}
	
	public void startup(){
		this.pServer.start();
	}
	
	public void exit(){
		this.pServer.close();
	}
	
	 public static void printUsage() {
	    	System.out.println("* * * * * * * * * * * * * * * * * *");
	        System.out.println("*  CS550 PA2: DHT P2P System  *");
	        System.out.println("*                                 *");
	        System.out.println("*      Name: Si Liu               *");
	        System.out.println("*      CWID: A20334820            *");
	        System.out.println("* * * * * * * * * * * * * * * * * *");
	        System.out.println("Commands: PUT, GET, DELETE, EXIT");
	        System.out.println("  [PUT]: Put <key,value> pair to the distributed hash table.");
	        System.out.println("  [GET]: Get the value of the specific key in the distributed hash table.");
	        System.out.println("  [DELETE]: Delete a <key,value> entry in the distributed hash table.");
	        System.out.println("  [EXIT]: Exit this client.");
	        System.out.println("Usage: Input the command or parameter as each promot says.");
	    }
	 

	 
	public static void main(String[] args) {
		
		printUsage();
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("Please input the index for this peer: ");
    	Scanner inputScanner = new Scanner(System.in);
    	String inputRaw = inputScanner.nextLine();
    	String configPath = System.getProperty("user.dir") + "/src/dhtp2p/utils/config.txt" ;
    	
		try {
			Peer peer = new Peer(Integer.parseInt(inputRaw),configPath);
			peer.startup();
			Thread.sleep(10);
			PeerClient pc = peer.getpClient();
			
	    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	    	String userInput;
	    	System.out.println("Please input command:  ");
	    	
	    	while((userInput = br.readLine()) != null){
				
				if(userInput.equalsIgnoreCase("PUT")){
					
					System.out.println("Please input the key to put into the hash table: ");
					String dhtKey = br.readLine();
					System.out.println("Please input the value corresponding to the key: ");
					String dhtValue = br.readLine(); 
				    pc.put(dhtKey, dhtValue);
				    System.out.println("Please input next command:  ");
					
				}else if(userInput.equalsIgnoreCase("GET")){
					
					System.out.println("Please input the key to get the value: ");
					String dhtKey = br.readLine();
					pc.get(dhtKey);
					System.out.println("Please input next command:  ");
					
				}else if(userInput.equalsIgnoreCase("DELETE")){
					
					System.out.println("Please input the key of pair to delete: ");
					String dhtKey = br.readLine();
					pc.delete(dhtKey);
					System.out.println("Please input next command:  ");

				}else if(userInput.equalsIgnoreCase("EXIT")){
					
					System.out.println("Exit this client.");
					peer.exit();
					break;
					
				}else{
					
					System.out.println("This command is not supported yet! ");
					System.out.println("Commands supported:  PUT, GET, DELETE, EXIT");
					System.out.println("Please input command:  ");
				}

			}
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
	}


	
}
