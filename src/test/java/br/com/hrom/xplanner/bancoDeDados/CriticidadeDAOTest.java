package br.com.hrom.xplanner.bancoDeDados;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.dbunit.DatabaseUnitException;

import org.xml.sax.SAXException;

import br.com.hrom.xplanner.modelo.dao.implementacoes.CriticidadeDefeitoDAO;
import br.com.hrom.xplanner.modelo.entidades.CriticidadeDefeito;

/**
 * Teste da classe CriticidadeDefeitoDAO
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class CriticidadeDAOTest extends
		AbstractDAOTest<CriticidadeDefeitoDAO, CriticidadeDefeito> {

	public CriticidadeDAOTest() throws ParserConfigurationException,
			SAXException, IOException, SQLException, DatabaseUnitException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		super();
	}

	@Override
	protected String getDataSet() {
		return "src/test/resources/datasets/criticidade_dataset.xml";
	}

	@Override
	protected List<CriticidadeDefeito> populaListaEntidades() {
		List<CriticidadeDefeito> lista = new ArrayList<CriticidadeDefeito>();
		lista.add(new CriticidadeDefeito(1l, "Baixa"));
		lista.add(new CriticidadeDefeito(2l, "Média"));
		lista.add(new CriticidadeDefeito(3l, "Alta"));
		
		
		return lista;
	}

	@Override
	protected Object getIdExistente() {
		return 1l;
	}

	@Override
	protected Object getIdInexistente() {
		return 800l;
	}

	@Override
	protected void atualizaEntidade(CriticidadeDefeito entidade) {
		entidade.setCriticidade("Nome atualizado para teste");		
	}

	@Override
	protected CriticidadeDefeito criaEntidadeDefault() {
		return new CriticidadeDefeito(1l, "Baixa");
	}

	@Override
	protected int quantidadeDeRegistrosNaTabela() {
		return 3;
	}
}
