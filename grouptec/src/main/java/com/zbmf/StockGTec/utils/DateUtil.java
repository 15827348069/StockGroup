package com.zbmf.StockGTec.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by iMac on 2016/12/28.
 */

public class DateUtil {
    static String dayNames[] = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    public static String getNewChatTime(long timesamp) {
        String result = "";
        Calendar todayCalendar = Calendar.getInstance();
        Calendar otherCalendar = Calendar.getInstance();
        otherCalendar.setTimeInMillis(timesamp);
        String timeFormat="M月d日 HH:mm";
        String yearTimeFormat="yyyy年M月d日 HH:mm";
        String am_pm="";
        int hour=otherCalendar.get(Calendar.HOUR_OF_DAY);
        if(hour>=0&&hour<6){
            am_pm="凌晨";
        }else if(hour>=6&&hour<12){
            am_pm="早上";
        }else if(hour==12){
            am_pm="中午";
        }else if(hour>12&&hour<18){
            am_pm="下午";
        }else if(hour>=18){
            am_pm="晚上";
        }
        timeFormat="M月d日 "+ am_pm +" hh:mm";
        yearTimeFormat="yyyy年M月d日 "+ am_pm +" hh:mm";
        boolean yearTemp = todayCalendar.get(Calendar.YEAR)==otherCalendar.get(Calendar.YEAR);
        if(yearTemp){
            int todayMonth=todayCalendar.get(Calendar.MONTH);
            int otherMonth=otherCalendar.get(Calendar.MONTH);
            if(todayMonth==otherMonth){//表示是同一个月
                int temp=todayCalendar.get(Calendar.DATE)-otherCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        result = getHourAndMin(timesamp);
                        break;
                    case 1:
                        result = "昨天 " + getHourAndMin(timesamp);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        int dayOfMonth = otherCalendar.get(Calendar.WEEK_OF_MONTH);
                        int todayOfMonth=todayCalendar.get(Calendar.WEEK_OF_MONTH);
                        if(dayOfMonth==todayOfMonth){//表示是同一周
                            int dayOfWeek=otherCalendar.get(Calendar.DAY_OF_WEEK);
                            if(dayOfWeek!=1){//判断当前是不是星期日   如想显示为：周日 12:09 可去掉此判断
                                result = dayNames[otherCalendar.get(Calendar.DAY_OF_WEEK)-1]+" " + getHourAndMin(timesamp);
                            }else{
                                result = getTime(timesamp,timeFormat);
                            }
                        }else{
                            result = getTime(timesamp,timeFormat);
                        }
                        break;
                    default:
                        result = getTime(timesamp,timeFormat);
                        break;
                }
            }else{
                result = getTime(timesamp,timeFormat);
            }
        }else{
            result=getYearTime(timesamp,yearTimeFormat);
        }
        return result;
    }
    /**
     * 当天的显示时间格式
     * @param time
     * @return
     */
    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(time));
    }
    /**
     * 不同年的显示时间格式
     * @param time
     * @param yearTimeFormat
     * @return
     */
    public static String getYearTime(long time,String yearTimeFormat) {
        SimpleDateFormat format = new SimpleDateFormat(yearTimeFormat);
        return format.format(new Date(time));
    }
    public static long getTimes(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }
    /**
     * 格式化时间
     * @param time
     * @return
     */
    public static String formatDateTime(String time) {
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(time==null ||"".equals(time)){
            return "";
        }
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();	//今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set( Calendar.HOUR_OF_DAY, 0);
        today.set( Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();	//昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH)-1);
        yesterday.set( Calendar.HOUR_OF_DAY, 0);
        yesterday.set( Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

        if(current.after(today)){
            return "今天 "+time.split(" ")[1];
        }else if(current.before(today) && current.after(yesterday)){

            return "昨天 "+time.split(" ")[1];
        }else{
            int index = time.indexOf("-")+1;
            return time.substring(index, time.length());
        }
    }
    public static String getTime(long time,String format){
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   (format);
        Date date =new Date(time);
        return formatter.format(date);
    }
    /**
     * 格式化更新时间
     * @param time
     * @return
     */
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

    /**
     * 获取更新天数
     * @param beginTime
     * @param _endTime
     * @param dataformt
     * @return
     */
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
    public static Date afterNDay(int n){
        Calendar c=Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE,n);
        return c.getTime();
    }
    public static Date afterMonth(int n){
        Calendar c=Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH,n);
        return c.getTime();
    }

    public static String getDate3(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        return sDateFormat.format(date);

    }

    public static String getDate4(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        Date date = calendar.getTime();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
        return sDateFormat.format(date);

    }
}
