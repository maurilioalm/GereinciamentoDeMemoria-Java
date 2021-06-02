
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
			System.out.println("ESCOLHA A OP��O ENTRE OS TAMANHOS DE PAGINA��O DISPON�VEIS EM KB: 16,32 ou 64.");
			tamanho = sc.nextInt();
			if (tamanho != 16 && tamanho != 32 && tamanho != 64) {
				System.out.println("VALOR INCORRETO!");
			}
		} while (tamanho != 16 && tamanho != 32 && tamanho != 64);
		memoriaVirtual = tamanho;
		this.criarArrayDePaginas();
		quantPaginasVirtuais = calcularQuantPaginas(memoriaVirtual);
		quantPaginasVirtuaisRestante = calcularQuantPaginas(memoriaVirtual);
		System.out.println("\nVOC� TEM " + quantPaginasFisicas
				+ " P�GINAS DISPON�VEIS NA MEM�RIA F�SICA, CADA P�GINA COM O TAMANHO DE: " + this.tamamhoDasPaginas()
				+ "KB");
		System.out.println("Voc� tem " + this.calcularQuantPaginas(memoriaVirtual)
				+ " P�GINAS DISPON�VEIS NA MEM�RIA VIRTUAL, CADA P�GINA COM TAMANHO DE: " + this.tamamhoDasPaginas()
				+ "KB");
		this.printTamanhoMemoriaVirtual();
		sc.close();
	}

	// CALCULA A QUANTIDADE DE PAGINAS DA MEMORIA VIRTUAL DE ACORDO COM O TAMANHO DA
	// MEMORIA SELECIONADO.
	public int calcularQuantPaginas(int memoriaVirtual) {
		return (memoriaVirtual / 4);
	}

	// IMPRESS�O STATUS DA MEMORIA F�SICA E VIRTUAL
	public void printTamanhoMemoriaVirtual() {
		System.out.println("O TAMANHO DA SUA MEM�RIA F�SICA �: " + memoriaFisica + "KB");
		System.out.println("O TAMANHO DA SUA MEM�RIA VIRTUAL �: " + memoriaVirtual + "KB\n");
	}

	// DEFINE O TAMANHO DAS P�GINAS
	public int tamamhoDasPaginas() {
		return 4;
	}

	// CALCULA A QUANTIDADE DE P�GINAS NECESS�RIAS PARA CADA PROCESSO
	public int verificarQuatPaginaNecessaria(ProcessoVM processo) {
		int contador = 0;
		contador = processo.getTamanhoProcesso() / 4;
		if ((processo.getTamanhoProcesso() % 4) != 0) {
			contador += 1;
		}
		return contador;
	}

	// ADICIONA CADA PROCESSO AS P�GINAS DA MEMORIA PRINCIPAL (MEDOTO PRINCIPAL)
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
							+ ", ADICIONADO COM SUCESSO A P�GINA ID:" + pagina.getId()
							+ ", ESPA�O DISPON�VEL NO BLOCO: " + paginasFisicas[i].getEspacoDisponivel());
					System.out.println("MOMENTO QUE O PROCESSO FOI ALOCADO NESTE BLOCO: "
							+ paginasFisicas[i].getData().getTimeInMillis() + " (TIME).");
				}
			}
		} else {
			System.out.println("ESPA�O INSUFICIENTE NA RAM PARA ALOCAR O " + processo.getNome()
					+ " --> EXECUTAR ALGORITMO DE SUBSTITUI��O");
			controle = 0;
			FIFO(processo);
		}
		if (controle != 0) {
			quantidadeDePaginasDisponiveis();
			System.out.println("PROCESSO ADICIONADO COM SUCESSO!\n");
			this.pageMiss();
		}
	}

	// CALCULA O TAMANHO RESTANTE DE P�GINAS NECESS�RIAS PARA CADA PROCESSO
	public int calculoDoTamanhoRestantanteDoProcesso(int tamanho, int paginaTamanho) {
		if ((tamanho - paginaTamanho) < 0) {
			return 0;
		}
		return (tamanho - paginaTamanho);
	}

	// CALCULA O TAMANHO RESTANTE DA P�GINA
	public int calculoDoTamanhoRestanteDaPagina(int tamanho, int paginaTamanho) {
		if (tamanho >= paginaTamanho) {
			return 0;
		}
		return (paginaTamanho - tamanho);
	}

	// IMPRIME A QUANTIDADE DE P�GINAS DISPON�VEIS
	public void quantidadeDePaginasDisponiveis() {
		System.out.println("TEMOS " + quantPaginasFisicasRestante + " P�GINAS F�SICAS DISPON�VEIS.");
		System.out.println("TEMOS " + quantPaginasVirtuaisRestante + " P�GINAS VIRTUAIS DISPON�VEIS.");
		this.statusDaMemoria();
		this.statusDaMemoriaVirtual();
		this.fragmentacao();
	}

	// ALGORITMO DE SUBSTITUI��O
	public void FIFO(ProcessoVM processo) {
		System.out.println("ENTROU NO ALGORITMO");
		if (!verificarSeTemPaginaFisicaDisponivel(verificarQuatPaginaNecessaria(processo))) {
			System.out.println("EXECUTANDO");
			Pagina pagina = null; // paginasFisicas[0];
			// LA�O PARA PEGAR PRIMEIRA P�GINA OCUPADA.
			for (int j = 0; j < quantPaginasFisicas; j++) {
				if (pagina == null) {
					if (paginasFisicas[j] != null) {
						pagina = paginasFisicas[j];
					}
				}
			}
			// LA�O PARA COMPARAR DATAS
			for (int i = 0; i < quantPaginasFisicas; i++) {
				if (paginasFisicas[i] != null && paginasFisicas[i].getData().compareTo(pagina.getData()) == -1) {
					pagina = paginasFisicas[i];
				}
			}
			// LA�O PARA EXCLUIR P�GINAS
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

	// VERIFICA SE TEM A QUANTIDADE DE P�GINAS NECESS�RIAS PARA ALOCAR ESSE PROCESSO
	public boolean verificarSeTemPaginaFisicaDisponivel(int quantNecessaria) {
		if (quantNecessaria <= quantPaginasFisicasRestante) {
			return true;
		}
		return false;
	}

	// CALCULA E IMPRIME A FRAGMENTA��O INTERNA DAS P�GINAS F�SICAS
	public void fragmentacao() {
		int fragmentacao = 0;
		for (int i = 0; i < quantPaginasFisicas; i++) {
			if (paginasFisicas[i] != null)
				fragmentacao += paginasFisicas[i].getEspacoDisponivel();
		}
		System.out.println("TOTAL DE FRAGMENTA��O INTERNA: " + fragmentacao + "\n");
	}

	// FAZ A ALOCA��O DA P�GINA QUE ESTAVA NA MEM�RIA FISICA PARA MEM�RIA VIRTUAL.
	public void swapping(Pagina pagina) {
		System.out.println("FAZENDO SWAP OUT");
		int contador = 1; // SERVE PARA MOVER UMA �NICA P�GINA PARA N�O PREENCHER TODO ARRAY
		for (int i = 0; i < paginasVirtuais.length; i++) {
			if (paginasVirtuais[i] == null && contador != 0) {
				paginasVirtuais[i] = pagina;
				contador -= 1;
				quantPaginasVirtuaisRestante -= 1;
				System.out.println("PaginaID:" + pagina.getId() + " MOVIDA PARA MEM�RIA VIRTUAL.\n");
			}
		}
	}

	// PEGE MISS -> VERIFICA SE OS PROCESSOS QUE EST�O NA MEMORIA RAM NAQUELE
	// MOMENTO EST�O COMPLETOS
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

	// IMPRIME O STATUS DA MEM�RIA F�SICA (TOTAL, OCUPADA, DISPON�VEL, FRAGMENTADA)
	public void statusDaMemoria() {
		int aux = 0;
		for (int i = 0; i < 4; i++) {
			if (paginasFisicas[i] != null) {
				aux += 4;
			}
		}
		System.out.println("MEM�RIA F�SICA TOTAL: " + memoriaFisica + "KB, MEM�RIA F�SICA OCUPADA: " + aux +"KB.");
	}
	
	// IMPRIME O STATUS DA MEM�RIA VIRTUAL (TOTAL, OCUPADA, DISPON�VEL, FRAGMENTADA)
		public void statusDaMemoriaVirtual() {
			int aux = 0;
			for (int i = 0; i < quantPaginasVirtuais; i++) {
				if (paginasVirtuais[i] != null) {
					aux += 4;
				}
			}
			System.out.println("MEM�RIA VIRTUAL TOTAL: " + memoriaVirtual + "KB, MEM�RIA VIRTUAL OCUPADA: " + aux + "KB.");
		}
}
