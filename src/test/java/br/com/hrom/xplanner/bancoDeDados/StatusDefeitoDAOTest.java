package br.com.hrom.xplanner.bancoDeDados;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.dbunit.DatabaseUnitException;
import org.xml.sax.SAXException;

import br.com.hrom.xplanner.modelo.dao.implementacoes.StatusDefeitoDAO;
import br.com.hrom.xplanner.modelo.entidades.StatusDefeito;

/**
 * Testes para a classe StatusDefeitoDAO
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class StatusDefeitoDAOTest extends AbstractDAOTest<StatusDefeitoDAO, StatusDefeito> {

	public StatusDefeitoDAOTest() throws ParserConfigurationException,
			SAXException, IOException, SQLException, DatabaseUnitException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		super();		
	}

	@Override
	protected String getDataSet() {
		return "src/test/resources/datasets/status_dataset.xml";
	}

	@Override
	protected List<StatusDefeito> populaListaEntidades() {
		List<StatusDefeito> lista = new ArrayList<>();
		lista.add(new StatusDefeito(1, "Aberto"));
		lista.add(new StatusDefeito(2, "Em Andamento"));
		lista.add(new StatusDefeito(3, "Concluído"));		
		
		return lista;
	}

	@Override
	protected Object getIdExistente() {
		return 1;
	}

	@Override
	protected Object getIdInexistente() {
		return 800;
	}

	@Override
	protected void atualizaEntidade(StatusDefeito entidade) {
		entidade.setStatus("status atualizado para testes");		
	}

	@Override
	protected StatusDefeito criaEntidadeDefault() {
		return new StatusDefeito(1, "Aberto");
	}

	@Override
	protected int quantidadeDeRegistrosNaTabela() {
		return 3;
	}
}
