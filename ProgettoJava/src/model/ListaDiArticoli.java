package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import model.exception.NumeroSbagliatoException;

/**
 * Rappresenta una lista della spesa contenente un elenco di articoli attivi
 * e un elenco di articoli cancellati (il cestino).
 * Implementa Iterable per permettere di scorrere tutti gli articoli (attivi e cancellati).
 * * @author Davide Aime e Mattia Cremonesi
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
	 * Restituisce una copia dell'elenco degli articoli attivi nella lista.
	 * @return un ArrayList contenente gli articoli attivi
	 */
	public ArrayList<Articolo> getArticoliAttivi() {
		// Restituisce una nuova lista che contiene gli stessi elementi (copia di sicurezza)
		return new ArrayList<>(this.contenitoreArticolo);
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
		
		if (prefisso == null || prefisso.trim().isEmpty()) {
			return null;
		}
		
		String prefissoLower = prefisso.trim().toLowerCase();
		for (Articolo a : contenitoreArticolo) {
			if (a.getNota() != null && a.getNota().toLowerCase().startsWith(prefissoLower)) {
				return a;
			}
		}
		for (Articolo a : articoliCancellati) {
			if (a.getNota() != null && a.getNota().toLowerCase().startsWith(prefissoLower)) {
				return a;
			}
		}
		return null;
	}
	
	/**
	 * Svuota definitivamente il cestino eliminando tutti gli articoli cancellati.
	 */
	public void svuotaCancellati() {
		this.articoliCancellati.clear();
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
				if (!hasNext()) {
					throw new NoSuchElementException("Nessun altro articolo disponibile nella lista.");
				}
				if (index < contenitoreArticolo.size()) {
					return contenitoreArticolo.get(index++);
				} else {
					return articoliCancellati.get(index++ - contenitoreArticolo.size());
				}
			}
		};
	}
	
	/**
	 * Restituisce una copia dell'elenco degli articoli presenti nel cestino (cancellati).
	 * @return un ArrayList contenente gli articoli cancellati
	 */
	public ArrayList<Articolo> getArticoliCancellati() {
		// Restituisce una nuova lista che contiene gli stessi elementi (copia di sicurezza)
		return new ArrayList<>(this.articoliCancellati);
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
}