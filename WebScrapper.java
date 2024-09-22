package org.example;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlImage;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlParagraph;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebScrapper {
    public static void main(String[] args){
        try (final WebClient webClient = new WebClient()) { //initialize the web browser
            webClient.getOptions().setCssEnabled(false); //disable css
            webClient.getOptions().setJavaScriptEnabled(false); //enable javascript

            String url = "https://whats-on-nairobi.com/"; //url to scrape
            HtmlPage page = webClient.getPage(url);

            List<HtmlParagraph> paragraphs = page.getByXPath("//p");
            List<HtmlImage> images = page.getByXPath("//img");

            // List to hold structured data for paragraphs and images together
            List<Map<String, String>> structuredData = new ArrayList<>();

            // Assuming paragraphs and images are paired (first paragraph -> first image, etc.)
            int size = Math.min(paragraphs.size(), images.size()); // Only iterate up to the smaller list's size

            for (int i = 0; i < size; i++) {
                Map<String, String> pair = new HashMap<>();
                pair.put("paragraph", paragraphs.get(i).asNormalizedText());
                pair.put("image", images.get(i).getSrcAttribute());

                structuredData.add(pair);
            }

            // Write structured data to a JSON file
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("structuredScrapedData.json"), structuredData);

            System.out.println("Data saved to structuredScrapedData.json");




        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
