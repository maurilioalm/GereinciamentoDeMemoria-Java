//       PARTE II
import java.util.ArrayList;

public class ProcessoVM {
	int id;
	String nome;
	int tamanhoProcesso;
	ArrayList<Pagina> paginas = new ArrayList<Pagina>();
	

	public ProcessoVM(int id, String nome, int tamanhoProcesso) {

		this.id = id;
		this.nome = nome;
		this.tamanhoProcesso = tamanhoProcesso;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getTamanhoProcesso() {
		return tamanhoProcesso;
	}

	public void setTamanhoProcesso(int tamanhoProcesso) {
		this.tamanhoProcesso = tamanhoProcesso;
	}

}
