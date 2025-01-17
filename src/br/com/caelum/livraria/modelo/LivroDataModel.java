package br.com.caelum.livraria.modelo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.caelum.livraria.dao.DAO;

public class LivroDataModel extends LazyDataModel<Livro> {
	
	
	DAO<Livro> dao = new DAO<Livro>(Livro.class);
	
	public LivroDataModel(){
		super.setRowCount(dao.quantidadeDeElementos());
	}
	
	@Override
//	public List<Livro> load(int inicio, int quantidade, String campoOrdenacao, SortOrder sentidoOrdenacao, Map<String, Object> filtros) {
//	    String titulo = (String) filtros.get("titulo");
//
//	    return dao.listaTodosPaginada(inicio, quantidade, "titulo", titulo);
//	}
	public List<Livro> load(int inicio, int quantidade, String campoOrdenacao, SortOrder sentidoOrdenacao,
            Map<String, Object> filtros) {
        String titulo = (String) filtros.get("titulo");

        List<Livro> data = dao.listaTodosPaginada(inicio, quantidade, "titulo", titulo);

        // sort
        if (campoOrdenacao != null) {
            Collections.sort(data, new LazySorter(campoOrdenacao, sentidoOrdenacao));
        }

        return data;
    }
	
}