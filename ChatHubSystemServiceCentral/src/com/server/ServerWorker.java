/**
 * Class implements ServerSockets accept and run methods to start work execution in own Thread 
 */
package com.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class implements ServerSockets accept and run methods to start work execution
 * in own Thread
 * 
 * @author ejoverk
 *
 */
public class ServerWorker implements Runnable {

	private ServerSocket serverSocket;
	private int clientId;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;

	public ServerWorker(ServerSocket serverSocket, int clientId) {
		this.serverSocket = serverSocket;
		this.clientId = clientId;

	}

	@Override
	public void run() {
		System.out.println("Run Methods Success Listen on Port: " + serverSocket.getLocalPort());
		System.out.println("RUN CONNECTION PAIRS ON PORTS");

		try {

			Socket socketToClient = serverSocket.accept();
			System.out.println("ACCEPTED ON SERVERSIDE ON PORT ON JAVAARCH: " + socketToClient.getPort() + ", "
					+ socketToClient.getLocalPort() + ", " + serverSocket.getLocalPort());

			inFromClient = new BufferedReader(new InputStreamReader(socketToClient.getInputStream()));
			outToClient = new DataOutputStream(socketToClient.getOutputStream());

			// Read data from Client, start Communication, later a smart way
			// implements

			String msg = null;

			while (true) {
				if (inFromClient.ready()) {
					System.out.println("SERVER: ");
					msg = receiveMsg(inFromClient);
					System.out.println("READED FROM CLIENT SENDER: " + msg);
				} else if (!inFromClient.ready()) {
					sendMsg(outToClient, msg);
					System.out.println("CLIENT: " + clientId);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean sendMsg(DataOutputStream outStream, String sndMsg) {
		boolean status = true;
		System.out.println("Sending Msg: " + sndMsg);

		try {
			outStream.writeBytes(sndMsg + "\n");
		} catch (IOException ex) {
			System.out.println("Cannot send a message");
			ex.printStackTrace();
			status = false;
		}

		return status;
	}

	public String receiveMsg(BufferedReader inStream) {
		String rcvMsg = null;
		try {
			rcvMsg = inStream.readLine();
		} catch (IOException ex) {
			System.out.println("Cannot read a message");
			ex.printStackTrace();
		}

		return rcvMsg;

	}

}
