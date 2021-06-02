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
		System.out.println("Blocos fixos criados com sucesso." + " Tamanho total 16MB, divido em quatro blocos de 4MB");
	}

	// ALOCACAO DE MEMORIA FIRST-FIT
	public void addProcessoAosBlocos(Processo processo) {
		if (this.verificarEspacoDisponivel(processo)) {
			if (this.verificarTamanhoProcesso(processo)) {
				for (int i = 0; i < 4; i++) {
					if (blocos[i] == null && processo.getControle() != 0) {
						processo.setParticao(i);
						processo.setControle(0);
						Bloco bloco = new Bloco();
						bloco.setProcesso(processo);
						bloco.setUsado(processo.getValor());
						blocos[i] = bloco;
						System.out.println("Processo de ID: " + blocos[i].getProcesso().getId() + " Nome: "
								+ blocos[i].getProcesso().getNome() + " Adicionado com sucesso ao Bloco: "
								+ blocos[i].getProcesso().getParticao());
						this.statusDaMemoria();
					}
				}
			} else {
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
						System.out.println("Processo de ID: " + blocos[i].getProcesso().getId() + " Nome: "
								+ blocos[i].getProcesso().getNome() + " Adicionado com sucesso ao Bloco: "
								+ blocos[i].getProcesso().getParticao());
						this.statusDaMemoria();
					}
				}
			}
		} else {
			System.out.println("Memoria cheia! " + processo.nome + " NÃO adicionado por falta de Memória disponível.");
			this.retirarProcesso(processo);
			System.out.println("FAZENDO SWAPPING!");
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

	// VERIFICA A FRAGMENTAÇÃO INTERNA DOS BLOCOS
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

	// VERIFICA A QUANTIDADE DE MEMORIA DISPONÍVEL
	public int verificarMemoriaDisponivel() {
		int memoDis = 16;
		for (int i = 0; i < 4; i++) {
			if (blocos[i] != null) {
				memoDis -= blocos[i].getUsado();
			}
		}
		return memoDis;
	}

	// VERIFICA A QUANTIDADE DE BLOCOS DISPONÍVEIS NA MEMORIA PRINCIPAL//
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
	// OS BLOCOS DESSE PROCESSO NA MEMORIA VIRTAL (SWAPPING)
	public void retirarProcesso(Processo processo) {
		int quantNecessariaPeloProcesso = this.verificaQuantBlocosNecessarios(processo.getValor());
		int random = (int) ((Math.random() * 10) % 4);
		for (int i = 0; i < 4; i++) {
			if (blocos[i] != null) {
				if (blocos[i].processo.getId() == random) {
					blocosMemoriaVirtual[i] = blocos[i];
					System.out.println("Bloco: " + i + " Liberado com sucesso." + blocos[i].processo.getNome()
							+ " Retirado com sucesso");
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

	// IMPRIME O STATUS DA MEMORIA (TOTAL, OCUPADA, DISPONÍVEL, FRAGMENTADA)
	public void statusDaMemoria() {
		int aux = 0;
		for (int i = 0; i < 4; i++) {
			if (blocos[i] != null) {
				aux += 4;
			}
		}
		System.out.println("Memoria Total: " + tamanhoTotalMB + ", Memoria Ocupada: " + aux + "MB, Memoria Disponível: "
				+ this.verificarMemoriaDisponivel() + "MB" + ", Fragmentação interna atual: "
				+ this.verificarFragmentacao() + "MB");
	}
}
