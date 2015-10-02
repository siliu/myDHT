package dhtp2p.peer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import dhtp2p.utils.Address;
import dhtp2p.utils.Command;
import dhtp2p.utils.Configuration;
import dhtp2p.utils.KeyHash;
import dhtp2p.utils.Message;
import dhtp2p.utils.Transfer;

public class PeerClient {
	
	private Socket socket;
	private String config;
	private int peerNum;
	private static Map<Long, Address> addressTable = new ConcurrentHashMap<Long, Address>();
	
	//Cache the old socket used for communication with other servers for reuse in the future communication
	private Map<Address, Socket> socketCache = new ConcurrentHashMap<Address, Socket>(); 
	
	public PeerClient(String config) throws FileNotFoundException{
		this.config = config;
		this.addressTable = Configuration.load(config);
		this.peerNum = addressTable.size();
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public synchronized Address lookupAddressTable(long peerIndex){
		
		Address peerAddress = null;
		if(addressTable.containsKey(peerIndex))
			peerAddress = addressTable.get(peerIndex);
		return peerAddress;
	}
	
	private void getSocket(Address peerAddress){
		
		if(socketCache.containsKey(peerAddress)){
			this.socket = socketCache.get(peerAddress);	
		}else{
			try {
				this.socket = new Socket(peerAddress.getHostname(),peerAddress.getPort());
				socketCache.put(peerAddress,this.socket);
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * checkMessage function is to check if the IndexServer returned OK command to the peer.
	 * @param msgIn : The input message to check
	 * @return
	 */
	private boolean checkMessage(Message msgIn){
		
		if(msgIn != null && msgIn.getCmd() != null && msgIn.getCmd().equals(Command.OK))
			return true;
		
		return false;
	}
	
	public void put(String dhtKey, String dhtValue){
		
		//System.out.println("Client dhtKey 2: " + dhtKey);
		try {
			int peerIndex = KeyHash.getIndex(dhtKey, peerNum);
//			System.out.println("This <key,value> pair should go to peer: " + peerIndex);
			Address peerAddress = lookupAddressTable(peerIndex);
//			System.out.println("The address of this peer is: " + peerAddress.getHostname() + "/" + peerAddress.getPort());
			
//			socket = new Socket(peerAddress.getHostname(), peerAddress.getPort());
			this.getSocket(peerAddress);
			
			Map<String, Object> putMap = new HashMap<String,Object>();
			putMap.put("dhtKey", dhtKey);
			putMap.put("dhtValue", dhtValue);
		
			Message msgOut = new Message(Command.PUT,putMap);
			Transfer.sendMessage(msgOut, this.socket.getOutputStream());
//			this.socket.shutdownOutput();
			
			Message msgIn = Transfer.receiveMessage(this.socket.getInputStream());

//			if(checkMessage(msgIn)){
//				System.out.println(msgIn.getContent());
//			}else{
//				System.out.println(msgIn.getContent());
//			}
//			this.socket.shutdownInput();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void get(String dhtKey){
		
		try {
			int peerIndex = KeyHash.getIndex(dhtKey, peerNum);
//			System.out.println("The <key,value> pair is on peer: " + peerIndex);
			Address peerAddress = lookupAddressTable(peerIndex);
//			System.out.println("The address of this peer is: " + peerAddress.getHostname() + "/" + peerAddress.getPort());
			
			//socket = new Socket(peerAddress.getHostname(), peerAddress.getPort());
			this.getSocket(peerAddress);
			
			Map<String, Object> getMap = new HashMap<String,Object>();
			getMap.put("dhtKey", dhtKey);
		
		
			Message msgOut = new Message(Command.GET,getMap);
			Transfer.sendMessage(msgOut, this.socket.getOutputStream());
//			this.socket.shutdownOutput();
			
			//Set the peerId for this peer

			Message msgIn = Transfer.receiveMessage(this.socket.getInputStream());
//			if(checkMessage(msgIn)){
//				System.out.println("The value of this key is: " + msgIn.getContent());
//			}else{
//				System.out.println("This <key,value> pair is not avaliable!");
//			}
//			this.socket.shutdownInput();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void delete(String dhtKey){
		
		try {
			int peerIndex = KeyHash.getIndex(dhtKey, peerNum);
//			System.out.println("This <key,value> pair is on peer: " + peerIndex);
			Address peerAddress = lookupAddressTable(peerIndex);
//			System.out.println("The address of this peer is: " + peerAddress.getHostname() + "/" + peerAddress.getPort());
			
			//socket = new Socket(peerAddress.getHostname(), peerAddress.getPort());
			this.getSocket(peerAddress);
			
			Map<String, Object> deleteMap = new HashMap<String,Object>();
			deleteMap.put("dhtKey", dhtKey);
		
			Message msgOut = new Message(Command.DELETE,deleteMap);

			Transfer.sendMessage(msgOut, this.socket.getOutputStream());
//			this.socket.shutdownOutput();
			
			//Set the peerId for this peer

			Message msgIn = Transfer.receiveMessage(this.socket.getInputStream());
//			if(checkMessage(msgIn)){
//				System.out.println(msgIn.getContent());
//			}else{
//				System.out.println(msgIn.getContent());
//			}
//			this.socket.shutdownInput();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	
}
