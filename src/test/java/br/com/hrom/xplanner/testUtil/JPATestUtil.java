package br.com.hrom.xplanner.testUtil;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class JPATestUtil {
	
	private EntityManager entityManager;
	
	public JPATestUtil() throws ParserConfigurationException, SAXException, IOException{
		LeitorPersistenceXMLUtil leitor = new LeitorPersistenceXMLUtil();
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(leitor.getPersistenceUnit());
		this.entityManager = factory.createEntityManager();			
	}
	
	public void beginTransaction(){
		this.entityManager.getTransaction().begin();
	}
	
	public void commitTransaction(){
		this.entityManager.getTransaction().commit();
	}
	
	public void rollbackTransaction(){
		this.entityManager.getTransaction().rollback();
	}	

	public EntityManager getEntityManager() {
		return entityManager;
	}	
}
