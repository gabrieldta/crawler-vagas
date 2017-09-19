package br.com.unibh.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import br.com.unibh.util.CommonMethods;

public class Main {

	private static final String HOME_PAGE = "https://www.vagas.com.br/";
	
	public static void main(String[] args) {
		System.out.println("Começando o crawler...");
		discoverVagas(); // descobere vagas
		crawlVagas(); // captura vagas
		System.out.println("Fim!");
	}
	
	/**
	 * Captura as vagas presentes neste path: src/resources/vagas/links/vagasLinks.txt
	 * Consequentemente grava o html das mesmas nesse path: src/resources/vagas/htmls/
	 */
	private static void crawlVagas() {
		System.out.println("Começando a capturar vagas.");
		List<String> vagasLinks = CommonMethods.readLinesFromFile("src/resources/vagas/links/vagasLinks.txt");
		
		for(String link : vagasLinks) {
			Document htmlVaga = CommonMethods.crawlHtml(link);
			
			Element e = htmlVaga.select(".id-da-vaga").first();
			Element e2 = htmlVaga.select(".tituloVaga ").first();
			
			if(e!= null && e2 != null) {
				String idVaga = e.ownText().replace("(", "").replace(")", "").trim();
				String nomeVaga = e2.ownText().trim();
				
				System.err.println("Salvando vaga " + nomeVaga + " do id " + idVaga);
				
				CommonMethods.saveStringToAFile(htmlVaga, "src/resources/vagas/htmls/vaga-" + idVaga + ".html");
			}
		}
	}
	
	private static void discoverVagas() {
		System.out.println("Começando o descobridor de vagas.");
		List<String> vagas = new ArrayList<>();
		vagas.add("Analista de Sistemas");
		//vagas.add("Desenvolvedor Web");
		
		Set<String> urlVagas = new HashSet<>(); // hashset impede urls repetidas
		
		for(String vaga : vagas) {
			String url = HOME_PAGE + "vagas-de-" + getNameUrl(vaga); // url de busca de vagas
			
			System.out.println("Estou requisitando a url " + url);
			
			Document doc = CommonMethods.crawlHtml(url); // pegando html da página;
			Elements vagasElements = doc.select(".vaga h2 > a[href]"); // pegando link das vagas
			
			System.out.println("Existem " + vagasElements.size() + " vagas nesse link.");
			
			for(Element e : vagasElements) {
				String href = e.attr("href");
				
				if(!href.startsWith(HOME_PAGE)) {
					href = HOME_PAGE + href;
				}
				
				urlVagas.add(href);
			}
		}
		
		StringBuilder str = new StringBuilder();
		
		for(String link : urlVagas) {
			str.append(link + "\n");
		}
		
		System.out.println("Gravando os links das vagas em um documento.");
		CommonMethods.saveStringToAFile(str.toString(), "src/resources/vagas/links/vagasLinks.txt");
	}
	
	private static String getNameUrl(String vaga) {
		return CommonMethods.removeAccents(vaga).trim().replace(" ", "-").toLowerCase();
	}

}
