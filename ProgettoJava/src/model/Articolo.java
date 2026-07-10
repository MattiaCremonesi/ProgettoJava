package model;

/**
 * Rappresenta un articolo inseribile in una lista della spesa.
 * Ogni articolo ha una categoria, un prezzo e una nota opzionale.
 * * @author Davide Aime e Mattia Cremonesi
 */
public class Articolo {

	private Categoria categoria;
	private double prezzo;
	private String nota;
	
	/**
	 * Costruisce un nuovo Articolo. Applica valori di default se i parametri non sono validi.
	 * * @param categoria la categoria dell'articolo (se null, imposta "non categorizzato")
	 * @param prezzo il prezzo dell'articolo (se negativo, viene azzerato)
	 * @param nota una nota opzionale sull'articolo (se null o vuota, imposta "")
	 */
	public Articolo (Categoria categoria, double prezzo, String nota) {
		if (categoria == null) {
			this.categoria = new Categoria ("non categorizzato");
		} else {
			this.categoria = categoria;
		}
		
		if (prezzo < 0) {
			this.prezzo = 0;
		} else {
			this.prezzo = prezzo;
		}
		
		if (nota == null || nota.equals("")) {
			this.nota = "";
		} else {
			this.nota = nota;
		}
	}	

	/**
	 * Restituisce la nota associata all'articolo.
	 * @return la nota dell'articolo (stringa vuota se non presente)
	 */
	public String getNota() {
		return this.nota;
	}

	/**
	 * Restituisce il prezzo dell'articolo.
	 * @return il prezzo
	 */
	public double getPrezzo() {
		return this.prezzo;
	}
	
	/**
	 * Restituisce la categoria dell'articolo.
	 * @return l'oggetto Categoria
	 */
	public Categoria getCategoria() {
		return this.categoria;
	}
	
	/**
	 * Modifica la nota dell'articolo.
	 * @param nota la nuova nota (se null, viene impostata a stringa vuota)
	 */
	public void setNota (String nota) {
		if (nota == null) {
			this.nota = "";
		} else {
			this.nota = nota;
		}
	}
	
	/**
	 * Modifica il prezzo dell'articolo. Il prezzo viene aggiornato solo se maggiore o uguale a 0.
	 * @param prezzo il nuovo prezzo dell'articolo
	 */
	public void setPrezzo (double prezzo) {
		if (prezzo >= 0) {
			this.prezzo = prezzo;
		}
	}
	
	/**
	 * Modifica la categoria dell'articolo.
	 * @param categoria la nuova categoria (se null, imposta "non categorizzato")
	 */
	public void setCategoria (Categoria categoria) {
		if (categoria == null) {
			this.categoria = new Categoria("non categorizzato");
		} else {
			this.categoria = categoria;
		}
	}
	
	/**
	 * Restituisce una rappresentazione testuale leggibile dell'articolo.
	 * * @return la stringa che descrive l'articolo
	 */
	@Override
	public String toString() {
		return "Articolo [Nota=" + this.nota + ", Prezzo=" + this.prezzo + "€, Categoria=" + this.categoria.getNome() + "]";
	}
}