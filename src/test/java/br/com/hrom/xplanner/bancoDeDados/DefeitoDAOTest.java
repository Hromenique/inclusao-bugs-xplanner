package br.com.hrom.xplanner.bancoDeDados;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.junit.After;
import org.junit.Before;
import org.xml.sax.SAXException;

import br.com.hrom.xplanner.modelo.dao.implementacoes.DefeitoDAO;
import br.com.hrom.xplanner.modelo.dao.implementacoes.PersonDAO;
import br.com.hrom.xplanner.modelo.dao.implementacoes.StoryDAO;
import br.com.hrom.xplanner.modelo.dao.interfaces.IPersonDAO;
import br.com.hrom.xplanner.modelo.dao.interfaces.IStoryDAO;
import br.com.hrom.xplanner.modelo.entidades.ClassificacaoDefeito;
import br.com.hrom.xplanner.modelo.entidades.CriticidadeDefeito;
import br.com.hrom.xplanner.modelo.entidades.Defeito;
import br.com.hrom.xplanner.modelo.entidades.Person;
import br.com.hrom.xplanner.modelo.entidades.StatusDefeito;
import br.com.hrom.xplanner.modelo.entidades.TipoDefeito;
import br.com.hrom.xplanner.modelo.entidades.UserStory;

/**
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class DefeitoDAOTest extends AbstractDAOTest<DefeitoDAO, Defeito>{

	private IStoryDAO storyDAO;
	private IPersonDAO personDAO;	
	
	public DefeitoDAOTest() throws ParserConfigurationException, SAXException,
			IOException, SQLException, DatabaseUnitException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		super();

		storyDAO = new StoryDAO(jpaUtil.getEntityManager());
		personDAO = new PersonDAO(jpaUtil.getEntityManager());
	}
	
	@Override
	@Before
	public void iniciaDados() throws DatabaseUnitException, SQLException,
			MalformedURLException {		
		this.excluiDados();
		
		//Respons�vel/Person
		dbUnitUtil.insert("src/test/resources/datasets/person_dataset.xml");	
		//Tipo
		dbUnitUtil.insert("src/test/resources/datasets/tipo_dataset.xml");
		//Classifica��o
		dbUnitUtil.insert("src/test/resources/datasets/classificacao_dataset.xml");
		//Criticidade
		dbUnitUtil.insert("src/test/resources/datasets/criticidade_dataset.xml");
		//Status
		dbUnitUtil.insert("src/test/resources/datasets/status_dataset.xml");	
		//Est�ria/UserStory
		dbUnitUtil.insert("src/test/resources/datasets/story_dataset.xml");
		super.iniciaDados();
	}
	
	@Override
	@After
	public void excluiDados() throws MalformedURLException, DataSetException,
			DatabaseUnitException, SQLException {		
		super.excluiDados();
		//Est�ria/UserStory
		dbUnitUtil.deleteAll("src/test/resources/datasets/story_dataset.xml");
		//Respons�vel/Person
		dbUnitUtil.deleteAll("src/test/resources/datasets/person_dataset.xml");		
		//Tipo
		dbUnitUtil.deleteAll("src/test/resources/datasets/tipo_dataset.xml");
		//Classifica��o
		dbUnitUtil.deleteAll("src/test/resources/datasets/classificacao_dataset.xml");
		//Criticidade
		dbUnitUtil.deleteAll("src/test/resources/datasets/criticidade_dataset.xml");
		//Status
		dbUnitUtil.deleteAll("src/test/resources/datasets/status_dataset.xml");		
	}

	@Override
	protected String getDataSet() {
		return "src/test/resources/datasets/defeito_dataset.xml";
	}

	@Override
	protected List<Defeito> populaListaEntidades() {
		List<Defeito> lista = new ArrayList<>();
		
		
		Person responsavel = new Person(80, "usr_tst", "usuario_teste", "no@reply.com", null, "TST");
		CriticidadeDefeito criticidade = new CriticidadeDefeito(1, "Baixa");
		TipoDefeito tipo = new TipoDefeito(1, "Requisitos");
		ClassificacaoDefeito classificacao = new ClassificacaoDefeito(1,"Impeditivo");
		StatusDefeito status = new StatusDefeito(2, "Em Andamento") ;
		UserStory estoria = new UserStory(800, "teste1", "descri��o teste1", responsavel);
		Defeito defeito = new Defeito(1, "defeito1", "teste defeito1", estoria , responsavel , criticidade, tipo, classificacao, status);
		
		lista.add(defeito);
		
		responsavel = new Person(81, "usr_tst2", "usuario_teste2", "no@reply.com", null, "TST2");				
		criticidade = new CriticidadeDefeito(3, "Alta");
		tipo = new TipoDefeito(3, "Codifica��o");
		classificacao = new ClassificacaoDefeito(2,"Funcional");
		status = new StatusDefeito(3, "Conclu�do") ;
		estoria = new UserStory(801, "teste2", "descri��o teste2", responsavel);
		defeito = new Defeito(2, "defeito2", "teste defeito2", estoria , responsavel , criticidade, tipo, classificacao, status);
		
		lista.add(defeito);
		
		return lista;
	}

	@Override
	protected Object getIdExistente() {
		return 1l;
	}

	@Override
	protected Object getIdInexistente() {
		return 600l;
	}

	@Override
	protected void atualizaEntidade(Defeito entidade) {
		entidade.setTitulo("t�tulo atualizado");		
	}

	@Override
	protected Defeito criaEntidadeDefault() {
		Person responsavel = new Person(80, "usr_tst", "usuario_teste", "no@reply.com", null, "TST");
		UserStory estoria = new UserStory(800, "teste1", "descri��o teste1", responsavel);
		CriticidadeDefeito criticidade = new CriticidadeDefeito(1, "Baixa");
		TipoDefeito tipo = new TipoDefeito(1, "Requisitos");
		ClassificacaoDefeito classificacao = new ClassificacaoDefeito(1,"Impeditivo");
		StatusDefeito status = new StatusDefeito(2, "Em Andamento") ;
		return  new Defeito(1, "defeito1", "teste defeito1", estoria , responsavel , criticidade, tipo, classificacao, status);		
	}

	@Override
	protected int quantidadeDeRegistrosNaTabela() {
		return 2;
	}
}
