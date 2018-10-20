package com.zbmf.StockGroup.utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by xuhao on 2017/4/21.
 */

public class BoxDetailHtml {
   private final static String html =
            "<html>"+
                    "<style type=\"text/css\">"
                    +".box-android{padding:5px 13px; min-height:64px; line-height:27px; border-bottom:1px solid #dfdfdf;}"
                    +".box-android-time{font-size:12px; color:#666;}"
                    +".box-android-content{font-size:16px; color:#333;line-height:1.5;text-align: justify;word-wrap: break-word;}"
                    +"body {color: black; -webkit-touch-callout: none;}"
                    +"img {vertical-align: middle; max-width:160px; max-height:90px; min-width:20px; min-height:20px; }"
                    +"a:link {text-decoration: none;}"
                    +"a:visited {text-decoration: none;}"
                    +"a:hover {text-decoration: none;}"
                    +"a:active {text-decoration: none;}"
                    +"</style>"
                    +"<body>"
            ;
    private final static String divr="<div class='box-android'><div class='box-android-time'>[b]</div><div class='box-android-content'>[c]</div></div>";

    public static  String getBoxHtml(JSONArray box_contents){
        StringBuilder sb=new StringBuilder();
        sb.append(html);
        for (int i = 0; i < box_contents.length(); i++)
        {
            JSONObject item = box_contents.optJSONObject(i);
            String content = item.optString("content");
            LogUtil.e(content);
            content = content.replaceAll("http://img:", "");
//            content = StringExtend.filterDivTags(content);
            content = StringExtend.filterImageTags(content);
            if(!item.optString("created_at").trim().equals("")){
                sb.append(divr.replace("[c]",content).replace("[b]",item.optString("created_at")));
            }
        }
        sb.append("</body></html>");
        return  sb.toString();
    }
}
