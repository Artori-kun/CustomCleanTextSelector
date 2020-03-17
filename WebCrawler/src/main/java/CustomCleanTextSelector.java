import DBHandler.DbHandler;
import HtmlSourceHandler.ContentHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomCleanTextSelector {
    public static final String url = "https://www.qdnd.vn";

    public static void main(String[] args) throws SQLException {
        ContentHandler contentHandler = new ContentHandler();
        DbHandler dbHandler = new DbHandler("root", "krifferson", "cleanTextTest");

        //first create cleantext table
        dbHandler.createCleanTextTable();

        //visit first url
        contentHandler.unvisitedUrl.add(url);
        int page_id = 0;
        int text_id = 0;

        while (!contentHandler.unvisitedUrl.isEmpty()) {
            String currentUrl = contentHandler.unvisitedUrl.poll();
            String article = "";
            System.out.println("Visiting: " + currentUrl);

            //extract paragraph content
            for (String paragraph : contentHandler.getParagraphContent(currentUrl)
            ) {
                if (!paragraph.endsWith(".")) {
                    paragraph = paragraph.concat(".");
                }
                article = article.concat(" " + paragraph);
//                System.out.println("Inserting paragraph:\n" + paragraph);
//                dbHandler.insertCleanText( paragraph, "1", "1");
            }

            //extract header content
            for (String header : contentHandler.getHeaderContent(currentUrl)
            ) {
                if (!header.endsWith(".")) {
                    header = header.concat(".");
                }
//                System.out.println("Inserting header:\n" + header);
//                dbHandler.insertCleanText( header, "1", "1");
                article = article.concat("" + header);
            }

            article = article.trim();

            page_id++;
            text_id++;
            System.out.println("Inserting: " + article);
            dbHandler.insertCleanText(article, String.valueOf(page_id), String.valueOf(text_id));
            //visit other related sites
            contentHandler.findNewUrl(currentUrl, "qdnd.vn");
        }

    }
}
