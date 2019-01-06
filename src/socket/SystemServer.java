package socket;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class SystemServer {

	private ServerSocket server; // server socket

	public SystemServer() {

	}

	public void runServer() throws SQLException {
		try {
			server = new ServerSocket(12345, 100);
			System.out.println("服务器启动");
			while (true) {

				Socket connection = server.accept();
				new ServerThread(connection).start();
				;

			}
		} catch (IOException e) {

			// e.printStackTrace();
			System.out.println("服务器关闭");
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws SQLException {
		SystemServer systemServer = new SystemServer();
		systemServer.runServer();
	}

}
