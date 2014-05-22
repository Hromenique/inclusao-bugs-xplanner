package br.com.hrom.xplanner.testUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.xml.parsers.ParserConfigurationException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseDataSet;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.xml.sax.SAXException;

/**
 * Esta classe contém métodos utilitários com as principais operações oferecidas pelo DB Unit a partir de arquivos (xml) de dataSets.
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class DBUnitUtil {

	private IDatabaseConnection databaseConnection;
	
	/**
	 * Construtor que cria uma nova instância de DBUnitUtil contendo uma conexão (IDatabaseConnection) criada com dados provenientes do arquivo <b>src/test/resources/META-INF/persistence.xml</b>
	 * 
	 * @throws SQLException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws DatabaseUnitException
	 */
	public DBUnitUtil() throws SQLException, ParserConfigurationException, SAXException, IOException, DatabaseUnitException{
		LeitorPersistenceXMLUtil leitor = new LeitorPersistenceXMLUtil();
		
		String url = leitor.getStringConexaoMysql();
		String usuario = leitor.getUsuario();
		String senha = leitor.getSenha();
		
		Connection connection = DriverManager.getConnection(url, usuario, senha);
		this.databaseConnection = new DatabaseConnection(connection);
	}
	
	/**
	 *  Construtor que cria uma nova instância de DBUnitUtil
	 * 
	 * @param databaseConnection Objeto do tipo IDatabaseConnection que representa uma conexão com banco de dados
	 */
	public DBUnitUtil(IDatabaseConnection databaseConnection){
		this.databaseConnection = databaseConnection;
	}
	
	public void insert(String pathDataSet) throws MalformedURLException, DataSetException, DatabaseUnitException, SQLException{
		DatabaseOperation.INSERT.execute(this.databaseConnection, getDataSet(pathDataSet));		
	}
	
	public void deleteAll(String pathDataSet) throws MalformedURLException, DataSetException, DatabaseUnitException, SQLException{
		DatabaseOperation.DELETE.execute(this.databaseConnection, getDataSet(pathDataSet));		
	}	
	
	/**
	 * Obtém um objeto DataSet (DBUnit) a partir de um XML contendo dados e estrutura do banco de dados
	 * 
	 * @param pathDatasetXML Caminho para o arquivo .xml que contém dados e estrutura do banco de dados. O arquivo deve seguir o padrão apresentado aqui: 
	 * <a href="http://dbunit.sourceforge.net/apidocs/org/dbunit/dataset/xml/FlatXmlDataSet.html">http://dbunit.sourceforge.net/apidocs/org/dbunit/dataset/xml/FlatXmlDataSet.html</a>
	 * @return
	 * @throws MalformedURLException
	 * @throws DataSetException
	 */
	private IDataSet getDataSet(String pathDatasetXML) throws MalformedURLException, DataSetException{
		File file = new File(pathDatasetXML);
		FlatXmlDataSetBuilder builderDataSet = new FlatXmlDataSetBuilder();	
		FlatXmlDataSet dataset = builderDataSet.build(file);
		
		return dataset;
	}	

	public IDatabaseConnection getDatabaseConnection() {
		return databaseConnection;
	}

	public void setDatabaseConnection(IDatabaseConnection databaseConnection) {
		this.databaseConnection = databaseConnection;
	}
}
