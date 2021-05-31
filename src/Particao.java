public class Particao {

	static int tamanhoTotalMB = 16;
	static int fragmentacao = 0;
	int blocoMb = 4;
	Processo[] processos = new Processo[4];

	public Particao() {
		this.mensagemInicial();
	}

	// Alocação First-fit
	public void addProcessoAosBlocos(Processo processo) {
		if (this.verificarEspacoDisponivel()) {
			if (this.verificarTamanhoProcesso(processo)) {
				for (int i = 0; i < 4; i++) {
					if (processos[i] == null && processo.getControle() != 0) {
						processos[i] = processo;
						processo.setParticao(i);
						processo.setControle(0);
						System.out.println("Processo de ID: " + processo.getId() + " Nome: " + processo.getNome()
								+ " Adicionado com sucesso ao Bloco: " + processo.getParticao());
						this.verificarMemoriaDisponível();
					}
				}
			} else {
				processo.setControle(verificarQuantParticoes(processo.getValor()) + 1);
				for (int i = 0; i < 4; i++) {
					if (processos[i] == null && processo.getControle() != 0) {
						processos[i] = processo;
						processo.setParticao(i);
						processo.setControle(processo.getControle() - 1);
						System.out.println("Processo de ID: " + processo.getId() + " Nome: " + processo.getNome()
								+ " Adicionado com sucesso ao Bloco: " + processo.getParticao());
						this.verificarMemoriaDisponível();
					}
				}
			}
		} else
			System.out.println("Memoria cheia! " + processo.nome + " NÃO adicionado por falta de Memória disponível.");
	}

	// Verifica o tamanho do processo
	public boolean verificarTamanhoProcesso(Processo processo) {
		if (blocoMb - processo.getValor() >= 0) {
			return true;
		}
		return false;
	}

	// Se o processo for mais que o tamanho do bloco, indica quantos blocos serão
	// usados.
	public int verificarQuantParticoes(int valor) {
		int aux = 0;
		while (blocoMb - valor < 0) {
			fragmentacao += (valor - blocoMb);
			valor = valor - blocoMb;
			aux += 1;
		}
		return aux;
	}

	// Controlador de Memória com as informações
	public void verificarMemoriaDisponível() {
		int aux = 0;
		for (int i = 0; i < 4; i++) {
			if (processos[i] != null) {
				aux += 4;
			}
		}
		System.out.println("Memoria Total: " + tamanhoTotalMB + ", Memoria Ocupada: " + aux + "MB, Memoria Disponível: "
				+ (tamanhoTotalMB - aux) + "MB" + ", Fragmentação interna atual: " + fragmentacao + "MB");
	}

	public void mensagemInicial() {
		System.out.println("Partição fixa criada com sucesso." + " Tamanho total 16MB, divida em quatro blocos de 4MB");
	}

	public boolean verificarEspacoDisponivel() {
		for (int i = 0; i < 4; i++) {
			if (processos[i] == null) {
				return true;
			}
		}
		return false;
	}
//Retira o processo do bloco de forma aleatória
	public void retirarProcesso() {
		int random = (int)(Math.random() * 0+3);
		for (int i = 0; i < 4; i++) {
			if (processos[i].getId() == random) {
				Processo processoTemp = processos[i];
				processos[i] = null;
				System.out.println("Processo de ID: " + processoTemp.getId() + " Nome: " + processoTemp.getNome()
						+ " Retirado com sucesso do Bloco: " + processoTemp.getParticao());
			}

		}
	}

}
