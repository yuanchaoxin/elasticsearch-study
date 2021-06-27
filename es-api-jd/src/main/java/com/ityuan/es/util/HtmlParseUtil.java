package com.ityuan.es.util;

import com.ityuan.es.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName HtmlParseUtil
 * @Package com.ityuan.es.util
 * @Author yuanchaoxin
 * @Date 2021/6/27
 * @Version 1.0
 * @Description
 */
public class HtmlParseUtil {

    public static void main(String[] args) throws Exception {
        HtmlParseUtil.parseJD("编程").forEach(System.out::println);
    }

    private static String zhPattern = "[\\u4e00-\\u9fa5]";

    public static List<Content> parseJD(String keyword) throws Exception {
        String url = "https://search.jd.com/Search?keyword=" + keyword;
        url = encode(url,"utf-8");
        Document document = Jsoup.parse(new URL(url), 10000);
        Element element = document.getElementById("J_goodsList");
        Elements elements = element.getElementsByTag("li");

        List<Content> contentList = new ArrayList<>();
        for (Element el : elements) {
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String title = el.getElementsByClass("p-name").eq(0).text();

            Content content = new Content();
            content.setImg(img);
            content.setPrice(price);
            content.setTitle(title);
            contentList.add(content);
        }

        return contentList;
    }

    /**
     * 解决中文问题
     */
    public static String encode(String str, String charset) throws UnsupportedEncodingException {
        Pattern p = Pattern.compile(zhPattern);
        Matcher m = p.matcher(str);
        StringBuffer b = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
        }
        m.appendTail(b);
        return b.toString();
    }
}
