//import com.gargoylesoftware.htmlunit.*;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import com.gargoylesoftware.htmlunit.html.HtmlSpan;
//import com.gargoylesoftware.htmlunit.html.parser.HTMLParser;
//
//import java.io.FileWriter;
//import java.io.IOException;
//
//
//public class Main {
//
//
//    public static void main(String[] args) {
//
//        WebClient webClient = createWebClient();
//
//            try {
//                String link = "http://homeworktask.infare.lt/search.php?from=MAD&to=AUH&depart=2023-07-02&return=2023-07-06";
//                HtmlPage page = webClient.getPage(link);
//
//                System.out.println(page.getTitleText());
//                String xpath = "/html/body/div[1]/div[6]/div[1]/div/div/div/div[5]/div[13]/pre/span/span[2]";
//
//                HtmlSpan priceDiv = (HtmlSpan) page.getByXPath(xpath).get(0);
//                System.out.println(priceDiv.asNormalizedText());
//
//                writeCsvFile(link, priceDiv.asNormalizedText());
//
//            }  catch (FailingHttpStatusCodeException | IOException e) {
//                e.printStackTrace();
//            } finally {
//                webClient.close();
//            }
//
//    }
//
//    private static WebClient createWebClient() {
//        WebClient webClient = new WebClient(BrowserVersion.CHROME);
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
//        webClient.getOptions().setCssEnabled(false);
//        webClient.getOptions().setJavaScriptEnabled(false);
//        return webClient;
//    }
//
//    public static void writeCsvFile (String link, String price) throws IOException {
//
//        FileWriter recipesFile = new FileWriter("export.csv", true);
//
//        recipesFile.write("link, price\n");
//
//        recipesFile.write(link + ", " + price);
//
//        recipesFile.close();
//    }
//}
