import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket serverSocket=null;
		Socket socket=null;
		InputStream inputStream=null;
		OutputStream outputStream=null;
		try {
		    serverSocket = new ServerSocket(8080);
		    System.out.println("LISTENING");
		    socket= serverSocket.accept();
			
			 inputStream =  socket.getInputStream();
			 outputStream= socket.getOutputStream();
			 String res="<html> <body><h1>how are you</h1><body> </html>";
			 String req = "HTTP/1.0 200 OK\n"
				
				
				+"Content-Length: " + (res.length()) + "\n\n";

				String header = req  + res;
			 outputStream.write(header.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			
			try {
				socket.close();
				serverSocket.close();
				inputStream.close();
				outputStream.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("closed");
		}

	}

}
