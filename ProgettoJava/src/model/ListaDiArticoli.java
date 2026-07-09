package model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Rappresenta una lista della spesa contenente un elenco di articoli attivi
 * e un elenco di articoli cancellati (il cestino).
 * Implementa Iterable per permettere di scorrere tutti gli articoli (attivi e cancellati).
 * * @author IlTuoNome E IlNomeDelCompagno
 */
public class ListaDiArticoli {

	// Rendiamo esplicitamente privato anche il nome della lista
	private String nome;
	private ArrayList<Articolo> contenitoreArticolo = new ArrayList<>();
	private ArrayList<Articolo> articoliCancellati = new ArrayList<>();
	
	/**
	 * Crea una nuova lista con un nome e un articolo iniziale opzionale.
	 * * @param nome il nome della lista
	 * @param articolo l'articolo iniziale (può essere null)
	 */
	public ListaDiArticoli (String nome, Articolo articolo) {
		if (nome == null || nome.trim().equals("")) {
			this.nome = "Nuova Lista";
		} else {
			this.nome = nome;
		}
		
		if (articolo != null) {			 
			contenitoreArticolo.add(articolo);
		}
	}
	
	/**
	 * Restituisce il nome della lista.
	 * @return il nome della lista
	 */
	public String getListaNome () {
		return this.nome;
	}
	
	/**
	 * Rimuove un articolo dalla lista attiva e lo sposta nel cestino dei cancellati.
	 * (CORRETTO: Risolto bug del doppio remove)
	 * * @param a l'articolo da cancellare
	 */
	public void cancellaArticolo (Articolo a) {
		if (contenitoreArticolo.remove(a)) {
			articoliCancellati.add(a);
		}
	}
	
	/**
	 * Aggiunge un nuovo articolo alla lista degli articoli attivi.
	 * @param a l'articolo da aggiungere
	 */
	public void aggiungiArticolo (Articolo a) {
		if (a != null) {
			contenitoreArticolo.add(a);
		}
	}

	/**
	 * Ripristina un articolo dalla lista dei cancellati, riportandolo in quella attiva.
	 * * @param a l'articolo da ripristinare
	 */
	public void ripristinaArticolo(Articolo a) {
		if (articoliCancellati.remove(a)) {
			contenitoreArticolo.add(a);
		}
	}
	
	//Continuare con gli altri metodi, se vuoi continuo io
}