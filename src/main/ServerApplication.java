package main;

import java.sql.SQLException;

import socket.SystemServer;

public class ServerApplication {
	public static void main(String[] args) {
		SystemServer systemServer = new SystemServer();
		try {
			systemServer.runServer();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

}
