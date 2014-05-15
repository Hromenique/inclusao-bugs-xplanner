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
import org.xml.sax.SAXException;

import br.com.hrom.xplanner.testUtil.LeitorPersistenceXMLUtil;

/**
 * 
 * Classe abstrata que serve de estrutura para os testes das classes de DAO
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class AbstractDAOTest {

	private IDatabaseConnection conexao;
	private EntityManager entityManager;	
	private LeitorPersistenceXMLUtil leitor;
	
	public AbstractDAOTest() throws ParserConfigurationException, SAXException, IOException, SQLException, DatabaseUnitException{
		this.leitor = new LeitorPersistenceXMLUtil();
		iniciaConexao();
		iniciaEntityManager();
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
	
	/**
	 * Obtem um objeto DataSet (DBUnit) a partir de um XML contendo dados e estrutura do banco de dados
	 * 
	 * @param pathDatasetXML Caminho para o arquivo .xml que contém dados e estrutura do banco de dados. O arquivo deve seguir o padrão apresentado aqui: 
	 * <a href="http://dbunit.sourceforge.net/apidocs/org/dbunit/dataset/xml/FlatXmlDataSet.html">http://dbunit.sourceforge.net/apidocs/org/dbunit/dataset/xml/FlatXmlDataSet.html</a>
	 * @return
	 * @throws MalformedURLException
	 * @throws DataSetException
	 */
	public IDataSet getDataSet(String pathDatasetXML) throws MalformedURLException, DataSetException{
		File file = new File(pathDatasetXML);
		FlatXmlDataSetBuilder builderDataSet = new FlatXmlDataSetBuilder();	
		return builderDataSet.build(file);		
	}
	
	public IDatabaseConnection getConexao() {
		return conexao;
	}

	public void setConexao(IDatabaseConnection conexao) {
		this.conexao = conexao;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
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
