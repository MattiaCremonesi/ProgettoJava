package ui.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import model.*;
import model.exception.NumeroSbagliatoException; 

public class InterfacciaTestuale {

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
				System.out.println("Errore: " + e.getMessage());
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
	
	static boolean menuPrincipale(Scanner scanner) throws NumeroSbagliatoException {
		int creaVisualizzaLista = scanner.nextInt();
		scanner.nextLine();
		
		if (creaVisualizzaLista < 0 || creaVisualizzaLista > 2) {
			throw new NumeroSbagliatoException("Scelta non valida nel menu principale.");
		}
		
		switch (creaVisualizzaLista) {
			case 0:
				System.out.println("Uscita dall'interfaccia testuale.");
				return false;
			case 1:
				creaNuovaLista(scanner);
				break;
			case 2:
				boolean ripetiVisualizzaModifica = true;
				do {
					System.out.println("\nGESTIONE LISTE");
					System.out.println("[1] per visualizzare le tue liste...");
					System.out.println("[2] per modificare o eliminare...");
					System.out.println("[0] per tornare indietro...");
					ripetiVisualizzaModifica = gestioneListe(scanner);
				}
				while (ripetiVisualizzaModifica);
				break;
		}
		return true;
	}

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
		
		GestioneListe.inserisciCategoria(categoria);
		GestioneListe.inserisciArticolo(articolo);
		GestioneListe.creaLista(nomeLista, articolo);
		System.out.println("Lista creata con successo!");
	}
	
	static boolean gestioneListe(Scanner scanner) throws NumeroSbagliatoException {
		int visualizzaModifica = scanner.nextInt();
		scanner.nextLine();
		
		if (visualizzaModifica < 0 || visualizzaModifica > 2){
			throw new NumeroSbagliatoException("Scelta non valida in Gestione Liste.");
		}
		
		switch (visualizzaModifica) {
			case 0:
				return false;
			case 1:
				boolean ripetiVisualizzaCerca = true;
				do {
					System.out.println ("\nVISUALIZZA LISTE\n");
					System.out.println ("[1] per visualizzare le liste");
					System.out.println ("[2] per operare su una lista specifica");
					System.out.println ("[0] per tornare indietro");
					ripetiVisualizzaCerca = menuVisualizzaListe(scanner);
				} 
				while (ripetiVisualizzaCerca);
				break;
			case 2:
				boolean ripetiModificaElimina = true;
				do {
					System.out.println("\nMODIFICA E CANCELLAZIONE");
					System.out.println("[1] per eliminare (Lista/Categoria/Articolo)...");
					System.out.println("[2] per modificare un articolo o una categoria...");
					System.out.println("[0] per tornare indietro...");
					ripetiModificaElimina = modificaCancellazione(scanner);
				} 
				while (ripetiModificaElimina);
				break;
		}
		return true;
	}

	static boolean menuVisualizzaListe(Scanner scanner) throws NumeroSbagliatoException {
		int visCerca = scanner.nextInt();
		scanner.nextLine();
		
		if (visCerca < 0 || visCerca > 2) {
			throw new NumeroSbagliatoException("Opzione non valida.");
		}
		
		if (visCerca != 0 && GestioneListe.getListeArticoli().isEmpty()) {
			System.out.println("L'archivio delle liste è vuoto");
			return true;
		}
		
		switch (visCerca) {
			case 0:
				return false;
			case 1:
				stampaTutteLeListe();
				break;
			case 2:
				selezionaEdEsploraLista(scanner);
				break;
		}
		return true;
	}

	static void stampaTutteLeListe() {
		System.out.println("Liste disponibili:");
		for (ListaDiArticoli liste : GestioneListe.getListeArticoli().values()) {
			System.out.println("- " + liste.getListaNome());
			for (Articolo a : liste) {
				System.out.println("   -> Articolo: " + a.getNota() + " | Prezzo: " + a.getPrezzo() + "€");
			}
		}
	}

	static void selezionaEdEsploraLista(Scanner scanner) throws NumeroSbagliatoException {
		System.out.println("Liste disponibili:");
		ArrayList<ListaDiArticoli> elencoListe = new ArrayList<>(GestioneListe.getListeArticoli().values());
		
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

	static void eseguiOperazioniSuLista(Scanner scanner, ListaDiArticoli listaSelezionata) throws NumeroSbagliatoException {
		System.out.println("\nMENU OPZIONI LISTA: " + listaSelezionata.getListaNome().toUpperCase() + " ---");
		System.out.println("[1] Cerca articolo per prefisso");
		System.out.println("[2] Calcola prezzo totale");
		System.out.println("[3] Ripristina articolo eliminato");
		System.out.println("[4] Svuota cestino eliminati");
		System.out.println("[5] Aggiungi articolo alla lista");
		System.out.println("[0] Torna indietro");
		
		int operazione = scanner.nextInt();
		scanner.nextLine();
		
		switch (operazione) {
			case 1:
				cercaEStampaPerPrefisso(scanner, listaSelezionata);
				break;
			case 2:
				System.out.println("Prezzo totale degli articoli attivi: " + listaSelezionata.calcolaPrezzoTotale() + "€");
				break;
			case 3:
				ripristinaDaCestino(scanner, listaSelezionata);
				break;
			case 4:
				listaSelezionata.svuotaCancellati();
				System.out.println("Il cestino degli articoli eliminati è stato svuotato.");
				break;
			case 5:
				String nomeCategoria = chiediInfCategoria(scanner); 
				Categoria categoria = new Categoria(nomeCategoria);
				ArrayList<Object> listaInfArticoli = chiediInfArticolo(scanner);
				
				Articolo nuovoArticolo = new Articolo(
												categoria, 
												(double) listaInfArticoli.get(0), 
												(String) listaInfArticoli.get(1)
												);
				
				GestioneListe.inserisciCategoria(categoria);
				GestioneListe.inserisciArticolo(nuovoArticolo);
				listaSelezionata.aggiungiArticolo(nuovoArticolo);
				System.out.println("Articolo aggiunto con successo alla lista " + listaSelezionata.getListaNome() + "!");
				break;
			case 0:
				break;
			default:
				throw new NumeroSbagliatoException("Operazione non valida.");
		}
	}
	
	static void cercaEStampaPerPrefisso(Scanner scanner, ListaDiArticoli listaSelezionata) {
		System.out.println("Inserisci il prefisso della nota da cercare:");
		String prefisso = scanner.nextLine();
		Articolo trovato = listaSelezionata.cercaArticoloPerPrefisso(prefisso);
		
		if (trovato != null) {
			System.out.println("Articolo trovato: " + trovato.getNota() + " | Prezzo: " + trovato.getPrezzo() + "€");
		} 
		else {
			System.out.println("Nessun articolo trovato.");
		}
	}
	
	static void ripristinaDaCestino(Scanner scanner, ListaDiArticoli listaSelezionata) throws NumeroSbagliatoException {
		System.out.println("Inserisci la nota dell'articolo da ripristinare:");
		String nota = scanner.nextLine();
		Articolo daRipristinare = listaSelezionata.cercaNeiCancellati(nota);
		
		if (daRipristinare != null) {
			listaSelezionata.ripristinaArticolo(daRipristinare);
			System.out.println("Articolo ripristinato con successo.");
		} 
		else {
			throw new NumeroSbagliatoException("Articolo non presente nel cestino dei cancellati.");
		}
	}
	
	static boolean modificaCancellazione(Scanner scanner) throws NumeroSbagliatoException {
		int eliminaModifica = scanner.nextInt();
		scanner.nextLine();
		
		if (eliminaModifica < 0 || eliminaModifica > 2) {
			throw new NumeroSbagliatoException("Scelta non valida in Modifica/Cancellazione.");
		}
		
		switch (eliminaModifica) {
			case 0:
				return false; 
			case 1:
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
				break;
			case 2:
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
				break;
		}
		return true;
	}
	
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
	    
		switch (cosaModificare) {
			case 1:
				System.out.println("Inserisci il prezzo da modificare: ");
				double prezzo = chiediPrezzo(scanner);
				articoloTrovato.setPrezzo(prezzo);
				System.out.println("Prezzo modificato: " + articoloTrovato.getPrezzo());
				break;
			case 2:
				String nomeCat = chiediInfCategoria(scanner);
				Categoria categoriaCercata = GestioneListe.cercaCategoriaGlobale(nomeCat);
				articoloTrovato.setCategoria(categoriaCercata);
				System.out.println("Categoria modificata: " + articoloTrovato.getCategoria());
				break;
			case 3:
				System.out.println("Inserisci la nuova nota: ");
				String nota = scanner.nextLine();
				articoloTrovato.setNota(nota);
				System.out.println("Nota modificata: " + articoloTrovato.getNota());
				break;
		}
		return true;
	}
	
	static boolean cancellazione(Scanner scanner) throws NumeroSbagliatoException {
		int eliminaOggetto = scanner.nextInt();
		scanner.nextLine();
		
		if (eliminaOggetto < 0 || eliminaOggetto > 3) {
			throw new NumeroSbagliatoException("Numero di opzione cancellazione errato.");
		}
		
		switch (eliminaOggetto) {
			case 0:
				return false;
			case 1:
				System.out.println("Inserisci il nome della lista da eliminare...");
				String nomeListaEliminare = scanner.nextLine();	 
				if (GestioneListe.getListeArticoli().containsKey(nomeListaEliminare)) {
					GestioneListe.cancellaLista(nomeListaEliminare);
					System.out.println("Lista eliminata con successo.");
				} else {
					throw new NumeroSbagliatoException("Nome della lista cercata non trovato nell'archivio.");
				}
				break;
			case 2:
				System.out.println("Inserisci il nome della categoria da eliminare...");
				String nomeCat = scanner.nextLine();
				Categoria c = GestioneListe.cercaCategoriaGlobale(nomeCat);
				GestioneListe.cancellaCategoria(c);
				System.out.println("Categoria cancellata.");
				break;
			case 3:
				System.out.println("Inserisci la nota dell'articolo da eliminare...");
				String notaCercata = scanner.nextLine();
				Articolo a = GestioneListe.cercaArticoloGlobale(notaCercata);
				GestioneListe.cancellaArticolo(a);
				System.out.println("Articolo eliminato con successo.");
				break;
		}
		return true;
	}
	
	static String chiediInfCategoria(Scanner scanner) {
		System.out.println("Inserisci il nome della categoria: ");
		return scanner.nextLine();
	}
	
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
	
	static String chiediInfLista(Scanner scanner) {
		System.out.println("Inserisci il nome della lista...");
		return scanner.nextLine();
	}	
	
	static double chiediPrezzo(Scanner scanner) throws NumeroSbagliatoException {
		double prezzo = scanner.nextDouble();
		scanner.nextLine();
		if (prezzo < 0) {
			throw new NumeroSbagliatoException("Il prezzo inserito non può essere inferiore a zero.");
		}
		return prezzo;
	}
}