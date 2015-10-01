package dhtp2p;

import java.util.Random;
import java.util.Scanner;

public class TestRandom {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Please input the index for this peer: ");
    	Scanner inputScanner = new Scanner(System.in);
    	String inputRaw = inputScanner.nextLine();
		Random rn = new Random(Integer.parseInt(inputRaw));

		
		for(int i=0 ; i < 10 ; i++){
			String dhtKey = "key" + rn.nextInt();
			//pc.put(dhtKey, dhtValue);
			System.out.println("dhtKey: " + dhtKey);
		}

	}

}
