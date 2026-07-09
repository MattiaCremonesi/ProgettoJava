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
			System.out.println("[2] per visualizzare o modificare una lista di articoli...");
			System.out.println("[0] per uscire dal programma...");
			
			try {
				ripeti = MenuPrincipale (scanner);
			} 
			catch (NumeroSbagliatoException e) {
				System.out.println ("Inserisci o [1] o [2]: ");
			}
			catch (InputMismatchException e) {
				System.out.println("Errore: Tipo di input non valido. Inserisci un numero.");
				scanner.nextLine();
			} 
			catch (Exception e) {
				System.out.printf("Si è verificato un errore: %s\n", e.getMessage());
			}
		} while (ripeti);	
	}
	
	
	
	
	
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
			throw new NumeroSbagliatoException ("Numero sbagliato.");
		}
		return true;
	}
	
	
	
	
	
	static boolean gestioneListe (Scanner scanner) throws NumeroSbagliatoException {
		
		int VisualizzaModifica = scanner.nextInt();
		scanner.nextLine();
		
		if (VisualizzaModifica == 1) {						
			if (GestioneListe.listeArticoli.isEmpty()) {
				System.out.println("L'archivio delle liste è vuoto");
			} else {
				System.out.println("Liste disponibili:");
				for (ListaDiArticoli liste : GestioneListe.listeArticoli.values()) {
					System.out.println("- " + liste.getListaNome());
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
			throw new NumeroSbagliatoException("Input sbagliato.");
		}
		return true;
	}
	
	
	
	
	
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
			throw new NumeroSbagliatoException ("Numero sbagliato.");
		}
		return true;
	}
	
	
	
	
	
	
	static boolean modifica (Scanner scanner) throws NumeroSbagliatoException {
		int cosaModificare = scanner.nextInt();
		scanner.nextLine();
		if (cosaModificare == 0) {
			return false;
		}
		else if (cosaModificare < 0 || cosaModificare > 3) {
			throw new NumeroSbagliatoException ("Input non valido.");
		}
		System.out.println ("Inserisci la nota dell'articolo da modificare: ");
		String notaCercata = scanner.nextLine();
		
		Articolo articoloTrovato = null;
	    for (Articolo a : GestioneListe.articoli) {
	        if (a.getNota().equalsIgnoreCase(notaCercata)) {
	            articoloTrovato = a;
	            break;
	        }
	    }
	    
	    if (articoloTrovato == null) {
	    	System.out.println ("Articolo non trovato.");
	    	return true;
	    }
	    
	    if (cosaModificare == 1) {
	    	System.out.println ("Inserisci il prezzo da modificare: ");
	    	double prezzo = chiediPrezzo (scanner);
	    	articoloTrovato.setPrezzo(prezzo);
	    	System.out.println ("Prezzo modificato: " + articoloTrovato.getPrezzo());
	    }
	    else if (cosaModificare == 2) {
	    	String categoria = chiediInfCategoria (scanner);
	    	Categoria categoriaCercata = null;
		    for (Categoria c : GestioneListe.categorie) {
		        if (c.getNome().equalsIgnoreCase(categoria)) {
		            categoriaCercata = c;
		            break;
		        }
		    }
		    articoloTrovato.setCategoria(categoriaCercata);
		    System.out.println ("Categoria modificata: " + articoloTrovato.getCategoria());
		}
	    else if (cosaModificare == 3) {
	    	String nota = scanner.nextLine();
	    	articoloTrovato.setNota(nota);
	    	System.out.println ("Nota modificata: " + articoloTrovato.getNota());
	    }
		return true;
	}
	
	
	
	
	
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
				System.out.println("Nome della lista non trovato.");
			}
		}
		else if (eliminaOggetto == 2) {
			System.out.println("Inserisci il nome della categoria da eliminare...");
			String nomeCat = scanner.nextLine();
			boolean trovata = false;
			for (Categoria c : GestioneListe.categorie) {
				if (c.getNome().equalsIgnoreCase(nomeCat)) {
					GestioneListe.CancellaCategoria(c);
					System.out.println("Categoria cancellata.");
					trovata = true;
					break;
				}
			}
			if (!trovata) {
				System.out.println("Categoria non trovata.");
			}
		} 
		else if (eliminaOggetto == 3) {
			System.out.println("Inserisci la nota dell'articolo da eliminare...");
			String notaCercata = scanner.nextLine();
			boolean trovata = false;
			for (Articolo a : GestioneListe.articoli) {
				if (a.getNota().equalsIgnoreCase(notaCercata)) {
					GestioneListe.CancellaArticolo(a);
					System.out.println("Articolo eliminato con successo.");
					trovata = true;
					break;
				}
			}
			if (!trovata) {
				System.out.println("Articolo non trovato con questa nota.");
			}
		}
		else if (eliminaOggetto == 0) {
			return false;
		}
		else if (eliminaOggetto < 0 || eliminaOggetto > 3) {
			throw new NumeroSbagliatoException ("Numero sbagliato");
		}
		return true;
	}
	
	
	

	static String chiediInfCategoria(Scanner scanner) {
		
		String nome = "";
		System.out.println("Inserisci il nome della categoria: ");
		nome = scanner.nextLine();
		return nome;
	}
	
	
	
	
	
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
	
	
	
	
	
	static String chiediInfLista(Scanner scanner) {
		
		String nome = "";
		System.out.println("Inserisci il nome della lista...");
		nome = scanner.nextLine();
		return nome;
	}	
	
	
	

	
	static double chiediPrezzo (Scanner scanner) throws NumeroSbagliatoException {
		
		double prezzo = scanner.nextDouble();
		scanner.nextLine();
		if (prezzo < 0) {
			throw new NumeroSbagliatoException ("prezzo non valido.");
		}
		return prezzo;
	}
	
	
	
	
	
}











