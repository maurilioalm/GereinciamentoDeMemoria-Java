
//       PARTE II
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

	// CONSTRUTOR
	public Paginacao() {
		this.selecionarTamanhoMemoriaVirtual();
	}

	// CRIA UM ARRAY DE PAGINAS VIRTUAIS DE ACORDO COM A QUANTIDADE SELECIONADA.
	public void criarArrayDePaginas() {
		int i = memoriaVirtual / 4;
		this.paginasVirtuais = new Pagina[i];
	}

	// PERMITE A ESCOLHA DO TAMANHO DA MEMORIA VIRTUAL
	public void selecionarTamanhoMemoriaVirtual() {
		int tamanho = 0;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("ESCOLHA A OPÇÃO ENTRE OS TAMANHOS DE PAGINAÇÃO DISPONÍVEIS EM KB: 16,32 ou 64.");
			tamanho = sc.nextInt();
			if (tamanho != 16 && tamanho != 32 && tamanho != 64) {
				System.out.println("VALOR INCORRETO!");
			}
		} while (tamanho != 16 && tamanho != 32 && tamanho != 64);
		memoriaVirtual = tamanho;
		this.criarArrayDePaginas();
		quantPaginasVirtuais = calcularQuantPaginas(memoriaVirtual);
		quantPaginasVirtuaisRestante = calcularQuantPaginas(memoriaVirtual);
		System.out.println("\nVOCÊ TEM " + quantPaginasFisicas
				+ " PÁGINAS DISPONÍVEIS NA MEMÓRIA FÍSICA, CADA PÁGINA COM O TAMANHO DE: " + this.tamamhoDasPaginas()
				+ "KB");
		System.out.println("Você tem " + this.calcularQuantPaginas(memoriaVirtual)
				+ " PÁGINAS DISPONÍVEIS NA MEMÓRIA VIRTUAL, CADA PÁGINA COM TAMANHO DE: " + this.tamamhoDasPaginas()
				+ "KB");
		this.printTamanhoMemoriaVirtual();
		sc.close();
	}

	// CALCULA A QUANTIDADE DE PAGINAS DA MEMORIA VIRTUAL DE ACORDO COM O TAMANHO DA
	// MEMORIA SELECIONADO.
	public int calcularQuantPaginas(int memoriaVirtual) {
		return (memoriaVirtual / 4);
	}

	// IMPRESSÃO STATUS DA MEMORIA FÍSICA E VIRTUAL
	public void printTamanhoMemoriaVirtual() {
		System.out.println("O TAMANHO DA SUA MEMÓRIA FÍSICA É: " + memoriaFisica + "KB");
		System.out.println("O TAMANHO DA SUA MEMÓRIA VIRTUAL É: " + memoriaVirtual + "KB\n");
	}

	// DEFINE O TAMANHO DAS PÁGINAS
	public int tamamhoDasPaginas() {
		return 4;
	}

	// CALCULA A QUANTIDADE DE PÁGINAS NECESSÁRIAS PARA CADA PROCESSO
	public int verificarQuatPaginaNecessaria(ProcessoVM processo) {
		int contador = 0;
		contador = processo.getTamanhoProcesso() / 4;
		if ((processo.getTamanhoProcesso() % 4) != 0) {
			contador += 1;
		}
		return contador;
	}

	// ADICIONA CADA PROCESSO AS PÁGINAS DA MEMORIA PRINCIPAL (MEDOTO PRINCIPAL)
	public void adiconarATabelaPaginas(ProcessoVM processo) {
		int controle = 1; // CONTROLAR OS PRINTS DE MEMORIA E PAGE MISS
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
					System.out.println("ADICIONANDO " + processo.getNome());
					System.out.println("PROCESSO DE ID: " + processo.getId() + ", NOME: " + processo.getNome()
							+ ", ADICIONADO COM SUCESSO A PÁGINA ID:" + pagina.getId()
							+ ", ESPAÇO DISPONÍVEL NO BLOCO: " + paginasFisicas[i].getEspacoDisponivel());
					System.out.println("MOMENTO QUE O PROCESSO FOI ALOCADO NESTE BLOCO: "
							+ paginasFisicas[i].getData().getTimeInMillis() + " (TIME).");
				}
			}
		} else {
			System.out.println("ESPAÇO INSUFICIENTE NA RAM PARA ALOCAR O " + processo.getNome()
					+ " --> EXECUTAR ALGORITMO DE SUBSTITUIÇÃO");
			controle = 0;
			FIFO(processo);
		}
		if (controle != 0) {
			quantidadeDePaginasDisponiveis();
			System.out.println("PROCESSO ADICIONADO COM SUCESSO!\n");
			this.pageMiss();
		}
	}

	// CALCULA O TAMANHO RESTANTE DE PÁGINAS NECESSÁRIAS PARA CADA PROCESSO
	public int calculoDoTamanhoRestantanteDoProcesso(int tamanho, int paginaTamanho) {
		if ((tamanho - paginaTamanho) < 0) {
			return 0;
		}
		return (tamanho - paginaTamanho);
	}

	// CALCULA O TAMANHO RESTANTE DA PÁGINA
	public int calculoDoTamanhoRestanteDaPagina(int tamanho, int paginaTamanho) {
		if (tamanho >= paginaTamanho) {
			return 0;
		}
		return (paginaTamanho - tamanho);
	}

	// IMPRIME A QUANTIDADE DE PÁGINAS DISPONÍVEIS
	public void quantidadeDePaginasDisponiveis() {
		System.out.println("TEMOS " + quantPaginasFisicasRestante + " PÁGINAS FÍSICAS DISPONÍVEIS.");
		System.out.println("TEMOS " + quantPaginasVirtuaisRestante + " PÁGINAS VIRTUAIS DISPONÍVEIS.");
		this.statusDaMemoria();
		this.statusDaMemoriaVirtual();
		this.fragmentacao();
	}

	// ALGORITMO DE SUBSTITUIÇÃO
	public void FIFO(ProcessoVM processo) {
		System.out.println("ENTROU NO ALGORITMO");
		if (!verificarSeTemPaginaFisicaDisponivel(verificarQuatPaginaNecessaria(processo))) {
			System.out.println("EXECUTANDO");
			Pagina pagina = null; // paginasFisicas[0];
			// LAÇO PARA PEGAR PRIMEIRA PÁGINA OCUPADA.
			for (int j = 0; j < quantPaginasFisicas; j++) {
				if (pagina == null) {
					if (paginasFisicas[j] != null) {
						pagina = paginasFisicas[j];
					}
				}
			}
			// LAÇO PARA COMPARAR DATAS
			for (int i = 0; i < quantPaginasFisicas; i++) {
				if (paginasFisicas[i] != null && paginasFisicas[i].getData().compareTo(pagina.getData()) == -1) {
					pagina = paginasFisicas[i];
				}
			}
			// LAÇO PARA EXCLUIR PÁGINAS
			for (int i = 0; i < quantPaginasFisicas; i++) {
				if (paginasFisicas[i] != null && paginasFisicas[i] == pagina) {
					System.out.println("PaginaID:" + paginasFisicas[i].getId() + " E TIME = "
							+ paginasFisicas[i].getData().getTimeInMillis() + " REMOVIDA COM SUCESSO.\n");
					this.swapping(paginasFisicas[i]);
					paginasFisicas[i] = null;
				}
			}

			quantPaginasFisicasRestante += 1;
			System.out.println("TENTAR ADICIONAR O " + processo.getNome() + " NOVAMENTE.\n");
			this.adiconarATabelaPaginas(processo);
		}
	}

	// VERIFICA SE TEM A QUANTIDADE DE PÁGINAS NECESSÁRIAS PARA ALOCAR ESSE PROCESSO
	public boolean verificarSeTemPaginaFisicaDisponivel(int quantNecessaria) {
		if (quantNecessaria <= quantPaginasFisicasRestante) {
			return true;
		}
		return false;
	}

	// CALCULA E IMPRIME A FRAGMENTAÇÃO INTERNA DAS PÁGINAS FÍSICAS
	public void fragmentacao() {
		int fragmentacao = 0;
		for (int i = 0; i < quantPaginasFisicas; i++) {
			if (paginasFisicas[i] != null)
				fragmentacao += paginasFisicas[i].getEspacoDisponivel();
		}
		System.out.println("TOTAL DE FRAGMENTAÇÃO INTERNA: " + fragmentacao + "\n");
	}

	// FAZ A ALOCAÇÃO DA PÁGINA QUE ESTAVA NA MEMÓRIA FISICA PARA MEMÓRIA VIRTUAL.
	public void swapping(Pagina pagina) {
		System.out.println("FAZENDO SWAP OUT");
		int contador = 1; // SERVE PARA MOVER UMA ÚNICA PÁGINA PARA NÂO PREENCHER TODO ARRAY
		for (int i = 0; i < paginasVirtuais.length; i++) {
			if (paginasVirtuais[i] == null && contador != 0) {
				paginasVirtuais[i] = pagina;
				contador -= 1;
				quantPaginasVirtuaisRestante -= 1;
				System.out.println("PaginaID:" + pagina.getId() + " MOVIDA PARA MEMÓRIA VIRTUAL.\n");
			}
		}
	}

	// PEGE MISS -> VERIFICA SE OS PROCESSOS QUE ESTÃO NA MEMORIA RAM NAQUELE
	// MOMENTO ESTÁO COMPLETOS
	public void pageMiss() {
		int count = 0;
		ProcessoVM processo = null;
		ProcessoVM processoControle = null;
		for (int i = 0; i < paginasFisicas.length; i++) {
			if (paginasFisicas[i] != null) {
				processo = paginasFisicas[i].getProcesso();
				if (processo != processoControle) {
					for (int j = 0; j < paginasVirtuais.length; j++) {
						if (paginasVirtuais[j] != null) {
							if (paginasFisicas[i].getProcesso().getId() == paginasVirtuais[j].getProcesso().getId()) {
								count++;
							} else {
								break;
							}
						} else {
							break;
						}
					}
					processoControle = processo;
				}
			}
		}
		System.out.println("PAGE MISS NESSE MOMENTO:" + count + "\n");
		processo = null;
	}

	// IMPRIME O STATUS DA MEMÓRIA FÍSICA (TOTAL, OCUPADA, DISPONÍVEL, FRAGMENTADA)
	public void statusDaMemoria() {
		int aux = 0;
		for (int i = 0; i < 4; i++) {
			if (paginasFisicas[i] != null) {
				aux += 4;
			}
		}
		System.out.println("MEMÓRIA FÍSICA TOTAL: " + memoriaFisica + "KB, MEMÓRIA FÍSICA OCUPADA: " + aux +"KB.");
	}
	
	// IMPRIME O STATUS DA MEMÓRIA VIRTUAL (TOTAL, OCUPADA, DISPONÍVEL, FRAGMENTADA)
		public void statusDaMemoriaVirtual() {
			int aux = 0;
			for (int i = 0; i < quantPaginasVirtuais; i++) {
				if (paginasVirtuais[i] != null) {
					aux += 4;
				}
			}
			System.out.println("MEMÓRIA VIRTUAL TOTAL: " + memoriaVirtual + "KB, MEMÓRIA VIRTUAL OCUPADA: " + aux + "KB.");
		}
}
