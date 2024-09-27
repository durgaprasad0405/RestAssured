package utilities;

import org.testng.annotations.Test;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

@Test
public class SFTPutilities extends Global {

	static Session session;
	static JSch jsch = new JSch();
	static Channel channel = null;
	static ChannelSftp sftp = null;

	static String filepath = "/app/home/telematics/batch/properties/";
	static String filename = "batch-last-date-time.txt";

	@Test
	public static void SFTPConnectionOn(String host, String userName, String password) {

		host = "10.101.0.5";
		int port = 22;
		String localFile = "C:/Narasimha/Automation/OBIEE_Odometer.txt";
        String remoteFile = "/app/content/base/telematics/OBIEE_Odometer.txt";
		try {
			jsch.addIdentity(privateKey);
			session = jsch.getSession("vxliqnpul", host, port);
			session.setPassword("Lnaresh8!");
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			Channel sftp = session.openChannel("sftp");
			ChannelSftp channelSftp = (ChannelSftp) sftp;
			channelSftp.put(localFile, remoteFile);
			
			// channelSftp.get(remoteFile, localFile);

            channelSftp.exit();

			System.out.println("Session connected:" + session.isConnected());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}