//       PARTE I
public class Processo {

	int id;
	String nome;
	int valor;
	int particao;
	int controle; // VARIAVEL PARA CONTROLAR A QUANTIDADE DE ALOCACOES NO ARRAY.

	public Processo(int id, String nome, int valor) {

		this.id = id;
		this.nome = nome;
		this.valor = valor;
		this.controle = 1;
	}

	public int getControle() {
		return controle;
	}

	public void setControle(int controle) {
		this.controle = controle;
	}

	public int getParticao() {
		return particao;
	}

	public void setParticao(int particao) {
		this.particao = particao;
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

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}
}
