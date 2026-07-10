package ui.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import model.*;
import model.exception.NumeroSbagliatoException; 

/**
 * Gestisce l'interfaccia utente da riga di comando (Terminale) per il sistema di gestione liste.
 * Fornisce menu interattivi catturando le eccezioni e permettendo di ripetere le operazioni.
 * * @author IlTuoNome E IlNomeDelCompagno
 */
public class InterfacciaTestuale {

	/**
	 * Avvia il ciclo principale dell'interfaccia testuale gestendo i menu e le eccezioni globali.
	 * * @param scanner lo scanner di sistema per leggere l'input
	 */
	public void avvia(Scanner scanner) {
		
		boolean ripeti = true;
		
		do {
			System.out.println("\nMENU PRINCIPALE TERMINALE");
			System.out.println("[1] per creare una lista di articoli...");
			System.out.println("[2] per visualizzare o modificare/eliminare una lista di articoli...");
			System.out.println("[0] per uscire dal programma...");
			
			try {
				ripeti = MenuPrincipale (scanner);
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
	 * Gestisce la logica del menu principale.
	 * * @param scanner lo scanner per l'input
	 * @return true se il programma deve continuare, false se l'utente desidera uscire
	 * @throws NumeroSbagliatoException se viene inserito un codice di menu non valido
	 */
	static boolean MenuPrincipale (Scanner scanner) throws NumeroSbagliatoException {
		
		int CreaVisualizzaLista = scanner.nextInt();
		scanner.nextLine();
		
		if (CreaVisualizzaLista == 0) {
			System.out.println("Uscita dall'interfaccia testuale.");
			return false;
		}
		else if (CreaVisualizzaLista == 1) { 
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
		else if (CreaVisualizzaLista == 2) {
			boolean ripetiVisualizzaModifica = true;
			do {
				System.out.println("\nGESTIONE LISTE");
				System.out.println("[1] per visualizzare le tue liste...");
				System.out.println("[2] per modificare o eliminare...");
				System.out.println("[0] per tornare indietro...");
				ripetiVisualizzaModifica = gestioneListe (scanner);
			}
			while (ripetiVisualizzaModifica);
		}
		else if (CreaVisualizzaLista < 0 || CreaVisualizzaLista > 2) {
			throw new NumeroSbagliatoException ("Scelta non valida nel menu principale.");
		}
		return true;
	}
	
	/**
	 * Gestisce le sotto-operazioni di visualizzazione globale e reindirizzamento alle modifiche delle liste.
	 * * @param scanner lo scanner per l'input
	 * @return true per rimanere nel menu corrente, false per tornare indietro
	 * @throws NumeroSbagliatoException se l'input numerico non rientra nelle opzioni disponibili
	 */
	static boolean gestioneListe (Scanner scanner) throws NumeroSbagliatoException {
		
		int VisualizzaModifica = scanner.nextInt();
		scanner.nextLine();
		
		if (VisualizzaModifica == 1) {						
			if (GestioneListe.listeArticoli.isEmpty()) {
				System.out.println("L'archivio delle liste è vuoto");
			} 
			else {
				System.out.println("Liste disponibili:");
				for (ListaDiArticoli liste : GestioneListe.listeArticoli.values()) {
					System.out.println("- " + liste.getListaNome());
					
					// Sfrutta l'iterazione polimorfica (Iterable) richiesta dal professore
					for (Articolo a : liste) {
						System.out.println("   -> Articolo: " + a.getNota() + " | Prezzo: " + a.getPrezzo() + "€");
					}
				}	
			}
		}
		else if (VisualizzaModifica == 2) {
			boolean ripetiModificaElimina = true;
			
			do {
				System.out.println("\nMODIFICA E CANCELLAZIONE");
				System.out.println("[1] per eliminare (Lista/Categoria/Articolo)...");
				System.out.println("[2] per modificare un articolo o una categoria...");
				System.out.println("[0] per tornare indietro...");
				ripetiModificaElimina = modificaCancellazione (scanner);
			} 
			while (ripetiModificaElimina);
		}
		else if (VisualizzaModifica == 0) {
			return false;
		}
		else if (VisualizzaModifica < 0 || VisualizzaModifica > 2){
			throw new NumeroSbagliatoException("Scelta non valida in Gestione Liste.");
		}
		return true;
	}
	
	/**
	 * Smista le scelte dell'utente tra i menu dedicati alla modifica o alla cancellazione degli elementi.
	 * * @param scanner lo scanner per l'input
	 * @return true per rimanere nel menu, false per risalire di un livello
	 * @throws NumeroSbagliatoException se l'utente inserisce un valore non valido
	 */
	static boolean modificaCancellazione (Scanner scanner) throws NumeroSbagliatoException {
		
		int eliminaModifica = scanner.nextInt();
		scanner.nextLine();
		
		if (eliminaModifica == 1) {
			boolean ripetiCancellazione = true;
			do {
				System.out.println("\nCANCELLAZIONE\n");
				System.out.println("[1] per eliminare una lista...");
				System.out.println("[2] per eliminare una categoria...");
				System.out.println("[3] per eliminare un articolo...");
				System.out.println("[0] per tornare indietro...");
				ripetiCancellazione = cancellazione (scanner);
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
				ripetiModifica = modifica (scanner);
			}
			while (ripetiModifica);
		}
		else if (eliminaModifica == 0) {
			return false; 
		}
		else if (eliminaModifica < 0 || eliminaModifica > 2) {
			throw new NumeroSbagliatoException ("Scelta non valida in Modifica/Cancellazione.");
		}
		return true;
	}
	
	/**
	 * Esegue le modifiche sui campi interni di un articolo recuperato tramite interazione con la logica del Model.
	 * * @param scanner lo scanner per l'input
	 * @return true per continuare l'interazione, false in caso di uscita volontaria
	 * @throws NumeroSbagliatoException se l'articolo non esiste o la scelta del campo è fuori range
	 */
	static boolean modifica (Scanner scanner) throws NumeroSbagliatoException {
		int cosaModificare = scanner.nextInt();
		scanner.nextLine();
		
		if (cosaModificare == 0) {
			return false;
		}
		else if (cosaModificare < 0 || cosaModificare > 3) {
			throw new NumeroSbagliatoException ("Input di modifica non valido.");
		}
		
		System.out.println ("Inserisci la nota dell'articolo da modificare: ");
		String notaCercata = scanner.nextLine();
		
		// Invocazione del metodo di ricerca centralizzato del model (niente cicli qui)
		Articolo articoloTrovato = GestioneListe.cercaArticoloGlobale(notaCercata);
	    
	    if (cosaModificare == 1) {
	    	System.out.println ("Inserisci il prezzo da modificare: ");
	    	double prezzo = chiediPrezzo (scanner);
	    	articoloTrovato.setPrezzo(prezzo);
	    	System.out.println ("Prezzo modificato: " + articoloTrovato.getPrezzo());
	    }
	    else if (cosaModificare == 2) {
	    	String nomeCat = chiediInfCategoria (scanner);
	    	Categoria categoriaCercata = GestioneListe.cercaCategoriaGlobale(nomeCat);
		    articoloTrovato.setCategoria(categoriaCercata);
		    System.out.println ("Categoria modificata: " + articoloTrovato.getCategoria());
		}
	    else if (cosaModificare == 3) {
	    	System.out.println ("Inserisci la nuova nota: ");
	    	String nota = scanner.nextLine();
	    	articoloTrovato.setNota(nota);
	    	System.out.println ("Nota modificata: " + articoloTrovato.getNota());
	    }
		return true;
	}
	
	/**
	 * Coordina l'eliminazione logica o fisica di liste, categorie o singoli articoli interrogando il Model.
	 * * @param scanner lo scanner per l'input
	 * @return true per rimanere nel sottomenu di cancellazione, false per arretrare
	 * @throws NumeroSbagliatoException se le risorse cercate non vengono trovate o l'input numerico è errato
	 */
	static boolean cancellazione (Scanner scanner) throws NumeroSbagliatoException {
		
		int eliminaOggetto = scanner.nextInt();
		scanner.nextLine();
		
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
		else if (eliminaOggetto == 0) {
			return false;
		}
		else if (eliminaOggetto < 0 || eliminaOggetto > 3) {
			throw new NumeroSbagliatoException ("Numero di opzione cancellazione errato.");
		}
		return true;
	}
	
	/**
	 * Richiede l'input testuale per il nome di una categoria.
	 * * @param scanner lo scanner per l'input
	 * @return il nome inserito dall'utente
	 */
	static String chiediInfCategoria(Scanner scanner) {
		System.out.println("Inserisci il nome della categoria: ");
		return scanner.nextLine();
	}
	
	/**
	 * Acquisisce i dettagli informativi iniziali per la creazione strutturata di un Articolo.
	 * * @param scanner lo scanner per l'input
	 * @return un ArrayList contenente nell'ordine il prezzo (Double) e la nota (String)
	 * @throws NumeroSbagliatoException se il prezzo inserito viola i limiti di validità
	 */
	static ArrayList<Object> chiediInfArticolo(Scanner scanner) throws NumeroSbagliatoException {
		
		ArrayList<Object> lista = new ArrayList<>();
		System.out.println("Inserisci il prezzo dell'articolo...");
		double prezzo = chiediPrezzo (scanner);
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
	 * Richiede l'input del nome identificativo da assegnare ad una nuova lista.
	 * * @param scanner lo scanner per l'input
	 * @return il nome identificativo letto
	 */
	static String chiediInfLista(Scanner scanner) {
		System.out.println("Inserisci il nome della lista...");
		return scanner.nextLine();
	}	
	
	/**
	 * Acquisisce e valida un valore monetario di tipo double.
	 * * @param scanner lo scanner per l'input
	 * @return il valore monetario positivo convalidato
	 * @throws NumeroSbagliatoException se il prezzo inserito è un numero negativo
	 */
	static double chiediPrezzo (Scanner scanner) throws NumeroSbagliatoException {
		double prezzo = scanner.nextDouble();
		scanner.nextLine();
		if (prezzo < 0) {
			throw new NumeroSbagliatoException ("Il prezzo inserito non può essere inferiore a zero.");
		}
		return prezzo;
	}
}