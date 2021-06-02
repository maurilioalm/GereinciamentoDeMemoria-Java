//       PARTE I E II EXECUCAO

public class Programa {

	public static void main(String[] args) {
		// PARTE I

		MemoriaRam MemoriaRam = new MemoriaRam();

		Processo processo1 = new Processo(0, "PROCESSO 01", 12);
		Processo processo2 = new Processo(1, "PROCESSO 02", 15);
		Processo processo3 = new Processo(3, "PROCESSO 03", 4);

		MemoriaRam.addProcessoAosBlocos(processo1);
		MemoriaRam.addProcessoAosBlocos(processo2);
		MemoriaRam.addProcessoAosBlocos(processo3);

		/**********************************************************************/
		// PARTE II

//		Paginacao tabela = new Paginacao();
//
//		ProcessoVM processoVM1 = new ProcessoVM(1, "PROCESSO 01", 8);
//		ProcessoVM processoVM2 = new ProcessoVM(2, "PROCESSO 02", 9);
//		ProcessoVM processoVM3 = new ProcessoVM(3, "PROCESSO 03", 4);
//
//		tabela.adiconarATabelaPaginas(processoVM1);
//		tabela.adiconarATabelaPaginas(processoVM2);
//		tabela.adiconarATabelaPaginas(processoVM3);

	}

}
