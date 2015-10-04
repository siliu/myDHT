package dhtp2p.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author siliu
 * Worker class is too implement the function of PeerServer
 */
public class Worker extends Thread {

	private Socket socket;
	private Map<String, Object> localTable = new ConcurrentHashMap<String, Object>();
	
	
	public Worker( Socket socket,Map<String, Object> localTable){
		this.socket = socket;
		this.localTable = localTable;
		
	}
	
	//Get the peer address with corresponding peer index

		
	
	public synchronized boolean put(String dhtKey, String dhtValue){
		
		boolean result = false;
		
		//System.out.println("Worker dhtKey 1: " + dhtKey);
		if(! localTable.containsKey(dhtKey)){
			localTable.put(dhtKey, dhtValue);
			result = true;
			System.out.println("The current table content on this peer is: ");
			MapOperator.printMap(localTable);
		}
		
		return result;
	}
	
	public synchronized String get(String dhtKey){
		
		return (String) localTable.get(dhtKey);
	}
	
	public synchronized boolean delete(String dhtKey){
		
		boolean result = false;
		
		if(localTable.containsKey(dhtKey)){
			localTable.remove(dhtKey);
			result = true;
			System.out.println("The current table content on this peer is: ");
			MapOperator.printMap(localTable);
		}
		
		return result;
	}
	
	public void run(){
		
		try {

			while(socket.isConnected()){
				
				InputStream is = socket.getInputStream();
				Message msg = Transfer.receiveMessage(is);
				Command cmd = msg.getCmd(); 
				Map<String, Object> receivedMap = (Map<String, Object>) msg.getContent();
				//TODO while(cmd != exit)

				if(cmd == Command.PUT) {
					
					String dhtKey = (String) receivedMap.get("dhtKey");
					String dhtValue = (String) receivedMap.get("dhtValue");
					
					Message returnMsg;
					if(this.put(dhtKey,dhtValue)){
						returnMsg = new Message(Command.OK, "Put success!");
						
					}else{
						returnMsg = new Message(Command.ERROR, "Put failed!");
					}
					
					Transfer.sendMessage(returnMsg,this.socket.getOutputStream());
					
					//socket.shutdownOutput();
					
				}if(cmd == Command.GET){
			
					String dhtKey = (String) receivedMap.get("dhtKey");

					String dhtValue  = this.get(dhtKey);
					Message returnMsg = new Message(Command.OK, dhtValue);
					Transfer.sendMessage(returnMsg, this.socket.getOutputStream());
//					socket.shutdownOutput();
					
				}if(cmd == Command.DELETE){
					
					String dhtKey = (String) receivedMap.get("dhtKey");
					
					Message returnMsg;
					if(this.delete(dhtKey)){
						returnMsg = new Message(Command.OK, "Delete success!");
						
					}else{
						returnMsg = new Message(Command.ERROR, "Delete failed!");
					}
					
					Transfer.sendMessage(returnMsg,this.socket.getOutputStream());
					
//					socket.shutdownOutput();
					
				}

				
			}



		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
