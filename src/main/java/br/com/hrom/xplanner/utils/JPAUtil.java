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
 * Classe com m�todos utilit�rios para auxiliar o framework JPA2. Os m�todos da classe ser�o chamados autom�ticamente
 * por meio do Weld (CDI)
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class JPAUtil implements Serializable {
	

	private static final long serialVersionUID = 1L;

	/**
	 * M�todo produtor (f�brica) que fornece um objeto EntityManagerFactory.
	 * 
	 * @return Um objeto EntityManagerFactory com escopo de aplica��o (compartilhado por todos as sess�es de usu�rio)
	 */
	@Produces @ApplicationScoped
	public EntityManagerFactory getEntityManagerFactory(){
		return Persistence.createEntityManagerFactory("xplanner_pu");
	}
	
	/**
	 * M�todo produtor (f�brica) que fornece um objeto EntityManager
	 * 
	 * @param factory Um objeto EntityMagerFactory respons�vel por criar os EntityManager. Ser� injetado pelo Weld (CDI)
	 * @return
	 */
	@Produces @RequestScoped
	public EntityManager getEntityManager(EntityManagerFactory factory){
		return factory.createEntityManager();
	}
	
	/**
	 * Encerra um EntityManager. O m�todo com o seu par�metro ser�o chamados automaticamente pelo Weld quando um 
	 * EntityManager gerenciado pelo mesmo (bean gerenciado) estiver fora do escopo
	 * 
	 * @param entityManager O EntityManager a ser encerrado.
	 */
	public void fechaEntityManager(@Disposes EntityManager entityManager){
		entityManager.close();
	}
}
