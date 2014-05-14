package br.com.hrom.xplanner.bancoDeDados;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

/**
 * 
 * Testa a criação/atualização do banco de dados
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class IniciaBancoDeDadosTest {

	@Test
	public void iniciaBancoDeDados() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("xplanner_pu");
		EntityManager entityManager = factory.createEntityManager();
	}
}
