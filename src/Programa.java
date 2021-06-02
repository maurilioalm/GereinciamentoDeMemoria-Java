//       PARTE I E II EXECUCAO

public class Programa {

	public static void main(String[] args) {
		// PARTE I

		MemoriaRam memoriaRam = new MemoriaRam();

		Processo processo1 = new Processo(1, "PROCESSO 01", 11);
		Processo processo2 = new Processo(2, "PROCESSO 02", 4);
		Processo processo3 = new Processo(3, "PROCESSO 03", 14);

		memoriaRam.addProcessoAosBlocos(processo1);
		memoriaRam.addProcessoAosBlocos(processo2);
		memoriaRam.addProcessoAosBlocos(processo3);

		/**********************************************************************/
		// PARTE II

//		Paginacao tabela = new Paginacao();
//
//		ProcessoVM processoVM1 = new ProcessoVM(1, "PROCESSO 01", 13);
//		ProcessoVM processoVM2 = new ProcessoVM(2, "PROCESSO 02", 7);
//		ProcessoVM processoVM3 = new ProcessoVM(3, "PROCESSO 03", 4);
//
//		tabela.adiconarATabelaPaginas(processoVM1);
//		tabela.adiconarATabelaPaginas(processoVM2);
//		tabela.adiconarATabelaPaginas(processoVM3);
	}
}
