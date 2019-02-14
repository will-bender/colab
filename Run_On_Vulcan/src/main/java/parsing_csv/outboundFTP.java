package parsing_csv;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.Configurable;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class outboundFTP extends FTP implements Configurable{

	String serverAddress, username, password,filenamePath;
	int port = 21;
	String javaFilePath = "/Users/will/test"; 
	FTPClient ftpClient;

	public void start_test() throws IOException {
		System.out.println("Starting the test");
		start("dlpuser@dlptest.com", "puTeT3Yei1IJ4UYT7q0r", "ftp.dlptest.com", "ST123_0000001072_DATI_20190206_0400.TXT");
	}
	
	public void start(String username, String password, String serverAddress, String filenamePath) throws IOException {
		ftpClient = new FTPClient();
		setGlobalVariables(username, password, serverAddress, filenamePath);
		try {
			connect();
			listFilesOnServer();
			diagnosticInformation();

			download();

		}
		catch(IOException e) {
			System.out.println("Error: "+ e.getMessage());
			e.printStackTrace();
		}
		finally {
			disconnect();
		}
	}
	
	public void setGlobalVariables(String username, String password, String serverAddress, String filenamePath) {
		this.username = username;
		this.password = password;
		this.serverAddress = serverAddress;
		this.filenamePath = filenamePath;
	}
	
	public void diagnosticInformation() throws SocketException, IOException {

//		System.out.println("Login: "+ ftpClient.getReplyString());
		System.out.println("Status info: "+ftpClient.getStatus());
		System.out.println("Connected? : "+ftpClient.isConnected());
		System.out.println("Availabile? : "+ftpClient.isAvailable());

	}
	public void connect() throws IOException {
		ftpClient.connect(serverAddress, port);
		System.out.println("Connected to: "+serverAddress+" "+ftpClient.getReplyString());
		ftpClient.login(username, password);
		ftpClient.enterLocalPassiveMode();
//		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		
		int reply = ftpClient.getReplyCode();
		if(!FTPReply.isPositiveCompletion(reply)) {
			ftpClient.disconnect();
			System.out.println("FTP server refused connect");
			System.exit(1);
		}
	}
	
	public void disconnect() throws IOException {
		if(ftpClient.isConnected()) {
		ftpClient.logout();
		ftpClient.disconnect();
	}
	}
	public void download() throws IOException {
		File downloadedFilePath = new File(javaFilePath);
		OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadedFilePath));
        System.out.println("Attempting to retrieve the file");
		boolean success = ftpClient.retrieveFile(filenamePath, outputStream);
        outputStream.close();
        
        if(success) {
        	System.out.println("File downloaded successfully ");
        	System.out.println("Path to downloaded File: " + javaFilePath);
        }
        else {
        	System.out.println("File download error: " + ftpClient.getReplyString());
        	System.out.println("Re-check config file? ");
        	
        }
	}
	
	public void listFilesOnServer() throws IOException {
		System.out.println("Number of files on server: "+ftpClient.listFiles().length);
		System.out.println("List of files on the server: ");
		for (int fileDetails = 0; fileDetails < ftpClient.listFiles().length; fileDetails++) {
			System.out.println(ftpClient.listFiles()[fileDetails]);
		}
	}
	
	
	public void configure(FTPClientConfig config) {
		
	}

}
