import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

public class Server {

	private static Map<String, Integer> map;

	public static void main(String[] args) {

		map = new LinkedHashMap<String, Integer>();
		try {
			System.out.println("Server listening : " + InetAddress.getLocalHost().getCanonicalHostName());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("PORT NUMBER : " + 8080);

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

				// System.out.println("LISTENING");
				socket = serverSocket.accept();

				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				// String input = "";
				String temp = "";
				String[] array = null;

				String filename = "";
				boolean isGetReq = false;
				if ((temp = bufferedReader.readLine()) != null) {

					if (temp.startsWith("GET")) {
						isGetReq = true;
					}
					array = temp.split(" ");

					filename = array[1].substring(1);
				}
				// isGetReq : to check whether its get request or not
				if (isGetReq) {

					// date format
					SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
					format.setTimeZone(TimeZone.getTimeZone("EST"));
					// one directory up - to read objects from www directory
					File path = new File(new File(System.getProperty("user.dir")).getParent());
					path = new File(path + "/..");

					File filePath = new File(
							path.getCanonicalPath() + File.separator + "www" + File.separator + filename);
					// if invalid file requested
					if (!Files.exists(Paths.get(filePath.getPath()))) {

						// System.out.println(" not exist >>>>>" + filePath.getPath());
						String res = "HTTP/1.1 404 Not Found\n\n"
								+ "<html><head></head>  <body><h1> file you requested is not found, please provide "
								+ "valid file name </h1></body> </html>";
						outputStream.write(res.getBytes());

					} else {

						// to serve valid get request
						// response header content
						String send = "HTTP/1.0 200 OK\n" + "Server: HTTP web server/0.1\n" + "Date: "
								+ format.format(new java.util.Date()) + "\n" + "Content-type: "
								+ Files.probeContentType(filePath.toPath()) + "\n" + "Content-Length: "
								+ (filePath.length()) + "\n\n";
						// System.out.println(send);
						outputStream.write(send.getBytes());
						
                        //copy requested file
						Files.copy(Paths.get(filePath.toString()), outputStream);
						String sAddr = socket.getInetAddress() + "";
						sAddr = sAddr.substring(1);

						String key = "/" + filename + "|" + sAddr + "|" + socket.getPort() + "|";
						// storing request in map to keep count
						Server.storeRequest(key);
						Server.printRequest(key);

						// System.out.println(" filePath.toString() "+filePath.toString());
					}
					// outputStream.close();
				} else {
					String res = "HTTP/1.1 404 Not Found\n"
							+ "<html><head></head>  <body><h1> we are serving only GET request"
							+ "</h1></body> </html>";
					outputStream.write(res.getBytes());
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				String res = "HTTP/1.1 404 Not Found\n" + "<html><head></head>  <body><h1> provide valid request"
						+ "</h1></body> </html>";
				try {
					outputStream.write(res.getBytes());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
			}
		}

	}

}
