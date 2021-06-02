//       PARTE I
// TRABALHAMOS COM MEMORIA FIXA.

public class MemoriaRam {

	static int tamanhoTotalMB = 16;
	static int memoriaDisponivel = 16;
	int blocoMb = 4;
	Bloco[] blocos = new Bloco[4];
	Bloco[] blocosMemoriaVirtual = new Bloco[8];

	// CONSTRUTOR
	public MemoriaRam() {
		// TODO Auto-generated constructor stub {
		this.mensagemInicial();
	}

	// MENSAGEM INICIAL
	public void mensagemInicial() {
		System.out.println("BLOCOS DE TAMANHO FIXO CRIADOS COM SUCESSO."
				+ " TAMANHO TOTAL 16MB, DIVIDO EM QUATRO BLOCOS DE 4MB.\n");
	}

	// ALOCACAO DE MEMORIA FIRST-FIT
	public void addProcessoAosBlocos(Processo processo) {
		if (this.verificarEspacoDisponivel(processo)) {
			if (this.verificarTamanhoProcesso(processo)) {
				System.out.println("TENTAR ADICIONAR PROCESSO.");
				for (int i = 0; i < 4; i++) {
					if (blocos[i] == null && processo.getControle() != 0) {
						processo.setParticao(i);
						processo.setControle(0);
						Bloco bloco = new Bloco();
						bloco.setProcesso(processo);
						bloco.setUsado(processo.getValor());
						blocos[i] = bloco;
						System.out.println("PROCESSO DE ID: " + blocos[i].getProcesso().getId() + " NOME: "
								+ blocos[i].getProcesso().getNome() + " ADICIONADO COM SUCESSO AO BLOCO: "
								+ blocos[i].getProcesso().getParticao());
						this.statusDaMemoria();
					}
				}
				System.out.println("PROCESSO ADICIONADO COM SUCESSO!");
				this.statusDaMemoriaVirtual();
			} else {
				System.out.println("TENTAR ADICIONAR PROCESSO.");
				processo.setControle(verificaQuantBlocosNecessarios(processo.getValor()));
				for (int i = 0; i < 4; i++) {
					if (blocos[i] == null && processo.getControle() != 0) {
						Processo processoTemp = processo;
						processo.setParticao(i);
						processo.setControle(processo.getControle() - 1);
						Bloco bloco = new Bloco();
						bloco.setProcesso(processo);
						if (processoTemp.getValor() > 4) {
							bloco.setUsado(4);
							processoTemp.setValor(processoTemp.getValor() - 4);
						} else {
							bloco.setUsado(processoTemp.getValor());
						}
						blocos[i] = bloco;
						System.out.println("PROCESSO ID: " + blocos[i].getProcesso().getId() + " NOME: "
								+ blocos[i].getProcesso().getNome() + " ADICIONADO COM SUCESSO AO BLOCO: "
								+ blocos[i].getProcesso().getParticao());
						this.statusDaMemoria();
					}
				}
				System.out.println("PROCESSO ADICIONADO COM SUCESSO!");
				this.statusDaMemoriaVirtual();
			}
		} else {
			System.out.println("MEMÓRIA CHEIA! " + processo.nome
					+ " NÃO ADICIONADO POR FALTA DE MEMÓRIA FÍSICA DISPONÍVEL.\nEXECUTAR LIBERAÇÃO DE ESPAÇO.\n");
			this.retirarProcesso(processo);
			System.out.println("\nFAZENDO SWAP OUT.");
			this.statusDaMemoriaVirtual();
			this.addProcessoAosBlocos(processo);
		}

	}

	// VERIFICA O TAMANHO DO PROCESSO
	public boolean verificarTamanhoProcesso(Processo processo) {
		if (blocoMb - processo.getValor() >= 0) {
			return true;
		}
		return false;
	}

	// VERIFICA A QUANTIDADE DE BLOCOS NECESSÁRIOS POR PROCESSO
	public int verificaQuantBlocosNecessarios(int valor) {
		int aux = 1;
		while ((blocoMb - valor) < 0) {
			valor = valor - blocoMb;
			aux += 1;
		}
		return aux;
	}

	// VERIFICA A FRAGMENTAÇÃO INTERNA DOS BLOCOS DA MEMÓRIA FÍSICA.
	public int verificarFragmentacao() {
		int fragmentacao = 0;
		for (int i = 0; i < 4; i++) {
			if (blocos[i] != null) {
				if (blocos[i].getUsado() <= 4) {
					fragmentacao += blocoMb - blocos[i].getUsado();
				}
			}
		}
		return fragmentacao;
	}

	// VERIFICA A FRAGMENTAÇÃO INTERNA DOS BLOCOS DA MEMÓRIA VIRTUAL.
	public int verificarFragmentacaoMemoriaVirtual() {
		int fragmentacao = 0;
		for (int i = 0; i < 8; i++) {
			if (blocosMemoriaVirtual[i] != null) {
				if (blocosMemoriaVirtual[i].getUsado() <= 4) {
					fragmentacao += blocoMb - blocosMemoriaVirtual[i].getUsado();
				}
			}
		}
		return fragmentacao;
	}

	// VERIFICA A QUANTIDADE DE BLOCOS DISPONÍVEIS NA MEMÓRIA PRINCIPAL//
	public int verificarBlocosDisponiveis() {
		int quantidade = 0;
		for (int i = 0; i < 4; i++) {
			if (blocos[i] == null) {
				quantidade++;
			}
		}
		return quantidade;
	}

	// VERIFICA SE O TAMANHO DO PROCESSO CABE NOS BLOCOS DISPONÍVEIS//
	public boolean verificarEspacoDisponivel(Processo processo) {
		int quantBlocosDesseProcesso = this.verificaQuantBlocosNecessarios(processo.getValor());
		int quantBlocosDisponiveis = this.verificarBlocosDisponiveis();
		if (quantBlocosDisponiveis < quantBlocosDesseProcesso) {
			return false;
		}
		return true;
	}

	// RETIRA UM PROCESSO DOS BLOCOS DE FORMA ALEATÓRIA E COLOCA
	// OS BLOCOS DESSE PROCESSO NA MEMÓRIA VIRTAL (SWAPPING)
	public void retirarProcesso(Processo processo) {
		int quantNecessariaPeloProcesso = this.verificaQuantBlocosNecessarios(processo.getValor());
		int random = (int) ((Math.random() * 10) % 4);
		for (int i = 0; i < 4; i++) {
			if (blocos[i] != null) {
				if (blocos[i].processo.getId() == random) {
					for (int j = 0; j < 8; j++) {
						if (blocosMemoriaVirtual[j] == null) {
							blocosMemoriaVirtual[j] = blocos[i];
							break;
						}
					}
					System.out.println("BLOCO: " + i + " LIBERADO COM SUCESSO! --> " + blocos[i].processo.getNome()
							+ " RETIRADO COM SUCESSO.");
					blocos[i] = null;
				}
			}
		}
		int blocosDisponiveis = this.verificarBlocosDisponiveis();
		if (blocosDisponiveis >= quantNecessariaPeloProcesso) {
			return;
		} else {
			this.retirarProcesso(processo);
		}
	}

	// IMPRIME O STATUS DA MEMÓRIA FÍSICA (TOTAL, OCUPADA, DISPONÍVEL, FRAGMENTADA)
	public void statusDaMemoria() {
		int aux = 0;
		for (int i = 0; i < 4; i++) {
			if (blocos[i] != null) {
				aux += 4;
			}
		}
		System.out.println("MEMÓRIA FÍSICA TOTAL: " + tamanhoTotalMB + ", MEMÓRIA FÍSICA OCUPADA: " + aux
				+ "MB, MEMÓRIA DISPONÍVEL: " + (tamanhoTotalMB - aux) + "MB" + ", FRAGMENTAÇÃO INTERNA ATUAL: "
				+ this.verificarFragmentacao() + "MB");
	}

	// IMPRIME O STATUS DA MEMÓRIA VIRTUAL (TOTAL, OCUPADA, DISPONÍVEL, FRAGMENTADA)
	public void statusDaMemoriaVirtual() {
		int aux = 0;
		for (int i = 0; i < 8; i++) {
			if (blocosMemoriaVirtual[i] != null) {
				aux += 4;
			}
		}
		System.out.println("\nMEMÓRIA VIRTUAL TOTAL: " + (blocoMb * 8) + ", MEMÓRIA VIRTUAL OCUPADA: " + aux
				+ "MB, MEMÓRIA DISPONÍVEL: " + ((blocoMb * 8) - aux) + "MB" + ", FRAGMENTAÇÃO INTERNA (VIRTUAL): "
				+ this.verificarFragmentacaoMemoriaVirtual() + "MB.\n");
	}
}
