import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class is responsible to listening to client who want to connect
 * Respond a new thread to handle them
 * @author maha_
 *
 */

public class Server {
  
	private ServerSocket serverSocket;
	
	/**
	 * 
	 * @param serverSocket is responsible for listening incomming connections 
	 */
	
	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	public void startServer() {
		
		try {
			System.out.println("Server Started ....");
			while(!serverSocket.isClosed()) {
				
				/**
				 * accept -> program will be haulted here until the clients connect
				 */
				Socket socket = serverSocket.accept(); 
				System.out.println("A new client has joined the chat room");
				
				/**
				 * Each object of the Multithreading class will be responsible for communicating with client
				 */
				MultiThreading clientHandler = new MultiThreading(socket); 
				
				/**
				 * the multithreading class implements the runnable interface so the instance of the class
				 *  is executed by a seperate thread
				 */
				Thread thread = new Thread(clientHandler);
				thread.start();
			}
		}catch (IOException e) {
			
		}
	}
	
	public void closeServerSocket() {
		try {
			if(serverSocket != null) {
				serverSocket.close();
			}
		}catch (IOException e) {
			// handle exception
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		/**
		 * Server is listening to client that are making connection at this port number
		 */
		ServerSocket serverSocket = new ServerSocket(9999);
		Server server = new Server(serverSocket);
		server.startServer();
	}
}
