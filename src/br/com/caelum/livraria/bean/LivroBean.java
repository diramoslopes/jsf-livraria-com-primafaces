package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.modelo.LivroDataModel;

@ManagedBean
@ViewScoped
public class LivroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Livro livro = new Livro();
	
	private List<Livro> livros;

	private Integer autorId;
	
	private LivroDataModel livroDataModel = new LivroDataModel();
	
	public LivroDataModel getLivroDataModel() {
		return livroDataModel;
	}

	public void setLivroDataModel(LivroDataModel livroDataModel) {
		this.livroDataModel = livroDataModel;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public Integer getAutorId() {
		return autorId;
	}

	public Livro getLivro() {
		return livro;
	}

	public List<Livro> getLivros() {
		DAO<Livro> dao = new DAO<Livro>(Livro.class);
		
		if(this.livros == null) {
			this.livros = dao.listaTodos();
		}
		
		return livros;
	}

	public List<Autor> getAutores() {
		return new DAO<Autor>(Autor.class).listaTodos();
	}

	public List<Autor> getAutoresDoLivro() {
		return this.livro.getAutores();
	}

	public void carregarLivroPelaId() {
		this.livro = new DAO<Livro>(Livro.class).buscaPorId(this.livro.getId()); 
	}
	
	public void gravarAutor() {
		Autor autor = new DAO<Autor>(Autor.class).buscaPorId(this.autorId);
		this.livro.adicionaAutor(autor);
		System.out.println("Escrito por: " + autor.getNome());
	}

	public void gravar() {
		System.out.println("Gravando livro " + this.livro.getTitulo());

		if (livro.getAutores().isEmpty()) {
			FacesContext.getCurrentInstance().addMessage("autor",
					new FacesMessage("Livro deve ter pelo menos um Autor."));
			return;
		}
		
		DAO<Livro> dao = new DAO<Livro>(Livro.class);

		if(this.livro.getId() == null) {
			dao.adiciona(this.livro);
			
			this.livros = dao.listaTodos();
		} else {
			dao.atualiza(this.livro);
		}

		this.livro = new Livro();
	}

	public void remover(Livro livro) {
		System.out.println("Removendo livro");
		new DAO<Livro>(Livro.class).remove(livro);
	}
	
	public void removerAutorDoLivro(Autor autor) {
		this.livro.removeAutor(autor);
	}
	
	public void carregar(Livro livro) {
		System.out.println("Carregando livro");
		this.livro = livro;
	}
	
	public String formAutor() {
		System.out.println("Chamanda do formul�rio do Autor.");
		return "autor?faces-redirect=true";
	}

	public void comecaComDigitoUm(FacesContext fc, UIComponent component,
			Object value) throws ValidatorException {

		String valor = value.toString();
		if (!valor.startsWith("1")) {
			throw new ValidatorException(new FacesMessage(
					"ISBN deveria come�ar com 1"));
		}

	}
	
	public String pagAutor() {
		return "autor?faces-redirect=true";
	}
	
	public String pagCarousel() {
		return "carousel?faces-redirect=true";
	}
	
	public boolean precoEhMenor(Object valorColuna, Object filtroDigitado, Locale locale) { // java.util.Locale

        //tirando espa�os do filtro
        String textoDigitado = (filtroDigitado == null) ? null : filtroDigitado.toString().trim();

        System.out.println("Filtrando pelo " + textoDigitado + ", Valor do elemento: " + valorColuna);

        // o filtro � nulo ou vazio?
        if (textoDigitado == null || textoDigitado.equals("")) {
            return true;
        }

        // elemento da tabela � nulo?
        if (valorColuna == null) {
            return false;
        }

        try {
            // fazendo o parsing do filtro para converter para Double
            Double precoDigitado = Double.valueOf(textoDigitado);
            Double precoColuna = (Double) valorColuna;

            // comparando os valores, compareTo devolve um valor negativo se o value � menor do que o filtro
            return precoColuna.compareTo(precoDigitado) < 0;

        } catch (NumberFormatException e) {

            // usuario nao digitou um numero
            return false;
        }
}
	
}
