package br.com.hrom.xplanner.bancoDeDados;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.xml.parsers.ParserConfigurationException;

import junitx.framework.Assert;
import junitx.framework.ListAssert;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import br.com.hrom.xplanner.modelo.dao.interfaces.IDAO;
import br.com.hrom.xplanner.modelo.entidades.CriticidadeDefeito;
import br.com.hrom.xplanner.modelo.entidades.TipoDefeito;
import br.com.hrom.xplanner.testUtil.DBUnitUtil;
import br.com.hrom.xplanner.testUtil.JPATestUtil;

/**
 * 
 * Classe abstrata que serve de estrutura para os testes para operações em comum
 * das classes de DAO
 * 
 * @author Hromenique Cezniowscki Leite Batista
 * 
 */
public abstract class AbstractDAOTest<E extends IDAO<T>, T> {

	protected JPATestUtil jpaUtil;
	protected DBUnitUtil dbUnitUtil;

	// Define qual será a classe DAO alvo do teste
	private Class<E> daoClass;
	private T entidade;
	private List<T> listaEntidades;
	protected IDAO<T> dao;

	public AbstractDAOTest() throws ParserConfigurationException, SAXException,
			IOException, SQLException, DatabaseUnitException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {

		this.dbUnitUtil = new DBUnitUtil();
		this.jpaUtil = new JPATestUtil();
		this.listaEntidades = populaListaEntidades();
		this.entidade = criaEntidadeDefault();

		// Recupera uma referência para a classe de DAO
		daoClass = (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		//entidade = (T) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
		// cria uma nova classe de DAO por meio de sua referência
		dao = daoClass.getConstructor(EntityManager.class).newInstance(jpaUtil.getEntityManager());
	}

	@Before
	public void iniciaDados() throws DatabaseUnitException, SQLException, MalformedURLException{
		dbUnitUtil.insert(getDataSet());		
	}
	
	@After
	public void excluiDados() throws MalformedURLException, DataSetException, DatabaseUnitException, SQLException{
		dbUnitUtil.deleteAll(getDataSet());		
	}
	
	/**
	 * Retorna o caminho para um dataset
	 * 
	 * @return
	 */
	abstract protected String getDataSet();
	
	/**
	 * Retorna uma lista de entidades. A lista deve possui valores  que estão no banco de dados
	 * @return lista com entidades existentes no banco de dados
	 */
	abstract protected List<T> populaListaEntidades();
	
	/**
	 * Retorna um id de entidade existente
	 * 
	 * @return id da entidade
	 */
	abstract protected Object getIdExistente();
	
	/**
	 * Retorna um id de entidade que não existe
	 * 
	 * @return id que não pertence a nenhuma entidade
	 */
	abstract protected Object getIdInexistente();	
	
	/**
	 * 
	 * 
	 * @param entidade
	 * @return
	 */
	abstract protected void atualizaEntidade(T entidade);
	
	/**
	 * Cria uma entidade cujo os valores (atributos) serão alvos de busca e comparações. Assim, esta entidade deve representar um dado existente na tabela
	 * 
	 * @return entidade default que representa uma entidade da tabela
	 */
	abstract protected T criaEntidadeDefault();
	
	/**
	 * Retorna a quantidade de registros na tabela. Valor será o mesmo o de registro no arquivo de dataset
	 * @return quantidade de registros na tabela
	 */
	abstract protected int quantidadeDeRegistrosNaTabela();

	@Test
	public void testBuscaPorId(){
		T entidadeBD = dao.buscaPorId(getIdExistente());		
		assertEquals(entidade, entidadeBD);
	}
	
	@Test
	public void testBuscaPorIdSemResultado(){
		T entidadeBD = this.dao.buscaPorId(getIdInexistente());
		assertNull(entidadeBD);
	}
	
	@Test
	public void testBuscaTodos(){
		List<T> listaBD = this.dao.listaTodos();
		assertEquals(quantidadeDeRegistrosNaTabela(), listaBD.size());
		for(T entidade : listaEntidades){
			ListAssert.assertContains(listaBD, entidade);
		}		
	}
	
	@Test
	public void testBuscaTodosEmTabelaVazia(){
		try {
			excluiDados();
			List<T> listaBD = this.dao.listaTodos();
			assertEquals(0, listaBD.size());
			
		} 
		catch (MalformedURLException | DatabaseUnitException | SQLException e ) {
			Assert.fail("Ocorreu um erro", e);			
		}
	}
	
	@Test
	public void testSalvaDados(){
		try {
			excluiDados();			
			this.dao.salva(entidade);
			
			T entidadeBD = this.dao.buscaPorId(getIdExistente());			
			Assert.assertEquals(entidade, entidadeBD);			
		} 
		catch (MalformedURLException | DatabaseUnitException | SQLException e ) {
			Assert.fail("Ocorreu um erro", e);			
		}
	}
	
	@Test
	public void testDeletaDados(){
		T entidadeBD = this.dao.buscaPorId(getIdExistente());			
		Assert.assertEquals(entidade, entidadeBD);
		
		dao.exclui(entidade);		
		
		entidadeBD = this.dao.buscaPorId(getIdExistente());		
		assertEquals(null, entidadeBD);	
	}
	
	@Test
	public void atualizaDados(){
		T entidadeAtualizada = this.dao.buscaPorId(getIdExistente());	
		atualizaEntidade(entidadeAtualizada);				
		
		dao.atualiza(entidadeAtualizada);		
		
		T entidadeBD = this.dao.buscaPorId(getIdExistente());
		assertEquals(entidadeAtualizada, entidadeBD);			
	}
}
