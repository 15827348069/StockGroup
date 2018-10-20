package com.zbmf.worklibrary.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GetTime {
	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public static void main(String[] args) {
		System.out.println(getTime("14-12-11 10:55"));
		getData();
	}
    public static String getTime(String time){
    	String str_time=null;
    	DecimalFormat df=new DecimalFormat("#");
    	SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yy-MM-dd HH:mm");  
    	SimpleDateFormat   formatter2  =   new 	 SimpleDateFormat	("yy-MM-dd");
    	Date   curDate   =   new   Date(System.currentTimeMillis());//获取当前时间     
    	GregorianCalendar cal1 = new GregorianCalendar();  
		GregorianCalendar cal2 = new GregorianCalendar();  
		String time1=formatter.format(curDate);
		Date date2=null,date1=null;
		try {
			date1 = formatter.parse(time1);
			date2 = formatter.parse(time);
			cal1.setTime(date1);  
			cal2.setTime(date2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double dayCount = ((cal1.getTimeInMillis()-cal2.getTimeInMillis())/1000);
		String time3=formatter2.format(date1);
		String time4=formatter2.format(date2);
		if(time3.equals(time4)){
			if(dayCount<60){
				str_time="刚刚";
			}else if(dayCount>=60&&dayCount<3600){
				str_time=df.format(dayCount/60)+"分钟前";
			}else if(dayCount>=3600&&dayCount<259200){
				str_time=df.format(dayCount/3600)+"小时前";
			}
		}else{
			//天数
			System.out.println(time3);
			System.out.println(time4);
			str_time=getdays(time4,time3,"yy-MM-dd")+"天前";
		}
    	return str_time;
    }
    public static String getdays(String beginTime,String _endTime,String dataformt){
		String str1 = beginTime;  //"yyyyMMdd"格式 如 20131022
		String str2 = _endTime;  //"yyyyMMdd"格式 如 20131022
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dataformt);//输入日期的格式 
		Date date1 = null;
		Date date2 = null;
		try {
		date1 = simpleDateFormat.parse(str1);
		date2 = simpleDateFormat.parse(str2);
		} catch (ParseException e) {
		e.printStackTrace();
		}  
		GregorianCalendar cal1 = new GregorianCalendar();  
		GregorianCalendar cal2 = new GregorianCalendar();  
		cal1.setTime(date1);  
		cal2.setTime(date2);  
		double dayCount = (cal2.getTimeInMillis()-cal1.getTimeInMillis())/(1000*3600*24);//从间隔毫秒变成间隔天数
		double i=0;
		double k=dayCount;
		Calendar calendar= Calendar.getInstance();  
        while(true){
        	  int day = calendar.get(Calendar.DAY_OF_WEEK); 
        	  if (day==1) {  
                  day=7;  
                  day=day-1;  
              } 
        	  i++; 
        	  if(i==dayCount){
        			DecimalFormat df=new DecimalFormat("#"); 
        			return df.format(k);
        	  }
        	  calendar.add(Calendar.DAY_OF_MONTH,1); 
         }
	}

	/**
	 * 是否是开盘时间
	 * @return
	 */
    public static boolean getData(){
    	Calendar cal = Calendar.getInstance();// 当前日期
    	int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
    	int minute = cal.get(Calendar.MINUTE);// 获取分钟
    	int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
    	final int start = 9 * 60 ;// 起始时间 的分钟数
    	final int end = 11 * 60+30;// 结束时间的分钟数
    	final int pm_start=13*60;
    	final int pm_end=15*60;
    	if(hour<12){
    		if (minuteOfDay >= start && minuteOfDay <= end) {
    			return true;
    		} else {
    			return false;
    		}
    	}else{
    		if (minuteOfDay >= pm_start && minuteOfDay <= pm_end) {
    			return true;
    		} else {
    			return false;
    		}
    	}
    }
    /***
     * 判断时间于是否过期
     * @param time
     * @return
     */
    public static boolean getTimeIsTrue(String time){
    	boolean istime=false;
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	try
    	{
    	    Date d1 = df.parse(time);
    	    Date   curDate   =   df.parse(df.format(new Date(System.currentTimeMillis()))); //获取当前时间     
    	    long diff = d1.getTime() - curDate.getTime();
    	    System.out.println("diff>>>"+d1.getTime()+">>>>"+curDate.getTime());
    	    if(diff>=0){
    	    	istime=true;
    	    }else{
    	    	istime=false;
    	    }
    	}
    	catch (Exception e)
    	{
    		istime=false;
    	}
    	return istime;
    }

	/**
	 * 以友好的方式显示时间
	 *
	 * @param sdate
	 * @return
	 */
	public static String getTime1(String sdate) {
		Date time = null;
		try {
			time = parseStringDate(sdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = format.format(cal.getTime());
		String paramDate = format.format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1)+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1)+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = format.format(time);
		}
		return ftime;
	}

	public static Date toDate(long date){
		return new Date(date);
	}
	public static Date parseStringDate(String dateStr) throws ParseException{
		return 	format1.parse(dateStr);
	}

}
