package model;

import java.util.ArrayList;
import java.util.Iterator;
import model.exception.NumeroSbagliatoException;

/**
 * Rappresenta una lista della spesa contenente un elenco di articoli attivi
 * e un elenco di articoli cancellati (il cestino).
 * Implementa Iterable per permettere di scorrere tutti gli articoli (attivi e cancellati).
 * * @author IlTuoNome E IlNomeDelCompagno
 */
public class ListaDiArticoli implements Iterable<Articolo> {

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
	 * * @return il nome della lista
	 */
	public String getListaNome () {
		return this.nome;
	}
	
	/**
	 * Rimuove un articolo dalla lista attiva e lo sposta nel cestino dei cancellati.
	 * * @param a l'articolo da cancellare
	 */
	public void cancellaArticolo (Articolo a) {
		if (contenitoreArticolo.remove(a)) {
			articoliCancellati.add(a);
		}
	}
	
	/**
	 * Aggiunge un nuovo articolo alla lista degli articoli attivi.
	 * * @param a l'articolo da aggiungere
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

	/**
	 * Calcola il prezzo totale di tutti gli articoli attivi presenti nella lista.
	 * * @return il prezzo totale degli articoli
	 */
	public double calcolaPrezzoTotale() {
		double totale = 0;
		for (Articolo a : contenitoreArticolo) {
			totale += a.getPrezzo();
		}
		return totale;
	}

	/**
	 * Cerca un articolo all'interno della lista controllando se il prefisso della 
	 * nota corrisponde alla stringa cercata. La ricerca avviene sia tra gli articoli 
	 * attivi che tra quelli cancellati.
	 * * @param prefisso il prefisso della nota da cercare
	 * @return l'articolo trovato, oppure null se non esiste
	 */
	public Articolo cercaArticoloPerPrefisso(String prefisso) {
		for (Articolo a : contenitoreArticolo) {
			if (a.getNota().startsWith(prefisso)) {
				return a;
			}
		}
		for (Articolo a : articoliCancellati) {
			if (a.getNota().startsWith(prefisso)) {
				return a;
			}
		}
		return null;
	}

	/**
	 * Restituisce un iteratore personalizzato che permette di scorrere sequenzialmente 
	 * prima tutti gli articoli attivi e successivamente tutti gli articoli cancellati.
	 * * @return un Iterator di tipo Articolo
	 */
	@Override
	public Iterator<Articolo> iterator() {
		return new Iterator<Articolo>() {
			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < (contenitoreArticolo.size() + articoliCancellati.size());
			}

			@Override
			public Articolo next() {
				if (index < contenitoreArticolo.size()) {
					return contenitoreArticolo.get(index++);
				} else {
					return articoliCancellati.get(index++ - contenitoreArticolo.size());
				}
			}
		};
	}
}