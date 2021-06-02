//       PARTE II
import java.util.Calendar;

public class Pagina {
	int tamanho = 4;
	int id;
	int espacoDisponivel;
	ProcessoVM processo;
	Calendar data = Calendar.getInstance();;
	
	public Pagina() {
	}

	public Pagina(int id) {
		this.id = id;
	}
	public ProcessoVM getProcesso() {
		return processo;
	}

	public void setProcesso(ProcessoVM processo) {
		this.processo = processo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	public int getEspacoDisponivel() {
		return espacoDisponivel;
	}

	public void setEspacoDisponivel(int espacoDisponivel) {
		if(espacoDisponivel < 0) {
			this.espacoDisponivel = espacoDisponivel * (-1);
		}else
		this.espacoDisponivel = espacoDisponivel;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}
	

}
