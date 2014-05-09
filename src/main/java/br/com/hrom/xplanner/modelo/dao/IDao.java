package br.com.hrom.xplanner.modelo.dao;

import java.util.List;

/**
 * Interface com as funções básicas de DAO (Data Access Object)
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 * @param <T> Classe que representa a entidade de dado
 */
public interface IDao<T> {	
	List<T> listaTodos();
	T buscaPorId(Object id);
	void salva(T entidade);
	void exclui(Object id);
	void atualiza(T entidade);	
}

