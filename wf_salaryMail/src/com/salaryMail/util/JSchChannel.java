package com.salaryMail.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

public class JSchChannel {
	private final Logger LOG = Logger.getLogger(JSchChannel.class);
	private String host;
	private String user;
	private String password;
	private int port;
	private int maxWaitTime;
	private String keyfile;
	private String passphrase;
	private boolean sshKey;
	private ChannelSftp sftp;
	private Session session;

	public JSchChannel(String host, String user, String password, int port,
			int maxWaitTime) {
		this.host = host;
		this.user = user;
		this.password = password;
		this.port = port;
		this.maxWaitTime = maxWaitTime;
		this.keyfile = null;
		this.passphrase = null;
		this.sshKey = false;
	}

	public JSchChannel(String host, String user, int port, int maxWaitTime,
			String keyfile, String passphrase) {
		this.host = host;
		this.user = user;
		this.password = null;
		this.port = port;
		this.maxWaitTime = maxWaitTime;
		this.keyfile = keyfile;
		this.passphrase = passphrase;
		this.sshKey = true;
	}

	public void open() throws JSchException {
		JSch client = new JSch();
		if (sshKey && keyfile != null && keyfile.length() > 0) {
			client.addIdentity(this.keyfile, this.passphrase);
		}
		session = client.getSession(this.user, this.host, this.port);
		session.setUserInfo(new UserInfo() {

			public String getPassphrase() {
				return null;
			}

			public String getPassword() {
				return password;
			}

			public boolean promptPassphrase(String arg0) {
				return true;
			}

			public boolean promptPassword(String arg0) {
				return true;
			}

			public boolean promptYesNo(String arg0) {
				return true;
			}

			public void showMessage(String arg0) {
			}
		});
		session.setTimeout(maxWaitTime);
		session.connect();
		Channel channel = session.openChannel("sftp");
		channel.connect();
		sftp = (ChannelSftp) channel;
	}

	/**上传文件
     * @param directory 上传的目录
     * @param uploadFile 要上传的文件
     * @param sftp
     */
    public void upload(String directory, String uploadFile) {
        try {
        	if  (isDirExist(directory) == true) {
                sftp.cd(directory);
        	}
        	else {
            	sftp.mkdir(directory);
            	sftp.cd(directory);
        	}
            File file = new File(uploadFile);
            FileInputStream in =new FileInputStream(file);
            sftp.put(in, file.getName());
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean isDirExist(String directory) throws SftpException {    
        boolean isDirExistFlag = false;    
        try {    
            SftpATTRS sftpATTRS = sftp.lstat(directory);    
            isDirExistFlag = true;    
            return sftpATTRS.isDir();
        } catch (Exception e) {    
            if (e.getMessage().toLowerCase().equals("no such file")) {    
                isDirExistFlag = false;    
            }    
        }    
        return isDirExistFlag;    
    }  
    
    /**下载文件
     * @param directory 下载目录
     * @param downloadFile 下载的文件
     * @param saveFile 存在本地的路径
     * @param sftp
     */
    public void download(String directory, String downloadFile,
            String saveFile) {
        try {
            sftp.cd(directory);
            sftp.get(downloadFile, saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void close() {
		if (sftp != null) {
			sftp.disconnect();
			sftp = null;
		}
		if (session != null) {
			session.disconnect();
			session = null;
		}
	}

	public void cd(String path) throws SftpException {
		sftp.cd(path);
	}

	public void pwd() throws SftpException {
		LOG.info(sftp.pwd());
	}
	
	public String[] getFileNames(String path) throws SftpException {
		Vector<?> vector = sftp.ls(path);
		String[] fileNames = new String[vector.size()];
		int i = 0;
		for (Object object : vector) {
			if (object instanceof LsEntry) {
				LsEntry entry = LsEntry.class.cast(object);
				fileNames[i] = entry.getFilename();
				i++;
			}
		}
		return fileNames;
	}
	
	public void ls() throws SftpException {
		Vector<?> vector = sftp.ls(".");
		for (Object object : vector) {
			if (object instanceof LsEntry) {
				LsEntry entry = LsEntry.class.cast(object);
				LOG.info(entry.getFilename());
			}
		}
	}

	public void ls(String path) throws SftpException {
		Vector<?> vector = sftp.ls(path);
		for (Object object : vector) {
			if (object instanceof LsEntry) {
				LsEntry entry = LsEntry.class.cast(object);
				LOG.info(entry.getFilename());
			}
		}
	}

	public void rename(String oldPath, String newPath) throws SftpException {
		sftp.rename(oldPath, newPath);
	}

	public void cp(String src, String dest) throws SftpException {
		int lastSlash = src.lastIndexOf("/");
		String srcFile = src;
		String srcDir = ".";
		if (lastSlash > -1) {
			srcFile = src.substring(lastSlash + 1);
			srcDir = src.substring(0, lastSlash);
		}
		String temp = srcDir + "/temp_" + srcFile;
		rename(src, temp);
		rename(temp, dest);
	}

	public void rm(String file) throws SftpException {
		sftp.rm(file);
	}

	public void mv(String src, String dest) throws SftpException {
		rename(src, dest);
	}

	public void rmdir(String path) throws SftpException {
		sftp.rmdir(path);
	}

	public void chmod(int permissions, String path) throws SftpException {
		sftp.chmod(permissions, path);
	}
}
