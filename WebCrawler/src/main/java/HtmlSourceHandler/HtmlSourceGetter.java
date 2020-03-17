package HtmlSourceHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HtmlSourceGetter {
    public static String getSourceHTMLString(String url ){
        String htmlSource = "Nothing";
        try{
            htmlSource = Jsoup.connect( url )
                    .get().html();
        }catch ( IOException e ){
            e.printStackTrace();
        }
        return htmlSource;
    }

    public static Elements getTagContents( String url, String tag ){
        Document document = Jsoup.parse( getSourceHTMLString( url ));
        return document.getElementsByTag( tag );
    }


//    public static void main(String[] args) {
//        ContentHandler contentHandler = new ContentHandler();
//        String url = "https://www.qdnd.vn/chinh-tri/tin-tuc-su-kien/nghiem-cam-ky-thi-nguoi-nhiem-covid-19-va-xu-ly-nghiem-nguoi-dua-thong-tin-sai-su-that-ve-dich-benh-612461";
////        System.out.println( getSourceHTMLString(url));
//        for ( Element element: getTagContents(
//                url,
//                "p")
//             ) {
//            System.out.println( contentHandler.contentFilter( element.text() ) );
//        }
//    }

}
