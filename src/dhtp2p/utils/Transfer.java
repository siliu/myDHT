/**
 * 
 */
package dhtp2p.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.google.gson.Gson;

/**
 * @author siliu 
 * Transfer class is to deal with the message transfer Message is composed by two parts: command and content
 */
public class Transfer {

	// Receive message from ObjectInputStream 
	// NOTE: PA1 uses GSON,which doesn't work in keeping socket connection for multiple requests
	public static Message receiveMessage(InputStream is) {
		
		Message msg = new Message();

		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(is);
			msg = (Message) ois.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;

	}

	// Send message by converting it to ObjectOutputStream
	public static void sendMessage(Message msg, OutputStream os) {
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(msg);
			oos.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
