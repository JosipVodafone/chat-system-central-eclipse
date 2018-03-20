/**
 * Create and start Client, connect to the server. Implement web-service web Server on Url connection
 */
package com.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.data.client.ClientData;

/**
 * Create and start Client, connect to the server. Implement web-service web
 * Server on URL connection
 * 
 * @author ejoverk
 *
 */
public class Client {

	/**
	 * @param args
	 * @throws IOException
	 * @throws SQLException
	 * 
	 */
	public static void main(String[] args) throws IOException, SQLException {

		int clientId = Integer.parseInt(args[0]);
		String clientName = args[1];
		String hostName = args[2];

		ClientData client = new ClientData(clientId, clientName, InetAddress.getByName(hostName));

		client.generatePortId();
		System.out.println("Generated Port Test: " + client.getClientPortId());

		Socket clientSocket = openSocketConnection(client);

		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Insert Receiver Client ID: ");
		String request = inFromClient.readLine();

		// First version on Connection Pair on ID-s
		int rcvclientId = Integer.parseInt(request);
		connectToReceiverOnDB(client.getClientId(), rcvclientId);

		sendMsg(clientSocket, inFromServer, outToServer, inFromClient, null);

		// Later version TO DO
		// InetAddress receiver = InetAddress.getByName(request);
		// connectToReceiver(clientSocket, receiver, inFromServer, outToServer);

	}

	public static void sendMsg(Socket clientSocket, BufferedReader inFromServer, DataOutputStream outToServer,
			BufferedReader inFromClient, InetAddress receiver) {

		try {

			System.out.println(
					"Insert your Messagge, just on breaking to handle more clients test, later inserting receiver address.");
			String readedClient = inFromClient.readLine();
			System.out.println("READED ON CLIENT AND SEND MESSAGE: " + readedClient);
			outToServer.writeBytes(readedClient + "\n");

			System.out.println("Trying to read on Client Side on port on JAVAARCH: " + clientSocket.getPort() + ", "
					+ clientSocket.getLocalPort());
			String readed = inFromServer.readLine();
			System.out.println("SERVER: " + readed);

		} catch (IOException e) {
			System.out.println("Cannot create Socket Connection to Server");
		}
	}

	public static void connectToReceiver(Socket clientSocket, InetAddress receiver, BufferedReader inFromServer,
			DataOutputStream outToServer) {

	}

	public static void connectToReceiverOnDB(int clientIdSnd, int clientIdRcv) throws SQLException {

		int connection_pairId = 1;

		Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/client_data", "postgres",
				"admin");
		PreparedStatement pstLoad = connection.prepareStatement("SELECT connection_id FROM connections_pairs");

		ResultSet resultId = pstLoad.executeQuery();

		while (resultId.next()) {
			if (resultId.isLast())
				connection_pairId = resultId.getInt(1) + 1;
		}

		System.out.println("CONNECTION ID NUMBER: " + connection_pairId);
		PreparedStatement pst = connection
				.prepareStatement("INSERT INTO connections_pairs (connection_id,client_id1,client_id2) VALUES(?,?,?)");

		pst.setInt(1, connection_pairId);
		pst.setInt(2, clientIdSnd);
		pst.setInt(3, clientIdRcv);

		// pst.execute();

		if (pst.executeUpdate() > 0)
			System.out.println("Created Connection pair on ID number: " + connection_pairId);
	}

	public void connectToGroup(Socket clientSocket, String groupName) {

	}

	public static Socket openSocketConnection(ClientData client) throws IOException {
		Socket clientSocket = new Socket(client.getClientUrl(), client.getClientPortId());
		return clientSocket;
	}

}
