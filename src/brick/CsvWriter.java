package brick;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class CsvWriter {
	//	Constants
	public static final String CSV_SEPARATOR = "|"; 
	public static final String CSV_FILETYPE = ".csv";
	
	//	Write products into CSV
	public void writeProductsToCsv (List<Product> products, String filename) throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter(filename + CSV_FILETYPE)) {
			
			//	Print header
			StringBuilder header = new StringBuilder();
			header.append("ProductName");
			header.append(CSV_SEPARATOR);
			header.append("ProductDesc");
			header.append(CSV_SEPARATOR);
			header.append("ProductImage");
			header.append(CSV_SEPARATOR);
			header.append("ProductPrice");
			header.append(CSV_SEPARATOR);
			header.append("ProductRating");
			header.append(CSV_SEPARATOR);
			header.append("ProductStore");
			
			out.println(header.toString());
			
			//	Print product list
			for (Product product : products) {
				StringBuilder sb = new StringBuilder();
				sb.append(product.name);
				sb.append(CSV_SEPARATOR);
				sb.append(product.description);
				sb.append(CSV_SEPARATOR);
				sb.append(product.imgUrl);
				sb.append(CSV_SEPARATOR);
				sb.append(product.price);
				sb.append(CSV_SEPARATOR);
				sb.append(product.rating);
				sb.append(CSV_SEPARATOR);
				sb.append(product.store);
				
				out.println(sb.toString());
			}
			
			products.toArray();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
