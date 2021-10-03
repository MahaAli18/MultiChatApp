import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String userName;

	public Client(Socket socket, String userName) {

		try {
			this.socket=socket;
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.userName = userName;
		}catch (IOException e) {
			closeEverything(socket, bufferedReader, bufferedWriter);
		}

	}

	public void sendMessage() {
		try {
			bufferedWriter.write(userName);
			bufferedWriter.newLine();
			bufferedWriter.flush();

			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			while(socket.isConnected()) {
				String messageString = scan.nextLine();
				bufferedWriter.write(userName + ": " + messageString);
				bufferedWriter.newLine();
				bufferedWriter.flush();
			}

		}catch (IOException e) {
			closeEverything(socket, bufferedReader, bufferedWriter);
		}
	}

	public void listenMessage() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String msgFromGroupChatString;
				while (socket.isConnected()) {
					try {
						msgFromGroupChatString=bufferedReader.readLine();
						System.out.println(msgFromGroupChatString);

					} catch (IOException e) {
						closeEverything(socket, bufferedReader, bufferedWriter);
					}
				}

			}
		}).start();
	}

	public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		try {
			if(bufferedReader != null) {
				bufferedReader.close();
			}
			if(bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (socket != null) {
				socket.close();
			}
		}catch (IOException e) {
			// handle exception
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter your Name to join the chat room: ");
		String userNameString = scan.nextLine();
		Socket socket = new Socket("localhost", 9999);
		Client client = new Client(socket, userNameString);
		client.listenMessage();
		client.sendMessage();
		
	}

}
