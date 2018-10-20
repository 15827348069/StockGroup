package com.zbmf.StockGroup.utils;


import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringExtend
{
    private static final String TAG = "StringExtend";

    // 过滤HTML格式标签
    public static String filterHTMLFormat(String html)
    {
        // 含html标签的字符串
        String htmlStr = html;
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;

        try {

            // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";

            // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";

            // 定义HTML标签的正则表达式
            String regEx_html = "<[^>]+>";

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);

            // 过滤script标签
            htmlStr = m_script.replaceAll("");

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            // 过滤style标签
            htmlStr = m_style.replaceAll("");

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            // 过滤html标签
            htmlStr = m_html.replaceAll("");

            textStr = htmlStr;

        } catch (Exception e) {

            System.err.println("Html2Text: " + e.getMessage());
        }

        // 返回文本字符串
        return textStr;
    }

    public static String filterDivTags(String content)
    {
        String mutableString = content;

        mutableString = mutableString.replace("<DIV", "<div");

        if (mutableString.length() == 0)
        {
            return "";
        }

        Pattern pattern = Pattern.compile("<div .*?>");
        Matcher matcher = pattern.matcher(mutableString);

        int i = 0;
        while (matcher.find(i))
        {
            String oldString = matcher.group();
            mutableString = mutableString.replace(oldString,"");
            i++;
        }
        return mutableString;
    }

    // 修改_content中的所有图片是jpg格式的img标签
    public static String filterImageTags(String content)
    {
        String mutableString = content;

        if (mutableString.length() == 0)
        {
            return "";
        }

        //  <\s*img\s+([^>]*)\s*>
        Pattern pattern = Pattern.compile("<img .*?>", Pattern.MULTILINE|Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);


        while (matcher.find())
        {
            String oldString = matcher.group();

            //Log.e(TAG, "oldString: " + oldString);
            String newString = "";

            Pattern p = Pattern.compile("[a-zA-z]+://[^\"|\\'| ]*");
            Matcher m = p.matcher(oldString);

            if (m.find())
            {
                newString = String.format("<a href='img:%s'>%s</a>", m.group(), oldString);
                //Log.e(TAG, "newString: " + newString);
            }

            mutableString = mutableString.replace(oldString, newString);
        }

        return mutableString;
    }
    /**
     * 使用java正则表达式去掉多余的.与0
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
