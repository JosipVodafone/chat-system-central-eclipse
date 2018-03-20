/**
 * Scan DataBase on Clients table on Clients in DB, open Socket Connection on ports, write in table on Database Server
 */
package com.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author ejoverk
 *
 */
public class ServerHandlerConnector {

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {

		Map<Integer, ServerSocket> serverSocket = new HashMap<>();

		System.out.println("Scannig DB on Clients and open Connection port ...");

		Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/client_data", "postgres",
				"root");

		PreparedStatement deleteAll = con.prepareStatement("DELETE FROM server_connection_port");
		System.out.println("Successfully DELETED " + deleteAll.executeUpdate() + " ROWS");

		PreparedStatement pst = con.prepareStatement("SELECT * FROM clients");
		ResultSet query = pst.executeQuery();

		while (query.next()) {

			System.out.println("QUERY RESULT: " + query.getInt(1) + ", " + query.getString(2).trim() + ", "
					+ query.getString(3).trim());
			System.out.println("Client ID: " + query.getInt(1));

			int clientId = query.getInt(1);
			int port = generatePortOnServerSide(clientId);

			try {
				serverSocket.put(clientId, new ServerSocket(port));
			} catch (IOException ex) {
				System.out.println("Cannot create Server Socket connection");
				ex.printStackTrace();
			}

			System.out.println("Connect to Database and write open ports to Clients");
			PreparedStatement writeToDB = con
					.prepareStatement("INSERT INTO server_connection_port (client_id,server_port) VALUES (?,?)");
			writeToDB.setInt(1, clientId);
			writeToDB.setInt(2, port);

			if (writeToDB.executeUpdate() > 0)
				System.out.println("Successfully added rows number");

		}

		System.out.println("Successfully finished and created: ");

		for (Integer key : serverSocket.keySet()) {
			System.out.println(serverSocket.get(key));
		}

		System.out.println("FOREACH CONNECTION PAIR, FIRST JUST ONE CONNECTION PAIRS, LATER ON DB");

		System.out.println("Open Connection in Own Thread each port");

		int count = 1;

		while (count < 3) {
			try {
				Thread serverWorkerThread = new Thread(new ServerWorker(serverSocket.get(count), count));
				serverWorkerThread.start();
			} catch (IllegalThreadStateException ex) {
				System.out.println(
						"Cannot create Server Socket accept and cannot listen on port: " + serverSocket.toString());
				ex.printStackTrace();
			}

			// serverSocket.remove(key);
			count++;
		}

	}

	public static int generatePortOnServerSide(int clientId) {
		// TODO Implement better method to generate Ports, connect to Web
		// Service, Universal Work, further versions
		int generatedPort = clientId + 4000;
		return generatedPort;

	}

}
