package informationRetrievel;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import classes.Query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

;
public class Main {

	static void nextPage(WebDriver driver, int page) {
		System.out.println("################################");
		System.out.println("nextPage function started");

		// go to page i in google
		driver.findElement(By.cssSelector("[aria-label='Page " + page + "']"))
				.click();

		System.out.println("nextPage function finished");
		System.out.println("################################");
	}

	static void getAllURL(WebDriver driver, List<String> allURLs) {
		System.out.println("################################");
		System.out.println("getAllURL function started");

		// get all url from google page
		List<WebElement> listOfLinkWithTag = driver.findElements(By
				.className("yuRUbf"));

		for (WebElement webElement : listOfLinkWithTag) {
			allURLs.add(webElement.findElement(By.tagName("a")).getAttribute(
					"href"));
		}

		System.out.println("getAllURL function finished");
		System.out.println("################################");

	}

	public static void fillList(List<Query> query, String folderName) {
		System.out.println("fillList function started");
		System.out.println("################################");

		final File folder = new File(folderName);
		for (File fileEntry : folder.listFiles()) {
			String fileName = fileEntry.getName();
			String[] lang_domain = fileName.split("_");

			String lang = lang_domain[0];
			String domain = lang_domain[1];

			System.out.println(fileEntry.getName());

			Query q = new Query(lang, domain);

			// Open the file
			FileInputStream fstream;
			try {
				fstream = new FileInputStream(fileEntry);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						fstream, "UTF-8"));

				String strLine;

				// Read File Line By Line
				while ((strLine = br.readLine()) != null) {

					// Print the content on the console
					System.out.println(strLine);
					q.addQuery(strLine);
				}

				// Close the input stream
				fstream.close();

				query.add(q);
			} catch (FileNotFoundException e) {
				System.err.println("FileNotFoundException - fillList");
				e.printStackTrace();
			} catch (IOException e) {
				System.err.println("IOException - fillList");
				e.printStackTrace();
			}
			System.out
					.println("******************************************************");
		}

		System.out.println("fillList function finished");
		System.out.println("################################");

	}

	public static String removeNoiseSymbol(String oldBody, String lang) {
		String newBody = "";

		if (lang.equals("ar")) {
			newBody = oldBody.replaceAll("[^Ã-ú\\s]+", "").replaceAll("( )+",
					" ");
		} else {
			newBody = oldBody.replaceAll("[^a-zA-Z\\s]+", "").replaceAll(
					"( )+", " ");
		}
		return newBody;
	}

	public static String removeNoiseSentence(String oldBody, int t) {
		// t is the threshold
		String newBody = "";

		String[] bodyLines = oldBody.split("\n");
		for (String line : bodyLines) {
			if (line.split("\\s").length > t) {
				newBody += line + "\n";
			}
		}
		return newBody;
	}

	static String QueryForlder = "queries";
	static String LogFile = "logfile.txt";
	static double threasholdCosine = -1;

	public static void main(String[] args) {
		System.out.println("main function started");
		System.out.println("################################");

		InfoRetrival info;
		// with API
		 //info=new WithAPI();

		// without API
		info = new WithoutAPI();

		// collect data
		// info.collectionData();

		// data Pre-Prossessing
		// info.dataPreProssessing();

		// data similarity
		info.calculateSimilaraty();
		
		System.out.println("main function finished");
		System.out.println("################################");
	}

}
