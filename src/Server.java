import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

public class Server {

	private static Map<String, Integer> map;

	public static void main(String[] args) {
		map = new LinkedHashMap<String, Integer>();
		ServerThread serverThread = new ServerThread();
		serverThread.start();

	}

	public static void storeRequest(String key) {
		if (map.get(key) == null) {
			map.put(key, 1);
		} else {
			map.put(key, map.get(key) + 1);
		}
	}

	public static void printRequest(String key) {
		System.out.println(key + map.get(key));
	}

}

class ServerThread extends Thread {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = null;
		Socket socket = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			serverSocket = new ServerSocket(8080);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (true) {

			try {

				System.out.println("LISTENING");
				socket = serverSocket.accept();

				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String input = "";
				String temp = "";
				String[] array = null;

				String filename = "";
				if ((temp = bufferedReader.readLine()) != null) {
					System.out.println("   " + temp);
					array = temp.split(" ");
					System.out.println("got it  " + array[1].substring(1));
					filename = array[1].substring(1);
					if (temp.length() < 1) {
						break;
					}
					input += temp;
				}
				// System.out.println(input);

				// + "Content-Length: " + (res.length()) + "\n\n";

				SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");

				System.out.println();

				File filePath = new File(new File(System.getProperty("user.dir")).getParent() + File.separator + "www"
						+ File.separator + filename);

				if (!Files.exists(Paths.get(filePath.getPath()))) {
					System.out.println(" not exist >>>>>" + filePath.getPath());
					String res = "HTTP/1.1 404 Not Found\n\n"
							+ "<html><head></head>  <body><h1> file you requested is not found, please provide "
							+ "valid file name </h1></body> </html>";
					outputStream.write(res.getBytes());
				} else {
					System.out.println("inside ..." + filePath.getPath());
					String send = "HTTP/1.0 200 OK\n" + "Server: HTTP server/0.1\n" + "Date: "
							+ format.format(new java.util.Date()) + "\n" + "Content-type: " + "html"
							+ "Content-Length: " + (filePath.length()) + "\n\n";

					outputStream.write(send.getBytes());

					Files.copy(Paths.get(filePath.toString()), outputStream);
					String sAddr=socket.getInetAddress()+"";
					sAddr=sAddr.substring(1);
					
					String key = "/" + filename + "|" + sAddr + "|" + socket.getPort() + "|";

					Server.storeRequest(key);
					Server.printRequest(key);

					// System.out.println(" filePath.toString() "+filePath.toString());
				}
				// outputStream.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				try {
					socket.close();
					// serverSocket.close();
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

}
