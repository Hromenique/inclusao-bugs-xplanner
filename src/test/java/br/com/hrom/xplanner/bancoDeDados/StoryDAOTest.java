package br.com.hrom.xplanner.bancoDeDados;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.dbunit.DatabaseUnitException;
import org.xml.sax.SAXException;

import br.com.hrom.xplanner.modelo.dao.implementacoes.StoryDAO;
import br.com.hrom.xplanner.modelo.entidades.UserStory;

public class StoryDAOTest extends AbstractDAOTest<StoryDAO, UserStory>{

	public StoryDAOTest() throws ParserConfigurationException, SAXException,
			IOException, SQLException, DatabaseUnitException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		super();		
	}

	@Override
	protected String getDataSet() {
		return "src/test/resources/datasets/story_dataset.xml";
	}

	@Override
	protected List<UserStory> populaListaEntidades() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object getIdExistente() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object getIdInexistente() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void atualizaEntidade(UserStory entidade) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected UserStory criaEntidadeDefault() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int quantidadeDeRegistrosNaTabela() {
		// TODO Auto-generated method stub
		return 0;
	}

}
