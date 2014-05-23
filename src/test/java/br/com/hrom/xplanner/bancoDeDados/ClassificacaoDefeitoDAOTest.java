package br.com.hrom.xplanner.bancoDeDados;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.dbunit.DatabaseUnitException;
import org.xml.sax.SAXException;

import br.com.hrom.xplanner.modelo.dao.implementacoes.ClassificacaoDefeitoDAO;
import br.com.hrom.xplanner.modelo.entidades.ClassificacaoDefeito;

/**
 * Teste da classe ClassificacaoDefeitoDAO
 * 
 * @author Hromenique Cezniowscki Leite Batista
 *
 */
public class ClassificacaoDefeitoDAOTest extends AbstractDAOTest<ClassificacaoDefeitoDAO, ClassificacaoDefeito>{

	public ClassificacaoDefeitoDAOTest() throws ParserConfigurationException,
			SAXException, IOException, SQLException, DatabaseUnitException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		super();		
	}

	@Override
	protected String getDataSet() {
		return "src/test/resources/datasets/classificacao_dataset.xml";
	}

	@Override
	protected List<ClassificacaoDefeito> populaListaEntidades() {
		List<ClassificacaoDefeito> lista = new ArrayList<>();
		lista.add(new ClassificacaoDefeito(1l, "Impeditivo"));
		lista.add(new ClassificacaoDefeito(2l, "Funcional"));
		lista.add(new ClassificacaoDefeito(3l, "Interface"));

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
	protected void atualizaEntidade(ClassificacaoDefeito entidade) {
		entidade.setNome("classificação atualizada para testes");		
	}

	@Override
	protected ClassificacaoDefeito criaEntidadeDefault() {
		return new ClassificacaoDefeito(1l, "Impeditivo");
	}

	@Override
	protected int quantidadeDeRegistrosNaTabela() {
		return 3;
	}
}
