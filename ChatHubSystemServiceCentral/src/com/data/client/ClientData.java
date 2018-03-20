/**
 * Model of data of Client in DataBase
 */
package com.data.client;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Model of data of Client in DataBase
 * 
 * @author ejoverk
 *
 */
public class ClientData {
	private int clientId;
	private String clientName;
	private InetAddress clientUrl;
	private int clientPortId;

	public ClientData(int clientId, String clientName, InetAddress clientUrl) {
		this.clientId = clientId;
		this.clientName = clientName;
		this.clientUrl = clientUrl;
	}

	public void generatePortId() {
		Connection connection = getConnectionToDB("client_data");
		int readedPort = executeQuery("SELECT * FROM server_connection_port WHERE client_id = ?", connection);
		this.clientPortId = readedPort;
	}

	public int getClientPortId() {
		return clientPortId;
	}

	public int getClientId() {
		return clientId;
	}

	public String getClientName() {
		return clientName;
	}

	public InetAddress getClientUrl() {
		return clientUrl;
	}

	public Connection getConnectionToDB(String database) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, "postgres",
					"admin");
		} catch (SQLException ex) {
			System.out.println("Cannot create Connection to DataBase: " + database);
		}

		return connection;
	}

	public int executeQuery(String sqlQuery, Connection connection) {
		int readedPortFromDB = 0;
		try {
			PreparedStatement pst = connection.prepareStatement(sqlQuery);
			pst.setInt(1, clientId);

			ResultSet resultIDs = pst.executeQuery();
			while (resultIDs.next()) {
				if (resultIDs.getInt(1) == clientId)
					readedPortFromDB = resultIDs.getInt(2);
			}

		} catch (SQLException ex) {
			System.out.println("Cannot execute query: " + sqlQuery);
			ex.printStackTrace();
		}

		return readedPortFromDB;
	}
}
