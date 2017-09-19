package br.com.unibh.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class CommonMethods {

	public static List<String> userAgents = Arrays.asList(
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36",
			"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
			"Mozilla/5.0 (Windows NT 10.0; WOW64; rv:46.0) Gecko/20100101 Firefox/46.0",
			"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36"
			);
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String removeAccents(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		str = str.replaceAll("[^\\p{ASCII}]", "");
		return str;
	}
	
	/**
	 * Retrieve a random user agent from the user agents array.
	 * @return
	 */
	public static String randUserAgent() {
		return userAgents.get(randInt(0, userAgents.size() - 1));
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String removeParentheses(String str){

		if(str.contains("(")) {
			int x = str.indexOf("(");
			str = str.substring(0, x).replaceAll("'", "''").replaceAll("<", "").replaceAll(">", "");
		} else {
			return str.replaceAll("'", "''").replaceAll("<", "").replaceAll(">", "");
		}

		return str;
	}

	/**
	 * Delay in thread
	 */
	public static void delay() {
		int count = randInt(4, 9) * 1000;

		try {
			Thread.sleep(count);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Generates a random integer in the interval between min and max
	 * @param min
	 * @param max
	 * @return
	 */
	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
	
	/**
	 * Delay in thread with specific time
	 * @param delay
	 */
	public static void delay(int delay) {
		int count =  delay * 1000;

		try {
			Thread.sleep(count);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveStringToAFile(Object body, String path) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			
			out.write(body.toString());
			out.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static List<String> readLinesFromFile(String path) {
		List<String> lines = new ArrayList<>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(path)); 
			
			while(br.ready()){ 
		   		lines.add(br.readLine()); 
			} 
			
			br.close(); 
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return lines;
	}
	
	public static Document crawlHtml(String url) {
		Document doc = new Document("");
		
		try {
			delay(10); // delay de 10 s para não acabar com o site
			doc = Jsoup.connect(url)
				.timeout(30000) // timeout de 30s
				.userAgent(randUserAgent()) // pegando um user agent aleatorio
				.followRedirects(true) // seguindo redirecionamento
				.get();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return doc;
	}
}
