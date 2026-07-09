package model;

/**
 * Rappresenta una categoria merceologica associabile agli articoli della spesa.
 * Le categorie sono condivise globalmente all'interno del sistema.
 * * @author Davide Aime e Mattia Cremonesi
 */
public class Categoria {
	
	private String nome;
	
	/**
	 * Crea una nuova Categoria con il nome specificato.
	 * Se il nome fornito è nullo o vuoto, viene assegnato il valore di default "non categorizzato".
	 * * @param nome il nome della categoria
	 */
	public Categoria (String nome) {
		if (nome == null || nome.trim().equals("")) {
			this.nome = "non categorizzato";
		} else {
			this.nome = nome;
		}
	}
	
	/**
	 * Restituisce il nome della categoria.
	 * * @return il nome della categoria
	 */
	public String getNome () {
		return this.nome;
	}

}