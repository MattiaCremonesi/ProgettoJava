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
			System.out.println("\n========================================");
			System.out.println("          MENU PRINCIPALE               ");
			System.out.println("========================================");
			System.out.println("[1] Crea una nuova lista della spesa");
			System.out.println("[2] Gestisci l'archivio (Visualizza/Modifica/Elimina)");
			System.out.println("[0] Esci dal programma");
			System.out.print("Scegli un'opzione: ");
			
			try {
				ripeti = menuPrincipale(scanner);
			} 
			catch (NumeroSbagliatoException e) {
				System.out.println("\n[!] Errore: " + e.getMessage());
			}
			catch (InputMismatchException e) {
				System.out.println("\n[!] Errore: Tipo di input non valido. Inserisci un numero.");
				scanner.nextLine();
			} 
			catch (Exception e) {
				System.out.printf("\n[!] Si è verificato un errore imprevisto: %s\n", e.getMessage());
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
					System.out.println("\n----------------------------------------");
					System.out.println("       PANNELLO DI GESTIONE LISTE         ");
					System.out.println("----------------------------------------");
					System.out.println("[1] Visualizza le tue liste");
					System.out.println("[2] Modifica o Elimina elementi globali");
					System.out.println("[0] Torna al menu principale");
					System.out.print("Scegli un'opzione: ");
					ripetiVisualizzaModifica = gestioneListe(scanner);
				}
				while (ripetiVisualizzaModifica);
				break;
		}
		return true;
	}

	static void creaNuovaLista(Scanner scanner) throws NumeroSbagliatoException {
		System.out.println("\n--- CREAZIONE NUOVA LISTA ---");
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
		System.out.println("\n[V] Lista '" + nomeLista + "' creata con successo insieme al primo articolo!");
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
					System.out.println("\n----------------------------------------");
					System.out.println("          VISUALIZZAZIONE LISTE         ");
					System.out.println("----------------------------------------");
					System.out.println("[1] Mostra il riepilogo di TUTTE le liste");
					System.out.println("[2] Seleziona e opera su una lista specifica");
					System.out.println("[0] Torna indietro");
					System.out.print("Scegli un'opzione: ");
					ripetiVisualizzaCerca = menuVisualizzaListe(scanner);
				} 
				while (ripetiVisualizzaCerca);
				break;
			case 2:
				boolean ripetiModificaElimina = true;
				do {
					System.out.println("\n----------------------------------------");
					System.out.println("         MODIFICA E CANCELLAZIONE       ");
					System.out.println("----------------------------------------");
					System.out.println("[1] Elimina definitivamente (Lista/Categoria/Articolo)");
					System.out.println("[2] Modifica i dati di un articolo o una categoria");
					System.out.println("[0] Torna indietro");
					System.out.print("Scegli un'opzione: ");
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
			System.out.println("\n[!] L'archivio è vuoto. Non ci sono liste registrate.");
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
		System.out.println("\n========================================");
		System.out.println("       RIEPILOGO GENERALE ARCHIVIO      ");
		System.out.println("========================================");
		for (ListaDiArticoli lista : GestioneListe.getListeArticoli().values()) {
			System.out.println("\nLISTA: " + lista.getListaNome().toUpperCase());
			System.out.println("----------------------------------------");
			
			for (Articolo a : lista) {
				if (lista.getArticoliCancellati().contains(a)) {
					System.out.println("   [CESTINO] -> " + a.getNota() + " (" + a.getCategoria().getNome() + ") | " + a.getPrezzo() + "€");
				} else {
					System.out.println("   (Attivo)  -> " + a.getNota() + " (" + a.getCategoria().getNome() + ") | " + a.getPrezzo() + "€");
				}
			}
			System.out.println("----------------------------------------");
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
		System.out.println("\n========================================");
		System.out.println("  OPZIONI LISTA: " + listaSelezionata.getListaNome().toUpperCase());
		System.out.println("========================================");
		System.out.println("[1] Cerca articolo per prefisso (Attivo o Cestinato)");
		System.out.println("[2] Calcola il prezzo totale corrente");
		System.out.println("[3] Sposta un articolo nel CESTINO (Elimina)");
		System.out.println("[4] RIPRISTINA un articolo dal cestino");
		System.out.println("[5] SVUOTA permanentemente il cestino");
		System.out.println("[6] Aggiungi un nuovo articolo a questa lista");
		System.out.println("[0] Esci da questa lista");
		System.out.print("Scegli un'operazione: ");
		
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
				System.out.println("Inserisci la nota esatta dell'articolo da mettere nel cestino:");
				String notaDaCancellare = scanner.nextLine();
				Articolo daCancellare = listaSelezionata.cercaArticoloPerPrefisso(notaDaCancellare);
				
				if (daCancellare != null) {
					listaSelezionata.cancellaArticolo(daCancellare);
					System.out.println("Articolo '" + daCancellare.getNota() + "' spostato nel cestino.");
				} else {
					System.out.println("Articolo non trovato in questa lista.");
				}
				break;
			case 4:
				ripristinaDaCestino(scanner, listaSelezionata);
				break;
			case 5:
				listaSelezionata.svuotaCancellati();
				System.out.println("Il cestino degli articoli eliminati è stato svuotato.");
				break;
			case 6:
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
					System.out.println("\n----------------------------------------");
					System.out.println("          ELIMINAZIONE GLOBALE          ");
					System.out.println("----------------------------------------");
					System.out.println("[1] Elimina del tutto una LISTA");
					System.out.println("[2] Elimina del tutto una CATEGORIA");
					System.out.println("[3] Elimina del tutto un ARTICOLO");
					System.out.println("[0] Torna indietro");
					System.out.print("Scegli un'opzione: ");
					ripetiCancellazione = cancellazione(scanner);
				}
				while (ripetiCancellazione);
				break;
			case 2:
				boolean ripetiModifica = true; 
				do {
					System.out.println("\n----------------------------------------");
					System.out.println("          MODIFICA IN LINEA             ");
					System.out.println("----------------------------------------");
					System.out.println("[1] Modifica il PREZZO di un articolo");
					System.out.println("[2] Modifica la CATEGORIA di un articolo");
					System.out.println("[3] Modifica la NOTA di un articolo");
					System.out.println("[0] Torna indietro");
					System.out.print("Scegli un'opzione: ");
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
				System.out.println("Categoria modificata: " + articoloTrovato.getCategoria().getNome());
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
		String input = scanner.nextLine().replace(",", ".");
		try {
			double prezzo = Double.parseDouble(input);
			if (prezzo < 0) {
				throw new NumeroSbagliatoException("Il prezzo inserito non può essere inferiore a zero.");
			}
			return prezzo;
		} catch (NumberFormatException e) {
			throw new NumeroSbagliatoException("Formato prezzo non valido. Inserisci un numero (es. 2.50).");
		}
	}
}