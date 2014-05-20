package br.com.hrom.xplanner.interceptors;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

/**
 * Interceptador que implementa a lógica (abrir/fechar) uma transação JPA 2
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */

@Interceptor
@Transacional
public class TransacionalInterceptor implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	@AroundInvoke
	public Object intercepta(InvocationContext context) throws Exception{
		Object resultado = null;
		//TODO Incluir logs
		try{
			entityManager.getTransaction().begin();
			resultado = context.proceed();
			entityManager.getTransaction().commit();
		}
		catch(Exception exception){
			if(entityManager.getTransaction().isActive()){
				entityManager.getTransaction().rollback();
			}
			
			throw exception;
		}
		
		return resultado;
	}

}
