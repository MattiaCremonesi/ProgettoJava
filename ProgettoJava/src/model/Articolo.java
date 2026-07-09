package model;

public class Articolo {

	private Categoria categoria;
	private double prezzo;
	private String nota;
	
	public Articolo (Categoria categoria, double prezzo, String nota) {
		
		if (categoria == null) {
			this.categoria = new Categoria ("non categorizzato");
		}	
		else this.categoria = categoria;
		
		if (prezzo == 0) {
			this.prezzo = 0;
		}
		else this.prezzo = prezzo;
		
		if (nota == null || nota.equals("")) {
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
		if (nota == null) {
			this.nota = "";
		} else {
			this.nota = nota;
		}
	}
	
	public void setPrezzo (double prezzo) {
		if (prezzo >= 0) {
			this.prezzo = prezzo;
		}
	}
	
	public void setCategoria (Categoria categoria) {
		if (categoria == null) {
			this.categoria = new Categoria("non categorizzato");
		} else {
			this.categoria = categoria;
		}
	}
	
}












