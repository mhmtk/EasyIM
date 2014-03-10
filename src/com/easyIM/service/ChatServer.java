package com.easyIM.service;
import java.io.IOException;
import java.net.ServerSocket;


/**
 * 
 * @author Tendai T.T. Mudyiwa
 * @author Mehmet Kologlu
 * @since	03/10/2014
 * @version 03/10/2014
 * 
 * This class implements the server for the chat application.
 * It listens for requests to connect and processes them on
 * individual threads
 *
 */
public class ChatServer {
	
	private ServerSocket serverSocket;
	
	public ChatServer(int port){
		

		listen(port);
	}
	
	private void listen(int port) {
		
		//create sever socket
		try {
			serverSocket = new ServerSocket(port);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args){
		
		//Retrieve Command Line Inputs
		int port = Integer.parseInt(args[1]);
		int IP_ADDRESS = Integer.parseInt(args[0]);
		
		//Log Message
		System.out.println("Command Line arguments received :)"+'\n');
		System.out.println("IP:"+'\t'+IP_ADDRESS+'\t'+"Port:"+'\t'+ port+'\n');
		System.out.println("******************************************"+'\n');
		
		//Create ChatServer object
		new ChatServer(port);
		
	}

}
