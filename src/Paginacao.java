import java.util.Scanner;

public class Paginacao {

	static int memoriaFisica = 16;
	static int quantPaginasFisicas = 4;
	static int quantPaginasFisicasRestante = 4;
	static int memoriaVirtual = 0;
	static int quantPaginasVirtuais = 0;
	static int quantPaginasVirtuaisRestante = 0;
	Pagina[] paginasFisicas = new Pagina[4];
	Pagina[] paginasVirtuais = null;

	public Paginacao() {
		this.selecionarTamanhoMemoriaVirtual();
	}
	//Cria o array de paginas virtuais de acordo com o tamanho selecionado
	public void criarArrayDePaginas() {
		int i = memoriaVirtual / 4;
		this.paginasVirtuais = new Pagina[i];
	}
	//Permite a escolha do tamanho da Memoria virtual
	public void selecionarTamanhoMemoriaVirtual() {
		int tamanho = 0;
		do {
			System.out.println("Escolha o opção entre os tamanhos disponíveis de paginação em KB: 16,32 ou 64.");
			Scanner sc = new Scanner(System.in);
			tamanho = sc.nextInt();
			sc.close();
			if (tamanho != 16 && tamanho != 32 && tamanho != 64) {
				System.out.println("Valor incorreto!");
			}
		} while (tamanho != 16 && tamanho != 32 && tamanho != 64);
		switch (tamanho) {
		case 16: {
			memoriaVirtual = 16;
			break;
		}
		case 32: {
			memoriaVirtual = 32;
			break;
		}
		case 64: {
			memoriaVirtual = 64;
			break;
		}
		}
		this.criarArrayDePaginas();
		quantPaginasVirtuais = calcularQuantPaginas(memoriaVirtual);
		quantPaginasVirtuaisRestante = calcularQuantPaginas(memoriaVirtual);
		System.out.println("Você tem " + this.calcularQuantPaginas(memoriaVirtual)
				+ " páginas disponíveis na memoria virtual, cada pagina com tamanho de: " + this.tamamhoDasPaginas()
				+ "KB");
		System.out.println("Você tem " + quantPaginasFisicas
				+ " páginas disponíveis na memoria fisica, cada pagina com tamanho de: " + this.tamamhoDasPaginas()
				+ "KB");
		this.printTamanhoMemoriaVirtual();

	}
	//Calcula a Quantidade de Paginas que terá a memoria virtual, dependendo do tamanho da memória.
	public int calcularQuantPaginas(int memoriaVirtual) {
		return (memoriaVirtual / 4);
	}
	//Impressão de controle Memoria Física e Virtual.
	public void printTamanhoMemoriaVirtual() {
		System.out.println("O tamanho total da sua memoria fisica é: " + memoriaFisica + "KB");
		System.out.println("O tamanho total da sua memoria virtual é: " + memoriaVirtual + "KB");
	}
	//Define o tamanho das páginas sejam Física ou Virtuais.
	public int tamamhoDasPaginas() {
		return 4;
	}
	//Calcula a quantidade de páginas necessárias para cada processo.
	public int verificarQuatPaginaNecessaria(ProcessoVM processo) {
		int contador = 0;
		contador = processo.getTamanhoProcesso() / 4;
		if ((processo.getTamanhoProcesso() % 4) != 0) {
			contador += 1;
		}
		// System.out.println(contador);
		return contador;
	}
	//Adiciona o processo a Memória (MEDOTO PRINCIPAL)
	public void adiconarATabelaPaginas(ProcessoVM processo) {
		int quantPaginasNecessaria = verificarQuatPaginaNecessaria(processo);
		int contador = 0;
		int tamanhoRestanteDoProcesso = processo.getTamanhoProcesso();
		if (verificarSeTemPaginaFisicaDisponivel(quantPaginasNecessaria)) {
			for (int i = 0; i < quantPaginasFisicas && contador < quantPaginasNecessaria; i++) {
				if (paginasFisicas[i] == null) {
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Pagina pagina = new Pagina(i);
					paginasFisicas[i] = pagina;
					paginasFisicas[i].setProcesso(processo);
					paginasFisicas[i].setEspacoDisponivel(
							calculoDoTamanhoRestanteDaPagina(tamanhoRestanteDoProcesso, paginasFisicas[i].tamanho));
					tamanhoRestanteDoProcesso = this.calculoDoTamanhoRestantanteDoProcesso(tamanhoRestanteDoProcesso,
							paginasFisicas[i].getTamanho());
					quantPaginasFisicasRestante -= 1;
					contador += 1;
					System.out.println("Momento processo alocado: " + paginasFisicas[i].getData().getTimeInMillis());
					System.out.println("ProcessoID: " + processo.getId() + ", Nome: " + processo.getNome()
							+ ", adicionado a paginaID:" + pagina.getId() + ", espaço disponível no bloco: "
							+ paginasFisicas[i].getEspacoDisponivel());
				}
			}
		} else {
			System.out.println("Espaço insuficiente para o: " + processo.getNome() + " --> executar FIFO");
			FIFO(processo);
			System.out.println("Processo adicionado com sucesso!");
		}
		quantidadeDePaginasDisponiveis();
	}
	
	//Calcula o tamanho restante de espaço necessário para cada página de um processo.
	public int calculoDoTamanhoRestantanteDoProcesso(int tamanho, int paginaTamanho) {
		if ((tamanho - paginaTamanho) < 0) {
			return 0;
		}
		return (tamanho - paginaTamanho);
	}
	//Calcula o tamanho restante da página.
	public int calculoDoTamanhoRestanteDaPagina(int tamanho, int paginaTamanho) {
		if (tamanho >= paginaTamanho) {
			return 0;
		}
		return (paginaTamanho - tamanho);
	}
	// Calcula a quantidade de Páginas Disponíveis.
	public void quantidadeDePaginasDisponiveis() {
		System.out.println("Temos " + quantPaginasFisicasRestante + " páginas FISICAS disponíveis.");
		System.out.println("Temos " + quantPaginasVirtuaisRestante + " páginas VIRTUAIS disponíveis.");
		this.fragmentacao();
	}
	//Substitui uma página quando um novo processo precisa ser alocado e não tem espaço
	//suficiente na Memória Física.
	public void FIFO(ProcessoVM processo) {
		System.out.println("Entrou no FIFO");
		if (!verificarSeTemPaginaFisicaDisponivel(verificarQuatPaginaNecessaria(processo))) {
			System.out.println("Executando");
			Pagina pagina = paginasFisicas[0];
			// laço para Comparar datas
			for (int i = 0; i < quantPaginasFisicas; i++) {
				if (paginasFisicas[i] != null) {
					System.out.println(paginasFisicas[i].getData().compareTo(pagina.getData()));
				}
				if (paginasFisicas[i] != null && paginasFisicas[i].getData().compareTo(pagina.getData()) == -1) {
					pagina = paginasFisicas[i];
				}
			}
			// Laço para excluir pagina
			for (int i = 0; i < quantPaginasFisicas; i++) {
				if (paginasFisicas[i] != null && paginasFisicas[i] == pagina) {
					System.out.println("PaginaID:" + paginasFisicas[i].getId() + " REMOVIDA. Time = "
							+ paginasFisicas[i].getData().getTimeInMillis());
					this.swapping(paginasFisicas[i]);
					paginasFisicas[i] = null;
				}
			}

			quantPaginasFisicasRestante += 1;
			System.out.println("Tentar Adicionar " + processo.getNome() + " novamente");
			this.adiconarATabelaPaginas(processo);
		}
	}
	//Verifica a quantidade de Páginas Físicas Disponíveis.
	public boolean verificarSeTemPaginaFisicaDisponivel(int quantNecessaria) {
		if (quantNecessaria <= quantPaginasFisicasRestante) {
			return true;
		}
		return false;
	}
	//Calcula a fragmentação interna.
	public void fragmentacao() {
		int aux = 0;
		for (int i = 0; i < quantPaginasFisicas; i++) {
			if (paginasFisicas[i] != null)
				aux += paginasFisicas[i].getEspacoDisponivel();
		}
		System.out.println("Total de fragmentação interna: " + aux);
	}
	//Faz a alocação da página que estava na Memória Física na Virtual.
	public void swapping(Pagina pagina) {
		System.out.println("Entrou  no swap");
		int contador = 1;
		for (int i = 0; i < paginasVirtuais.length; i++) {
			if (paginasVirtuais[i] == null && contador != 0) {
				paginasVirtuais[i] = pagina;
				contador -= 1;
				quantPaginasVirtuaisRestante -= 1;
				System.out.println("PaginaID:" + pagina.getId() + " movida para memória virtual");
			}
		}
	}

}
