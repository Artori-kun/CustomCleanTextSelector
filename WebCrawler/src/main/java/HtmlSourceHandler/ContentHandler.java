package HtmlSourceHandler;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentHandler {
    public Queue<String> unvisitedUrl;
    public Queue<String> visitedUrl;

    public ContentHandler() {
        unvisitedUrl = new PriorityQueue<String>();
        visitedUrl = new PriorityQueue<String>();
    }

    public String contentFilter(String paragraph) {

        String result = "";
        paragraph = paragraph.replaceAll("[\\.\\;]", " daucham ")
                .replaceAll("[\\,]", " dauphay ")
                .replaceAll("[\\?]", " dauhoi ")
                .replaceAll("[\\!]", " dauchamthan ");

        String[] words = paragraph.split("\\P{L}+");

        for ( String word :words
        ) {
            if ( word.equals("daucham")){
                result = result.concat(".");
            }
            else if( word.equals("dauphay")){
                result = result.concat(",");
            }
            else if( word.equals("dauhoi")){
                result = result.concat("?");
            }
            else if( word.equals("dauchamthan")){
                result = result.concat("!");
            }
            else {
                result = result.concat(" " + word);
            }
        }

        return result.trim();

        //trim all leading and trailing whitespaces

//        paragraph = paragraph.replaceAll("[\\;\\:\\!]", ".")
//                .replaceAll("[0-9]", "")
//                .replace("/", "")
//                .replace("-", "")
//                .replace("\"", "")
//                .replace("  ", " ")
//                .replaceAll("[\\&\\@\\(\\)\\{\\}\\$\\+\\_\\#\\=\\*\\'\\%\\|\\<\\>\\^]", "");
//        paragraph = paragraph.trim();
//        return paragraph;
    }

    public String alternativeContentFilter(String paragraph) {
        String result = "";
        paragraph = paragraph.replaceAll("[\\.\\;]", " daucham ")
                .replaceAll("[\\,]", " dauphay ")
                .replaceAll("[\\?]", " dauhoi ")
        .replaceAll("[\\!]", " dauchamthan ");

        String[] words = paragraph.split("\\P{L}+");

        for ( String word :words
             ) {
            if ( word.equals("daucham")){
                result = result.concat(".");
            }
            else if( word.equals("dauphay")){
                result = result.concat(",");
            }
            else if( word.equals("dauhoi")){
                result = result.concat("?");
            }
            else if( word.equals("dauchamthan")){
                result = result.concat("!");
            }
            else {
                result = result.concat(" " + word);
            }
        }

        return result.trim();
    }

    public ArrayList<String> getParagraphContent(String url) {
        Elements paragraphs = HtmlSourceGetter.getTagContents(url, "p");

        ArrayList<String> result = new ArrayList<String>();

        for (Element paragraph : paragraphs
        ) {
            if (!paragraph.html().contains("input")) {
                result.add(contentFilter(paragraph.text()));
            }
        }

        return result;
    }

    public ArrayList<String> getHeaderContent(String url) {
        Elements headers_1 = HtmlSourceGetter.getTagContents(url, "h1");
        Elements headers_2 = HtmlSourceGetter.getTagContents(url, "h2");

        ArrayList<String> result = new ArrayList<String>();

        for (Element header : headers_1
        ) {
            result.add(contentFilter(header.text()));
        }

        for (Element header : headers_2
        ) {
            result.add(contentFilter(header.text()));
        }

        return result;
    }

    public void findNewUrl(String url, String scope) {
        Elements newUrls = HtmlSourceGetter.getTagContents(url, "a");

        for (Element newUrl : newUrls
        ) {
            String u = newUrl.getElementsByAttributeValueContaining(
                    "href",
                    scope)
                    .attr("href");

            if (!u.contains("www.facebook.com") && u.contains("https://www.qdnd.vn")) {
                if (!visitedUrl.contains(u) && !unvisitedUrl.contains(u)) {
                    unvisitedUrl.add(u);
                }
            }
        }

        visitedUrl.add(url);
    }


    public static void main(String[] args) {
        ContentHandler contentHandler = new ContentHandler();
        String str = "Việt Nam đã ghi nhận 57 trường hợp mắc Covid-19 (16 trường hợp đã được điều trị khỏi và xuất viện). Tổng số người tiếp xúc gần và nhập cảnh từ vùng dịch đang được theo dõi sức khỏe (diện cách ly) là 42.956 người, trong đó có 2.227 người cách ly tập trung tại bệnh viện, 8.318 người cách ly tập trung tại cơ sở khác và 32.411 người cách ly tại nhà, nơi lưu trú.";
        System.out.println( str );
        System.out.println(contentHandler.alternativeContentFilter(str));
    }
}
