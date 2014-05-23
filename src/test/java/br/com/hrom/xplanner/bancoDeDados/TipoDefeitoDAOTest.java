package br.com.hrom.xplanner.bancoDeDados;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.dbunit.DatabaseUnitException;
import org.xml.sax.SAXException;

import br.com.hrom.xplanner.modelo.dao.implementacoes.TipoDefeitoDAO;
import br.com.hrom.xplanner.modelo.entidades.TipoDefeito;

/**
 * Teste da classe TipoDefeitoDAO
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class TipoDefeitoDAOTest extends AbstractDAOTest<TipoDefeitoDAO, TipoDefeito>{

	public TipoDefeitoDAOTest() throws ParserConfigurationException,
			SAXException, IOException, SQLException, DatabaseUnitException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		super();		
	}

	@Override
	protected String getDataSet() {
		return "src/test/resources/datasets/tipo_dataset.xml";
	}

	@Override
	protected List<TipoDefeito> populaListaEntidades() {
		
		List<TipoDefeito> lista = new ArrayList<TipoDefeito>();
		lista.add(new TipoDefeito(1l, "Requisitos"));
		lista.add(new TipoDefeito(2l, "Análise"));
		lista.add(new TipoDefeito(3l, "Codificação"));
		
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
	protected void atualizaEntidade(TipoDefeito entidade) {
		entidade.setNome("Tipo atualizado para testes");		
	}

	@Override
	protected TipoDefeito criaEntidadeDefault() {
		return new TipoDefeito(1l, "Requisitos");
	}

	@Override
	protected int quantidadeDeRegistrosNaTabela() {
		return 3;
	}

}
