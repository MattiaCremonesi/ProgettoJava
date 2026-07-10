package ui.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import model.*;
import model.exception.NumeroSbagliatoException; 

/**
 * Gestisce l'interfaccia a riga di comando per interagire con le liste della spesa.
 * Mostra i menu testuali, prende gli input dell'utente e intercetta le eccezioni
 * per evitare il crash del programma permettendo di riprovare.
 * * @author IlTuoNome E IlNomeDelCompagno
 */
public class InterfacciaTestuale {

	/**
	 * Fa partire il programma e mantiene attivo il menu principale finche' l'utente non decide di uscire.
	 * * @param scanner lo scanner per leggere i comandi da terminale
	 */
	public void avvia(Scanner scanner) {
		boolean ripeti = true;
		
		do {
			System.out.println("\nMENU PRINCIPALE TERMINALE");
			System.out.println("[1] per creare una lista di articoli...");
			System.out.println("[2] per visualizzare o modificare/eliminare una lista di articoli...");
			System.out.println("[0] per uscire dal programma...");
			
			try {
				ripeti = menuPrincipale(scanner);
			} 
			catch (NumeroSbagliatoException e) {
				System.out.println ("Errore: " + e.getMessage());
			}
			catch (InputMismatchException e) {
				System.out.println("Errore: Tipo di input non valido. Inserisci un numero.");
				scanner.nextLine();
			} 
			catch (Exception e) {
				System.out.printf("Si è verificato un errore imprevisto: %s\n", e.getMessage());
			}
		} while (ripeti);	
	}
	
	/**
	 * Gestisce le scelte del menu principale smistando l'utente verso la creazione o la gestione.
	 * * @param scanner lo scanner per l'input
	 * @return true se si vuole continuare, false se si sceglie di uscire
	 * @throws NumeroSbagliatoException se il numero inserito non corrisponde a nessuna opzione
	 */
	static boolean menuPrincipale(Scanner scanner) throws NumeroSbagliatoException {
		int creaVisualizzaLista = scanner.nextInt();
		scanner.nextLine();
		
		if (creaVisualizzaLista < 0 || creaVisualizzaLista > 2) {
			throw new NumeroSbagliatoException("Scelta non valida nel menu principale.");
		}
		
		if (creaVisualizzaLista == 0) {
			System.out.println("Uscita dall'interfaccia testuale.");
			return false;
		}
		else if (creaVisualizzaLista == 1) { 
			creaNuovaLista(scanner);
		}
		else if (creaVisualizzaLista == 2) {
			boolean ripetiVisualizzaModifica = true;
			do {
				System.out.println("\nGESTIONE LISTE");
				System.out.println("[1] per visualizzare le tue liste...");
				System.out.println("[2] per modificare o eliminare...");
				System.out.println("[0] per tornare indietro...");
				ripetiVisualizzaModifica = gestioneListe(scanner);
			}
			while (ripetiVisualizzaModifica);
		}
		return true;
	}

	/**
	 * Raccoglie i dati dall'utente e orchestra la creazione di una nuova lista nel model.
	 * * @param scanner lo scanner per l'input
	 * @throws NumeroSbagliatoException se ci sono errori durante l'inserimento dei dati
	 */
	static void creaNuovaLista(Scanner scanner) throws NumeroSbagliatoException {
		String nomeCategoria = chiediInfCategoria(scanner); 
		Categoria categoria = new Categoria(nomeCategoria);
		ArrayList<Object> listaInfArticoli = chiediInfArticolo(scanner);
		
		Articolo articolo = new Articolo(
										categoria, 
										(double) listaInfArticoli.get(0), 
										(String) listaInfArticoli.get(1)
										);
		String nomeLista = chiediInfLista(scanner);
		
		GestioneListe.InserisciCategoria(categoria);
		GestioneListe.InserisciArticolo(articolo);
		GestioneListe.CreaLista(nomeLista, articolo);
		System.out.println("Lista creata con successo!");
	}
	
	/**
	 * Smista l'utente tra la visualizzazione dei dati e le operazioni di modifica/cancellazione.
	 * * @param scanner lo scanner per l'input
	 * @return true per rimanere in questo menu, false per tornare indietro
	 * @throws NumeroSbagliatoException se la scelta non e' tra quelle previste
	 */
	static boolean gestioneListe(Scanner scanner) throws NumeroSbagliatoException {
		int visualizzaModifica = scanner.nextInt();
		scanner.nextLine();
		
		if (visualizzaModifica < 0 || visualizzaModifica > 2){
			throw new NumeroSbagliatoException("Scelta non valida in Gestione Liste.");
		}
		
		if (visualizzaModifica == 0) {
			return false;
		}
		
		if (visualizzaModifica == 1) {	
			boolean ripetiVisualizzaCerca = true;
			do {
				System.out.println ("\nVISUALIZZA LISTE\n");
				System.out.println ("[1] per visualizzare le liste");
				System.out.println ("[2] per cercare un articolo per prefisso");
				System.out.println ("[0] per tornare indietro");
				ripetiVisualizzaCerca = menuVisualizzaListe(scanner);
			} 
			while (ripetiVisualizzaCerca);
		}
		else if (visualizzaModifica == 2) {
			boolean ripetiModificaElimina = true;
			do {
				System.out.println("\nMODIFICA E CANCELLAZIONE");
				System.out.println("[1] per eliminare (Lista/Categoria/Articolo)...");
				System.out.println("[2] per modificare un articolo o una categoria...");
				System.out.println("[0] per tornare indietro...");
				ripetiModificaElimina = modificaCancellazione(scanner);
			} 
			while (ripetiModificaElimina);
		}
		return true;
	}

	/**
	 * Gestisce le opzioni di pura visualizzazione globale o selezione di una singola lista.
	 * * @param scanner lo scanner per l'input
	 * @return true per rimanere nel sottomenu, false per uscirne
	 * @throws NumeroSbagliatoException se l'input non è valido
	 */
	static boolean menuVisualizzaListe(Scanner scanner) throws NumeroSbagliatoException {
		int visCerca = scanner.nextInt();
		scanner.nextLine();
		
		if (visCerca < 0 || visCerca > 2) {
			throw new NumeroSbagliatoException("Opzione non valida.");
		}
		
		if (visCerca == 0) {
			return false;
		}
		
		if (GestioneListe.listeArticoli.isEmpty()) {
			System.out.println("L'archivio delle liste è vuoto");
			return true;
		}
		
		if (visCerca == 1) {
			stampaTutteLeListe();
		}
		else if (visCerca == 2) {
			selezionaEdEsploraLista(scanner);
		}
		return true;
	}

	/**
	 * Stampa a video tutte le liste presenti in archivio sfruttando l'iteratore polimorfico.
	 */
	static void stampaTutteLeListe() {
		System.out.println("Liste disponibili:");
		for (ListaDiArticoli liste : GestioneListe.listeArticoli.values()) {
			System.out.println("- " + liste.getListaNome());
			for (Articolo a : liste) {
				System.out.println("   -> Articolo: " + a.getNota() + " | Prezzo: " + a.getPrezzo() + "€");
			}
		}
	}

	/**
	 * Mostra un elenco numerato delle liste e permette all'utente di selezionarne una
	 * per fare ricerche o calcoli specifici.
	 * * @param scanner lo scanner per l'input
	 * @throws NumeroSbagliatoException se l'indice della lista scelta è errato
	 */
	static void selezionaEdEsploraLista(Scanner scanner) throws NumeroSbagliatoException {
		System.out.println("Liste disponibili:");
		ArrayList<ListaDiArticoli> elencoListe = new ArrayList<>(GestioneListe.listeArticoli.values());
		
		for (int i = 0; i < elencoListe.size(); i++) {
			System.out.println("[" + (i + 1) + "] " + elencoListe.get(i).getListaNome());
		}
		System.out.println("[0] per tornare indietro");
		
		int sceltaLista = scanner.nextInt();
		scanner.nextLine();
		
		if (sceltaLista == 0) {
			return;
		}
		
		if (sceltaLista > 0 && sceltaLista <= elencoListe.size()) {
			ListaDiArticoli listaSelezionata = elencoListe.get(sceltaLista - 1);
			eseguiOperazioniSuLista(scanner, listaSelezionata);
		} else {
			throw new NumeroSbagliatoException("Numero lista errato.");
		}
	}

	/**
	 * Mostra le operazioni matematiche e di ricerca eseguibili su una lista specifica.
	 * * @param scanner lo scanner per l'input
	 * @param listaSelezionata l'oggetto lista su cui operare
	 * @throws NumeroSbagliatoException se l'operazione scelta non esiste
	 */
	static void eseguiOperazioniSuLista(Scanner scanner, ListaDiArticoli listaSelezionata) throws NumeroSbagliatoException {
		System.out.println("\nMENU OPZIONI LISTA: " + listaSelezionata.getListaNome().toUpperCase() + " ---");
		System.out.println("[1] Cerca articolo per prefisso");
		System.out.println("[2] Calcola prezzo totale");
		System.out.println("[0] Torna indietro");
		
		int operazione = scanner.nextInt();
		scanner.nextLine();
		
		switch (operazione) {
			case 1:
				System.out.println("Inserisci il prefisso della nota da cercare:");
				String prefisso = scanner.nextLine();
				Articolo trovato = listaSelezionata.cercaArticoloPerPrefisso(prefisso);
				if (trovato != null) {
					System.out.println("Articolo trovato: " + trovato.getNota() + " | Prezzo: " + trovato.getPrezzo() + "€");
				} 
				else {
					System.out.println("Nessun articolo trovato.");
				}
				break;
			case 2:
				System.out.println("Prezzo totale degli articoli attivi: " + listaSelezionata.calcolaPrezzoTotale() + "€");
				break;
			case 0:
				break;
			default:
				throw new NumeroSbagliatoException("Operazione non valida.");
		}
	}
	
	/**
	 * Indirizza l'utente verso le operazioni di rimozione o di aggiornamento dei dati.
	 * * @param scanner lo scanner per l'input
	 * @return true per restare nel menu, false per salire di livello
	 * @throws NumeroSbagliatoException se il numero inserito e' fuori range
	 */
	static boolean modificaCancellazione(Scanner scanner) throws NumeroSbagliatoException {
		int eliminaModifica = scanner.nextInt();
		scanner.nextLine();
		
		if (eliminaModifica < 0 || eliminaModifica > 2) {
			throw new NumeroSbagliatoException("Scelta non valida in Modifica/Cancellazione.");
		}
		
		if (eliminaModifica == 0) {
			return false; 
		}
		
		if (eliminaModifica == 1) {
			boolean ripetiCancellazione = true;
			do {
				System.out.println("\nCANCELLAZIONE\n");
				System.out.println("[1] per eliminare una lista...");
				System.out.println("[2] per eliminare una categoria...");
				System.out.println("[3] per eliminare un articolo...");
				System.out.println("[0] per tornare indietro...");
				ripetiCancellazione = cancellazione(scanner);
			}
			while (ripetiCancellazione);
		}
		else if (eliminaModifica == 2) {
			boolean ripetiModifica = true; 
			do {
				System.out.println("\nMODIFICA ARTICOLO\n");
				System.out.println ("\n[1] per modificare il prezzo di un articolo...");
				System.out.println ("[2] per modificare la categoria di un articolo...");
				System.out.println ("[3] per modificare la nota di un articolo...");
				System.out.println ("[0] per tornare indietro...");
				ripetiModifica = modifica(scanner);
			}
			while (ripetiModifica);
		}
		return true;
	}
	
	/**
	 * Permette di cambiare le proprietà specifiche di un articolo.
	 * * @param scanner lo scanner per l'input
	 * @return true per continuare le modifiche, false se si sceglie di tornare indietro
	 * @throws NumeroSbagliatoException se l'input non è valido o l'articolo non esiste
	 */
	static boolean modifica(Scanner scanner) throws NumeroSbagliatoException {
		int cosaModificare = scanner.nextInt();
		scanner.nextLine();
		
		if (cosaModificare < 0 || cosaModificare > 3) {
			throw new NumeroSbagliatoException("Input di modifica non valido.");
		}
		
		if (cosaModificare == 0) {
			return false;
		}
		
		System.out.println("Inserisci la nota dell'articolo da modificare: ");
		String notaCercata = scanner.nextLine();
		
		Articolo articoloTrovato = GestioneListe.cercaArticoloGlobale(notaCercata);
	    
	    if (cosaModificare == 1) {
	    	System.out.println("Inserisci il prezzo da modificare: ");
	    	double prezzo = chiediPrezzo(scanner);
	    	articoloTrovato.setPrezzo(prezzo);
	    	System.out.println("Prezzo modificato: " + articoloTrovato.getPrezzo());
	    }
	    else if (cosaModificare == 2) {
	    	String nomeCat = chiediInfCategoria(scanner);
	    	Categoria categoriaCercata = GestioneListe.cercaCategoriaGlobale(nomeCat);
		    articoloTrovato.setCategoria(categoriaCercata);
		    System.out.println("Categoria modificata: " + articoloTrovato.getCategoria());
		}
	    else if (cosaModificare == 3) {
	    	System.out.println("Inserisci la nuova nota: ");
	    	String nota = scanner.nextLine();
	    	articoloTrovato.setNota(nota);
	    	System.out.println("Nota modificata: " + articoloTrovato.getNota());
	    }
		return true;
	}
	
	/**
	 * Gestisce la rimozione di liste, categorie o articoli dal sistema.
	 * * @param scanner lo scanner per l'input
	 * @return true per restare nel menu di rimozione, false per uscirne
	 * @throws NumeroSbagliatoException se si digita un numero errato o l'elemento non esiste
	 */
	static boolean cancellazione(Scanner scanner) throws NumeroSbagliatoException {
		int eliminaOggetto = scanner.nextInt();
		scanner.nextLine();
		
		if (eliminaOggetto < 0 || eliminaOggetto > 3) {
			throw new NumeroSbagliatoException("Numero di opzione cancellazione errato.");
		}
		
		if (eliminaOggetto == 0) {
			return false;
		}
		
		if (eliminaOggetto == 1) {
			System.out.println("Inserisci il nome della lista da eliminare...");
			String nomeListaEliminare = scanner.nextLine();	 
			if (GestioneListe.listeArticoli.containsKey(nomeListaEliminare)) {
				GestioneListe.CancellaLista(nomeListaEliminare);
				System.out.println("Lista eliminata con successo.");
			} else {
				throw new NumeroSbagliatoException("Nome della lista cercata non trovato nell'archivio.");
			}
		}
		else if (eliminaOggetto == 2) {
			System.out.println("Inserisci il nome della categoria da eliminare...");
			String nomeCat = scanner.nextLine();
			Categoria c = GestioneListe.cercaCategoriaGlobale(nomeCat);
			GestioneListe.CancellaCategoria(c);
			System.out.println("Categoria cancellata.");
		} 
		else if (eliminaOggetto == 3) {
			System.out.println("Inserisci la nota dell'articolo da eliminare...");
			String notaCercata = scanner.nextLine();
			Articolo a = GestioneListe.cercaArticoloGlobale(notaCercata);
			GestioneListe.CancellaArticolo(a);
			System.out.println("Articolo eliminato con successo.");
		}
		return true;
	}
	
	/**
	 * Chiede e raccoglie da terminale il nome di una categoria.
	 * * @param scanner lo scanner per l'input
	 * @return la stringa inserita
	 */
	static String chiediInfCategoria(Scanner scanner) {
		System.out.println("Inserisci il nome della categoria: ");
		return scanner.nextLine();
	}
	
	/**
	 * Raccoglie i dati necessari per configurare un nuovo articolo.
	 * * @param scanner lo scanner per l'input
	 * @return una struttura contenente il prezzo (posizione 0) e la nota (posizione 1)
	 * @throws NumeroSbagliatoException se il prezzo non e' valido
	 */
	static ArrayList<Object> chiediInfArticolo(Scanner scanner) throws NumeroSbagliatoException {
		ArrayList<Object> lista = new ArrayList<>();
		System.out.println("Inserisci il prezzo dell'articolo...");
		double prezzo = chiediPrezzo(scanner);
		lista.add(prezzo);
		
		System.out.println("Inserisci una nota per l'articolo (oppure premi 'n' per nessuna nota): ");
		String nota = scanner.nextLine();
		if (nota.equalsIgnoreCase("n")) {
			lista.add("");
		} 
		else {
			lista.add(nota);
		}
		return lista;
	}
	
	/**
	 * Chiede all'utente il nome da assegnare a una nuova lista.
	 * * @param scanner lo scanner per l'input
	 * @return il nome inserito
	 */
	static String chiediInfLista(Scanner scanner) {
		System.out.println("Inserisci il nome della lista...");
		return scanner.nextLine();
	}	
	
	/**
	 * Controlla che il valore inserito sia un prezzo valido.
	 * * @param scanner lo scanner per l'input
	 * @return il prezzo formattato e validato
	 * @throws NumeroSbagliatoException se l'utente inserisce un valore negativo
	 */
	static double chiediPrezzo(Scanner scanner) throws NumeroSbagliatoException {
		double prezzo = scanner.nextDouble();
		scanner.nextLine();
		if (prezzo < 0) {
			throw new NumeroSbagliatoException("Il prezzo inserito non può essere inferiore a zero.");
		}
		return prezzo;
	}
}