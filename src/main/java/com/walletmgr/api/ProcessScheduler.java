package com.walletmgr.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.walletmgr.api.Utils.FileLog;
import com.walletmgr.api.Utils.ProcessTrxUserWallet;
import com.walletmgr.api.dao.EventDao;
import com.walletmgr.api.service.WalletService;

@Component
public class ProcessScheduler {
	
	@Resource
	private EventDao eventDao;	
	
	@Resource
	private WalletService walletDao;
	
    //@Scheduled(cron = "0/60 * * * * *")
	@Scheduled(cron = "0 0 0/1 * * *")	//매 1시간 마다 동작
	//@Scheduled(fixedRate = 1000)
    public synchronized void printDate () throws Exception {
    	final String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
    	/*
    	FileLog.WriteLog(methodName + new Date().toString());
    	
    	int count = 0;
    	String from = StringFromCalendarSub("yyyy-MM-dd HH:mm:ss", 180, -6);
    	String to = StringFromCalendarSub("yyyy-MM-dd HH:mm:ss", 180, 0);
		//
    	ProcessTrxUserWallet ptrxwallet = new ProcessTrxUserWallet();
		count = ptrxwallet.ProcessUnlock(from, to);
		ptrxwallet.CloseProcessSession();
		
		FileLog.WriteLog(methodName + " from : " + from + " ~ to : " + to + " count : " + count);
     	*/
    }
    
	private Calendar CalendarFromString(String date, String formattype)
    {
		//yyyy-MM-dd
    	if(date == null || date == "" || date.isEmpty()==true) return null;
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat formatter = new SimpleDateFormat(formattype);
        try {
			cal.setTime(formatter.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return cal;
    }
	
	public String StringFromCalendarSub(String formattype, int day, int hour) {
		Calendar pcal = Calendar.getInstance();
		pcal.add(Calendar.DATE, day * -1);
		pcal.add(Calendar.HOUR, hour);
		SimpleDateFormat formatter = new SimpleDateFormat(formattype);
		return formatter.format(pcal.getTime());
	}	    
}
