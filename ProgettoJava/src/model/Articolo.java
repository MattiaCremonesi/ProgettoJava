package model;

public class Articolo {

	Categoria categoria;
	double prezzo;
	String nota;
	
	public Articolo (Categoria categoria, double prezzo, String nota) {
		if (categoria == null) {
			this.categoria = new Categoria ("non categorizzato");
		}
		else this.categoria = categoria;
		if (prezzo == 0) {
			this.prezzo = 0;
		}
		else this.prezzo = prezzo;
		if (nota == null || nota == "") {
			this.nota = "";
		}
		else this.nota = nota;
	}	

	public String getNota() {
		return this.nota;
	}

	public double getPrezzo() {
		return this.prezzo;
	}
	
	public Categoria getCategoria() {
		return this.categoria;
	}
	
	public void setNota (String nota) {
		this.nota = nota;
	}
	
	public void setPrezzo (double prezzo) {
		this.prezzo = prezzo;
	}
	
	public void setNota (Categoria categoria) {
		this.categoria = categoria;
	}
	
}












