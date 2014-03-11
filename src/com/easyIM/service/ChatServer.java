package com.easyIM.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 
 * @author Tendai T.T. Mudyiwa
 * @author Mehmet Kologlu
 * @since 03/10/2014
 * @version 03/10/2014
 * 
 *          This class implements the server for the chat application. It
 *          listens for requests to connect and processes them on individual
 *          threads
 * 
 */
public class ChatServer {

	private ServerSocket serverSocket;
	private Hashtable<Socket, DataOutputStream> outputStreamTable;

	public ChatServer(int port) {

		listen(port);
	}

	/**
	 * Listens for connection requests and handles them appropriately.
	 * 
	 * @param port
	 *            the port number
	 */
	private void listen(int port) {

		// create sever socket
		try {
			serverSocket = new ServerSocket(port);

			// Log successful socket creation
			System.out.println("Connection Successful!");
			System.out.println("Now listening for messages on port: " + port
					+ '\n');

			while (true) {

				// Receive new connection
				Socket socket = serverSocket.accept();

				// Log that we've received message
				System.out.println("Received connection from " + socket);

				// DataOutputStream for writing data
				DataOutputStream dout = new DataOutputStream(
						socket.getOutputStream());

				// Save this stream so we don't need to make it again
				outputStreamTable.put(socket, dout);

				// Create a chatServerThread
				new ChatServerThread(this, socket);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Switches on the the Server
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Retrieve Command Line Inputs
		int port = Integer.parseInt(args[1]);

		// Log Message
		System.out.println("Command Line argument received :)" + '\n');
		System.out.println("Port:" + '\t' + port + '\n');
		System.out.println("******************************************" + '\n');

		// Create ChatServer object
		new ChatServer(port);

	}

	/**
	 * Sends message to clients
	 * @param message
	 */
	public void sendMessage(String message) {

		// this prevents threads from interfering with each other
		synchronized (outputStreamTable) {

			// loop through all client outputStreams
			for (Enumeration<DataOutputStream> e = getOutputStreams(); e
					.hasMoreElements();) {
				// get the output stream ...
				DataOutputStream dout = (DataOutputStream) e.nextElement();
				// send the message
				try {
					dout.writeUTF(message);
				} catch (IOException ie) {
					System.out.println(ie);
				}
			}
		}

	}

	private Enumeration<DataOutputStream> getOutputStreams() {
		return outputStreamTable.elements();
	}

	/**
	 * Removes connection from dead client
	 * @param socket
	 */
	public void removeConnection(Socket socket) {
		
		synchronized (outputStreamTable) {
			
			//Log for removing socket
			System.out.println("Removing socket "+ socket+"...");
			outputStreamTable.remove(socket);
			
			try {
				socket.close();
			} catch (IOException e) {
				
				System.out.println("Failed to remove socket");
				e.printStackTrace();
			}
			
		}

	}

}
