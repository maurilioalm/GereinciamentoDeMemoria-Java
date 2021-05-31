
public class Programa {

	public static void main(String[] args) {
		// Parte 1
		/*
		 * Processo processo1 = new Processo(1,"Processo 1", 4); Processo processo2 =
		 * new Processo(2,"Processo 2", 6); Processo processo3 = new
		 * Processo(3,"Processo 3", 4); Processo processo4 = new
		 * Processo(4,"Processo 4", 4); Processo processo5 = new
		 * Processo(5,"Processo 5", 3); Particao MemoriaRam = new Particao();
		 * 
		 * MemoriaRam.addProcessoAosBlocos(processo1);
		 * MemoriaRam.addProcessoAosBlocos(processo2);
		 * MemoriaRam.addProcessoAosBlocos(processo3);
		 * MemoriaRam.addProcessoAosBlocos(processo4); MemoriaRam.retirarProcesso();
		 * MemoriaRam.addProcessoAosBlocos(processo4);
		 * MemoriaRam.addProcessoAosBlocos(processo5);
		 */
		/**********************************************************************/
		// Parte 2
		Paginacao tabela = new Paginacao();
		ProcessoVM processo1 = new ProcessoVM(1,"Processo 01", 8);
		ProcessoVM processo2 = new ProcessoVM(2,"Processo 02", 9);
		ProcessoVM processo3 = new ProcessoVM(3,"Processo 03", 4);
		tabela.adiconarATabelaPaginas(processo1);
		tabela.adiconarATabelaPaginas(processo2);
		tabela.adiconarATabelaPaginas(processo3);
		
	}

}
