package socket;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientServer {
	private ObjectOutputStream output; // output stream to server
	private ObjectInputStream input; // input stream from server
	private String message = ""; // message from server
	private String chatServer; // host server for this application
	private Socket client; // socket to communicate with server
	private InputStream inputStream; // input stream from server
	private OutputStream outputStream; // input stream from server

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public ObjectOutputStream getOutput() {
		return output;
	}

	public void setOutput(ObjectOutputStream output) {
		this.output = output;
	}

	public ObjectInputStream getInput() {
		return input;
	}

	public void setInput(ObjectInputStream input) {
		this.input = input;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getChatServer() {
		return chatServer;
	}

	public void setChatServer(String chatServer) {
		this.chatServer = chatServer;
	}

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	public ClientServer(String host) {
		chatServer = host;

	}

	public void runClient() {
		try // connect to server, get streams, process connection
		{
			connectToServer(); // create a Socket to make connection
			getStreams(); // get the input and output streams
		} // end try
		catch (EOFException eofException) {
			System.out.println("连接失败");
		} // end catch
		catch (IOException ioException) {
			System.out.println("连接失败");
			ioException.printStackTrace();
		} // end catch

	}

	private void connectToServer() throws UnknownHostException, IOException {
		System.out.println("正在尝试连接。。。");
		client = new Socket(InetAddress.getByName(chatServer), 12345);
		System.out.println(client);
		System.out.println("连接到" + client.getInetAddress().getHostName());

	}

	private void getStreams() throws IOException {

		output = new ObjectOutputStream(client.getOutputStream());
		output.flush();
//出问题
		input = new ObjectInputStream(client.getInputStream());

		inputStream = client.getInputStream();
		outputStream = client.getOutputStream();
	}

	public void closeConnection() {
		System.out.println("关闭连接");

		try {
			output.close(); // close output stream
			input.close(); // close input stream
			client.close(); // close socket
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
		} // end catch
	}

}
