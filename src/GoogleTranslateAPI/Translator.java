package GoogleTranslateAPI;

import org.apache.commons.text.StringEscapeUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Translator {
  public static String translate(String langFrom, String langTo, String text) throws IOException {
    String APIKEY = "AKfycbzxtNpZD2Ogs4oeUnj8nTaCmPlKwgwsLWPasyIsLQPB_WXvKdKU";
    URL url = new URL("https://script.google.com/macros/s/" + APIKEY + "/exec?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) + "&target=" + langTo + "&source=" + langFrom);
    HttpURLConnection request = (HttpURLConnection) url.openConnection();
    request.setRequestProperty("User-Agent", "Mozilla/5.0");
    BufferedReader inputStream = new BufferedReader(new InputStreamReader(request.getInputStream()));
    String inputLine;
    StringBuilder response = new StringBuilder();
    while ((inputLine = inputStream.readLine()) != null) response.append(inputLine);
    inputStream.close();
    return StringEscapeUtils.unescapeHtml4(response.toString());
  }

  public static void main(String[] args) throws IOException {
    String text = "I loved you, so much";
    System.out.println("Translated text: " + translate("en", "vi", text));
  }
}