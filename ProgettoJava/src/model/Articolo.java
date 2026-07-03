package model;

public class Articolo {

	Categoria categoria;
	double prezzo;
	String nota;
	
	Articolo (Categoria categoria, double prezzo, String nota) {
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
}
