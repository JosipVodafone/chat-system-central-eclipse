/**
 * Interface to handle Client request
 */
package com.client;

import java.net.InetAddress;
import java.net.Socket;

/**
 * Interface to handle Client request
 * 
 * @author ejoverk
 *
 */
public interface ClientIf {
	// public Socket openSocketConnection(ClientData client) throws IOException;
	public void connectToReceiver(Socket clientSocket, InetAddress receiver);

}
