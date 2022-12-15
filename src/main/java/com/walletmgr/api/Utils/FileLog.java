package com.walletmgr.api.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileLog {

	public FileLog() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static String GetBaseLogPath() {
		String 	basepath;
		int 	ostype;
		String osName = System.getProperty("os.name").toLowerCase();
		if(osName.indexOf("win") >= 0) {
			basepath = "C:\\pkg\\log\\";
			ostype = 1;
		} else if(osName.indexOf("mac") >= 0) {
			basepath = "/pkg/log/";
			ostype = 2;
		} else if(osName.indexOf("sunos") >= 0) {
			basepath = "/pkg/log/";
			ostype = 3;
		} else if(osName.indexOf("nix") >= 0 || osName.indexOf("nux") >= 0 || osName.indexOf("aix") > 0) {
			basepath = "/pkg/log/";
			ostype = 4;
		} else {
			basepath = "/pkg/log/";
			ostype = 0;
		}
		//System.out.println("basepath : " + basepath + ", ostype " + ostype + ", osName " + osName);	
		return basepath;
	}
	
	public static synchronized void WriteLog(String log) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");
		String sToday = formatter.format(cal.getTime());
		String sDate = formatter2.format(cal.getTime());
		String sFileName = GetBaseLogPath() + "reward_api_" + sDate +"_log.txt";
		//log = "[" + sToday + "]:" + log;
		System.out.println(log);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(sFileName, true));
			bw.write("[" + sToday + "]:" + log);
			bw.newLine();
			bw.close();
		}
		catch(IOException ie) {
			ie.printStackTrace();
		}
	}
}
