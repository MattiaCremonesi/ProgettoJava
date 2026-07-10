package model;

import java.util.ArrayList;
import java.util.HashMap;
import model.exception.NumeroSbagliatoException;

/**
 * Classe di gestione centralizzata che mantiene l'archivio globale delle liste,
 * delle categorie e degli articoli del sistema.
 * Fornisce metodi statici per la creazione, la cancellazione e la ricerca degli elementi.
 * * @author IlTuoNome E IlNomeDelCompagno
 */
public class GestioneListe {

	public static HashMap<String, ListaDiArticoli> listeArticoli = new HashMap<>();
	
	public static ArrayList<Categoria> categorie = new ArrayList<>(); 
	
	public static ArrayList<Articolo> articoli = new ArrayList<>();
	
	/**
	 * Crea una nuova lista inserendola nell'archivio globale associata al suo nome.
	 * * @param nome il nome identificativo della lista
	 * @param articoloIniziale l'articolo inserito alla creazione (può essere null)
	 */
	public static void CreaLista (String nome, Articolo articoloIniziale) {
		ListaDiArticoli nuovaLista = new ListaDiArticoli(nome, articoloIniziale);
		listeArticoli.put(nome, nuovaLista);
	}
	
	/**
	 * Rimuove permanentemente una lista dall'archivio globale tramite il suo nome.
	 * * @param nome il nome della lista da eliminare
	 */
	public static void CancellaLista (String nome) {
		listeArticoli.remove(nome);
	}
	
	/**
	 * Inserisce una nuova categoria all'interno dell'elenco globale delle categorie condivise.
	 * * @param categoria la categoria da aggiungere
	 */
	public static void InserisciCategoria (Categoria categoria) {
		categorie.add(categoria);
	}
	
	/**
	 * Rimuove una categoria dall'elenco globale.
	 * * @param categoria la categoria da rimuovere
	 */
	public static void CancellaCategoria (Categoria categoria) {
		categorie.remove(categoria);
	}
	
	/**
	 * Inserisci un nuovo articolo all'interno dell'elenco globale degli articoli condivisi.
	 * * @param articolo l'articolo da aggiungere
	 */
	public static void InserisciArticolo (Articolo articolo) {
		articoli.add(articolo);
	}
	
	/**
	 * Rimuove un articolo dall'elenco globale e lo sposta nello stato cancellato 
	 * all'interno di tutte le liste in cui era presente.
	 * * @param articolo l'articolo da rimuovere
	 */
	public static void CancellaArticolo (Articolo articolo) {
		articoli.remove(articolo);
		for (ListaDiArticoli lista : listeArticoli.values()) {
			lista.cancellaArticolo(articolo);
		}
	}
	
	/**
	 * Cerca globalmente un articolo registrato nel sistema tramite la sua nota identificativa.
	 * Questo metodo centralizza la ricerca evitando la duplicazione di cicli for nelle interfacce.
	 * * @param nota la nota dell'articolo da cercare
	 * @return l'Articolo trovato
	 * @throws NumeroSbagliatoException se nessun articolo corrisponde alla nota inserita
	 */
	public static Articolo cercaArticoloGlobale(String nota) throws NumeroSbagliatoException {
		for (Articolo a : articoli) {
			if (a.getNota().equalsIgnoreCase(nota)) {
				return a;
			}
		}
		throw new NumeroSbagliatoException("Articolo non trovato con la nota inserita.");
	}

	/**
	 * Cerca globalmente una categoria registrata nel sistema tramite il suo nome.
	 * Questo metodo centralizza la ricerca evitando la duplicazione di cicli for nelle interfacce.
	 * * @param nome il nome della categoria da cercare
	 * @return la Categoria trovata
	 * @throws NumeroSbagliatoException se nessuna categoria corrisponde al nome inserito
	 */
	public static Categoria cercaCategoriaGlobale(String nome) throws NumeroSbagliatoException {
		for (Categoria c : categorie) {
			if (c.getNome().equalsIgnoreCase(nome)) {
				return c;
			}
		}
		throw new NumeroSbagliatoException("Categoria non trovata.");
	}
}