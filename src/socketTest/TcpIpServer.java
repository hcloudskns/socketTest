
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.websocket.DeploymentException;


public class TcpIpServer {
	public static void main(String args[]) {
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		Map<String, Client> clients = new HashMap<String, Client> ();
		
		try {
				serverSocket = new ServerSocket(10001);
				System.out.println("10001번 서버가 준비되었습니다.");
				//socket = serverSocket.accept();
				
				while(true){
					
					socket = serverSocket.accept();
					
					Client c = new Client(socket.getInetAddress().toString(), Integer.toString(socket.getPort()),socket);
					clients.put(socket.getInetAddress().toString()+Integer.toString(socket.getPort()) , c);
					//System.out.println("IP: " + socket.getInetAddress().toString()+ " , " + Integer.toString(socket.getPort())) ;
	
					ServerReceiver receiver = new ServerReceiver(socket.getInetAddress().toString()+Integer.toString(socket.getPort()),clients );
					//sender.start();
					receiver.start();
				}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} // main
		
} // class


class ServerReceiver extends Thread {
	Socket socket;
	DataInputStream in;

	ServerReceiver(String key,Map<String, Client> clients) {
		
		Client c = clients.get(key);
		this.socket = c.getSocket();
		try {
			in = new DataInputStream(socket.getInputStream());
		} catch(IOException e) {}

	}

	public synchronized void run() {
		while(in!=null) {
			
				Connection conn = null;
				PreparedStatement pstmt = null;
				int ret = 0;
				String strData = "";
				try {
					
					byte[] buffer = new byte[8096];
					ret = in.read(buffer, 0, buffer.length);					
					strData = new String((Arrays.copyOfRange(buffer, 0, ret)),"UTF-8");
					
					System.out.println("Received Data From Polish Client: " + strData);
					
					//JSONParser jParser = new JSONParser();
					//JSONObject jObject;
				
					//String strByte = strData.substring(0, 7);
					//String strRealData = strData.substring(25,strData.length()-2);
					
					
					//jObject = (JSONObject)jParser.parse(strRealData);
					/*int byte_count = Integer.parseInt(strByte);
					String event_type = (String)jObject.get("event_type");
					String ip_address = (String)jObject.get("ip_address");
					String device_type = (String)jObject.get("device_type");
					String device_name = (String)jObject.get("device_name");
					String location = (String)jObject.get("location");
					String usage = (String)jObject.get("usage");
					String event_time = (String)jObject.get("event_time");
					String event_code = (String)jObject.get("event_code");
					String event_msg = (String)jObject.get("event_msg");
					String detail_msg = (String)jObject.get("detail_msg");*/
					/*					
					Class.forName("org.mariadb.jdbc.Driver");	
					conn = DriverManager.getConnection("jdbc:mariadb://192.168.101.110:3306/HCLOUD","root", "P@$$w0rd");
					pstmt = conn.prepareStatement("insert into TB_SYSTEM_LOG (BYTE_COUNT, EVENT_TYPE, IP_ADDRESS, DEVICE_TYPE, DEVICE_NAME, LOCATION, USAGE_STATUS, EVENT_TIME, EVENT_CODE, EVENT_MSG, DETAIL_MSG, VIEW_COUNT, CONFIRM_STATUS, REGISTER_ID, REGISTER_DATE, MODIFIER_ID, MODIFY_DATE)" +
							                      " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, ? , ?, now(), ?, now())");
					
					pstmt.setInt(1, byte_count);
					pstmt.setString(2, event_type);
					pstmt.setString(3, ip_address);
					pstmt.setString(4, device_type);
					pstmt.setString(5, device_name);
					pstmt.setString(6, location);
					pstmt.setString(7, usage);
					pstmt.setTimestamp(8,java.sql.Timestamp.valueOf(event_time));
					pstmt.setString(9, event_code);
					pstmt.setString(10, event_msg);
					pstmt.setString(11, detail_msg);
					pstmt.setString(12, "0");
					pstmt.setString(13, "ADMIN");
					pstmt.setString(14, "ADMIN");
					pstmt.executeUpdate();*/
					
					SendMessage(socket,"Sended Data From Server: OK");
					
					/* 호출 Start */
					/*   
					TcpEndpointClient  cli = new TcpEndpointClient();
				
					cli.sendMessage("메시지 간다1");					
					sleep(5000);
					cli.sendMessage("메시지 간다2");
					sleep(5000);
					cli.sendMessage("메시지 간다3");
					cli.onClose();
					*/
					
				}
				catch(SocketException e) {
					
					try {
						socket.close();
						break;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				/*catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}*/catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				/*}catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					break;*/
				}catch(Exception e) {
					e.printStackTrace();
					break;
				}finally {
					if(conn != null)
						try {
							conn.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					if(pstmt != null)
						try {
							pstmt.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
		}
	} // run
	
	public void SendMessage(java.net.Socket socket, String msg) throws IOException {
		
		DataOutputStream out = null;
		
		try {
				out = new DataOutputStream(socket.getOutputStream());
			
				byte[] buffer = new byte[msg.getBytes().length];
				buffer = msg.getBytes("utf-8");
				out.write(buffer);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}



/*class ServerSender extends Thread {
Socket socket;
DataOutputStream out;
String name;

ServerSender(String key, Map<String, Client> clients) {
	//this.socket = socket;
    Client c = clients.get(key);
    this.socket = c.getSocket();
    
    try {
		
		out = new DataOutputStream(socket.getOutputStream());
		name = "["+socket.getInetAddress()+":"+socket.getPort()+"]";
	} catch(Exception e) {}
}

public void run() {
	Scanner scanner = new Scanner(System.in);
	while(out!=null) {
		try {
			
			out.writeUTF(name+scanner.nextLine());		
		} catch(IOException e) {}
	}
} // run()
}
*/

/*class Sender extends Thread {
Socket socket;
DataOutputStream out;
String name;

Sender(Socket socket) {
	this.socket = socket;
	try {
		out = new DataOutputStream(socket.getOutputStream());
		name = "["+socket.getInetAddress()+":"+socket.getPort()+"]";
	} catch(Exception e) {}
}

public void run() {
	Scanner scanner = new Scanner(System.in);
	while(out!=null) {
		try {
			out.writeUTF(name+scanner.nextLine());		
		} catch(IOException e) {}
	}
} // run()
}*/
