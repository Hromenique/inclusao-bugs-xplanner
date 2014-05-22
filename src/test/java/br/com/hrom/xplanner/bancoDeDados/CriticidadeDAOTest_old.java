package br.com.hrom.xplanner.bancoDeDados;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.parsers.ParserConfigurationException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import br.com.hrom.xplanner.modelo.dao.implementacoes.CriticidadeDefeitoDAO;
import br.com.hrom.xplanner.modelo.dao.interfaces.ICriticidadeDefeitoDAO;
import br.com.hrom.xplanner.modelo.entidades.CriticidadeDefeito;
import br.com.hrom.xplanner.testUtil.LeitorPersistenceXMLUtil;

/**
 * 
 * @author Hromenique Cezniowscki Leite Batista
 * 
 */
public class CriticidadeDAOTest_old {
	
	private IDatabaseConnection conexao;
	private EntityManager entityManager;
	private ICriticidadeDefeitoDAO dao;		
	private String DATA_SET = "src/test/resources/datasets/criticidade_dataset.xml";
	private LeitorPersistenceXMLUtil leitor;
	
	public CriticidadeDAOTest_old() throws SQLException, DatabaseUnitException, ParserConfigurationException, SAXException, IOException{
		this.leitor = new LeitorPersistenceXMLUtil();
		iniciaConexao();
		iniciaEntityManager();		
		this.dao = new CriticidadeDefeitoDAO(entityManager);
	}
	
	@Before
	public void iniciaDados() throws DatabaseUnitException, SQLException, MalformedURLException{
		DatabaseOperation.INSERT.execute(this.conexao, getDataSet(this.DATA_SET));
		
	}
	
	@After
	public void excluiDados() throws MalformedURLException, DataSetException, DatabaseUnitException, SQLException{
		DatabaseOperation.DELETE_ALL.execute(this.conexao, getDataSet(this.DATA_SET));
	}	
	
	@Test
	public void testBuscaDadosExistentes() {
		CriticidadeDefeito criticidade = dao.buscaPorId(1L);

		assertEquals(1L, criticidade.getId());
		assertEquals("Baixa", criticidade.getCriticidade());		
	}
	
	@Test
	public void testBuscaDadosInexistentes(){
		CriticidadeDefeito criticidade = dao.buscaPorId(10L);
		assertEquals(null, criticidade);
	}
	
	private IDataSet getDataSet(String path) throws MalformedURLException, DataSetException{
		File file = new File(path);
		FlatXmlDataSetBuilder builderDataSet = new FlatXmlDataSetBuilder();	
		return builderDataSet.build(file);		
	}
	
	private void iniciaEntityManager(){
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(leitor.getPersistenceUnit());
		this.entityManager = factory.createEntityManager();	
	}
	
	private void iniciaConexao() throws SQLException, DatabaseUnitException{
		String url = leitor.getStringConexaoMysql();
		String usuario = leitor.getUsuario();
		String senha = leitor.getSenha();
		Connection connection = DriverManager.getConnection(url, usuario, senha);
		this.conexao = new DatabaseConnection(connection);
	}
}
