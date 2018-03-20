package com.database;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.management.Query;

import com.data.client.ClientData;

/**
 * Test connection to Database, create and update tables of clients
 */

/**
 * Test connection to Database, create and update tables of clients
 * 
 * @author ejoverk
 *
 */
public class DBConnectionDemoUpdate {

	/**
	 * @param args
	 * @throws SQLException
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws SQLException, UnknownHostException {

		int clientId = Integer.parseInt(args[0]);
		String clientName = args[1];
		String hostName = args[2];

		ClientData client = new ClientData(clientId, clientName, InetAddress.getByName(hostName));
		Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/client_data", "postgres",
				"admin");
		/*
		 * PreparedStatement stmt = con
		 * .prepareStatement("insert into clients (client_id,client_name,client_host) values(?,?,?)"
		 * );
		 * 
		 * stmt.setInt(1, clientId); stmt.setString(2, clientName);
		 * stmt.setString(3, hostName);
		 * 
		 * stmt.execute();
		 */
		PreparedStatement stmt = con.prepareStatement("select client_id from clients");

		ResultSet result = stmt.executeQuery();

		/*
		 * while (result.next()) { System.out.println("SHOWING: " +
		 * result.getInt(1) + ", " + result.getString(2) + "," +
		 * result.getString(3)); }
		 */

		int id_last = 1;

		while (result.next()) {
			if (result.isLast())
				id_last = result.getInt(1);

		}
		System.out.println("Size of Columns One And Index of ID of Last one: " + id_last);
	}

}
