package br.com.hrom.xplanner.utils;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * 
 * Classe com métodos utilitários para auxiliar o framework JPA2. Os métodos da classe serão chamados automáticamente
 * por meio do Weld (CDI)
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class JPAUtil implements Serializable {
	

	private static final long serialVersionUID = 1L;

	/**
	 * Método produtor (fábrica) que fornece um objeto EntityManagerFactory.
	 * 
	 * @return Um objeto EntityManagerFactory com escopo de aplicação (compartilhado por todos as sessões de usuário)
	 */
	@Produces @ApplicationScoped
	public EntityManagerFactory getEntityManagerFactory(){
		return Persistence.createEntityManagerFactory("xplanner_pu");
	}
	
	/**
	 * Método produtor (fábrica) que fornece um objeto EntityManager
	 * 
	 * @param factory Um objeto EntityMagerFactory responsável por criar os EntityManager. Será injetado pelo Weld (CDI)
	 * @return
	 */
	@Produces @RequestScoped
	public EntityManager getEntityManager(EntityManagerFactory factory){
		return factory.createEntityManager();
	}
	
	/**
	 * Encerra um EntityManager. O método com o seu parâmetro serão chamados automaticamente pelo Weld quando um 
	 * EntityManager gerenciado pelo mesmo (bean gerenciado) estiver fora do escopo
	 * 
	 * @param entityManager O EntityManager a ser encerrado.
	 */
	public void fechaEntityManager(@Disposes EntityManager entityManager){
		entityManager.close();
	}
}
