package br.com.hrom.xplanner.testUtil;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Classe com métodos utilitários para leitura e recuperação de valores do arquivo persistence.xml (configuração do JPA/Hibernate)
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class LeitorPersistenceXMLUtil {

	public static final String PATH_TEST = "src/test/resources/META-INF/persistence.xml";
	public static final String PATH_MAIN = "src/main/resources/META-INF/persistence.xml";
	private Document document;	
	
	/**
	 * Construtor que faz a leitura do arquivo persistence.xml locallizado em <b>src/test/resources/META-INF/</b>
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public LeitorPersistenceXMLUtil() throws ParserConfigurationException,
			SAXException, IOException {
		this(PATH_TEST);
	}
	
	/**
	 * Construtor que faz a leitura do arquivo persistence.xml com a localização definida pelo parâmetro <b>pathPersistenceXML</b>
	 * 
	 * @param pathPersistenceXML caminho (path) para o arquivo persistence.xml
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public LeitorPersistenceXMLUtil(String pathPersistenceXML)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
		this.document = documentBuilder.parse(pathPersistenceXML);
		this.document.normalize();
	}
	
	public String getStringConexaoMysql() {
		return getValorPropriedade("javax.persistence.jdbc.url");
	}
	
	public String getUsuario(){
		return getValorPropriedade("javax.persistence.jdbc.user");
	}
	
	public String getSenha(){
		return getValorPropriedade("javax.persistence.jdbc.password");
	}
	
	public String getPersistenceUnit(){
		Node persistenceUnitNo = document.getElementsByTagName("persistence-unit").item(0);
		NamedNodeMap atributos = persistenceUnitNo.getAttributes();
		Node nameNo = atributos.getNamedItem("name");
		return nameNo.getNodeValue();
	}

	public String getValorPropriedade(String nomePropriedade){
		NodeList properties = document.getElementsByTagName("property");
		String valorPropriedade = null;
		
		for(int i = 0; i < properties.getLength(); i++){
			Node nodeAtributo = properties.item(i).getAttributes().getNamedItem("name");
			
			if(nodeAtributo.getNodeValue().equals(nomePropriedade)){
				valorPropriedade = properties.item(i).getAttributes().getNamedItem("value").getNodeValue();
				break;
			}
		}
		return valorPropriedade;
	}
}
