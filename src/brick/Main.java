package brick;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;
import org.json.XML;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Main {

	public static void main(String[] args) {

		//	Set page URL
		String url = "https://www.tokopedia.com/p/handphone-tablet/handphone";
		
		try {
			//	Connect to webpage using Chrome settings and get web page as XML with HTMLUnit
			@SuppressWarnings("resource")
			WebClient webClient = new WebClient(BrowserVersion.CHROME);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage myPage = webClient.getPage(new URL(url));
			
			String pageXml = myPage.asXml();

			//	Convert XML to JSON to get javascript object
			JSONObject json = XML.toJSONObject(pageXml);   
	        String jsonString = json.toString(4);
			try (PrintWriter out = new PrintWriter("test.json")) {
				out.println(jsonString);
			}
			
			List<HtmlElement> items = myPage.getByXPath("//div") ;

			//	Get webpage using Firefox settings with JSoup
			Connection connection = Jsoup.connect(url)
					.userAgent("Mozilla")
					.referrer("http://www.google.com")
					.timeout(60000);
			Document document = connection.execute().parse();
			
			//	Select HTML tag
			String divProductList = "div[data-testid=lstCL2ProductList]";
			String divAttr = "div[data-testid=divProductWrapper]";
			String productLink = "a[data-testid=lnkProductContainer]";
			
			Elements productList = document.select(productLink);
			
			Elements listProduct = document.select(divProductList);
			
			List<Product> productResult = new ArrayList<Product>();
			
			//	Loop for each product
			for (Element element : productList) {
				String productUrl = element.attr("abs:href");
//				System.out.println(productUrl);
				
				//	Check if URL is a redirect link
				if (productUrl.contains("https://ta.tokopedia.com/promo/v1/clicks")) {
					String newUrl = productUrl.substring(productUrl.indexOf("https%3A%2F%2F")).replace("%2F", "/")
							.replace("%3A", ":").replace("%3F", "?").replace("%3D", "=");
//					System.out.println(newUrl);
					productUrl = newUrl;
				}
				//	Get new webpage and elements
				document = Jsoup.connect(productUrl)
						.userAgent("Mozilla")
						.timeout(60000).execute().parse();
				Elements divContent = document.select("div#main-pdp-container");
				
				//	Get product specifications
				String name = divContent
						.select("div#pdp_comp-product_content > div > h1[data-testid=lblPDPDetailProductName]").text();
				String desc = divContent.select(
						"div#pdp_comp-product_detail > div > div[role=tabpanel] > div div[data-testid=lblPDPDescriptionProduk]")
						.text();
				String img = divContent.select(
						"div#pdp_comp-product_detail > div > div[role=tabpanel] > div div[data-testid=lblPDPDescriptionProduk] img")
						.attr("src");
				String price = divContent.select("div#pdp_comp-product_content > div > div > div.price").text()
						.replace("Rp", "");
				String rating = divContent
						.select("div#pdp_comp-review > div[data-testid=pdpFlexWrapperContainer] > div > div > h5")
						.text();
				String store = divContent.select("div#pdp_comp-shop_credibility > div > div > div > a > h2").text();
				
				// Formatting String
				String newName = name.replace("|", "");
				String newDesc = desc.replace("|", "");
				String newStore = store.replace("|", "");
				
				//	Formatting price
				DecimalFormat decimalFormat = new DecimalFormat("#,###.##", new DecimalFormatSymbols(new Locale("id", "ID")));
				decimalFormat.setParseBigDecimal(true);
				BigDecimal priceAmt = (BigDecimal) decimalFormat.parse(price);
				
				Float ratingScore = 0.0F;
				if (!rating.trim().isEmpty()) {
					ratingScore = Float.valueOf(rating);
				}
				
//				System.out.println(name);
//				System.out.println(desc);
//				System.out.println(img);
//				System.out.println(priceAmt);
//				System.out.println(rating);
//				System.out.println(store);
				
				//	Generate result into Product class 
				Product product = new Product(newName, newDesc, img, priceAmt, ratingScore, newStore);
				productResult.add(product);
			}
			
			//	Write result to CSV
			CsvWriter csvWriter = new CsvWriter();
			csvWriter.writeProductsToCsv(productResult, "result");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
