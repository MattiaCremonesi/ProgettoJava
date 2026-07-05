package view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import model.*; 

public class InterfacciaTestuale {

	public void avvia(Scanner scanner) {
		int CreaVisualizzaLista = 0;
		boolean ripeti = true;
		
		do {
			System.out.println("\nMENU PRINCIPALE TERMINALE");
			System.out.println("[1] per creare una lista di articoli...");
			System.out.println("[2] per visualizzare o modificare una lista di articoli...");
			System.out.println("[0] per uscire dal programma...");
			
			try {
				CreaVisualizzaLista = scanner.nextInt();
				
				if (CreaVisualizzaLista == 0) {
					ripeti = false;
					System.out.println("Uscita dall'interfaccia testuale.");
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
					int VisualizzaModifica;
					boolean ripetiVisualizzaModifica = true;
					
					do {
						System.out.println("\nGESTIONE LISTE");
						System.out.println("[1] per visualizzare le tue liste...");
						System.out.println("[2] per modificare o eliminare...");
						System.out.println("[0] per tornare indietro...");
						VisualizzaModifica = scanner.nextInt();
						
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
								int eliminaModifica = scanner.nextInt();
								
								if (eliminaModifica == 1) {
									System.out.println("\n[1] per eliminare una lista...");
									System.out.println("[2] per eliminare una categoria...");
									System.out.println("[3] per eliminare un articolo...");
									System.out.println("[0] per tornare indietro...");
									int eliminaOggetto = scanner.nextInt();
									
									if (eliminaOggetto == 1) {
										System.out.println("Inserisci il nome della lista da eliminare...");
										String nomeListaEliminare = scanner.next();	 
										if (GestioneListe.listeArticoli.containsKey(nomeListaEliminare)) {
											GestioneListe.CancellaLista(nomeListaEliminare);
											System.out.println("Lista eliminata con successo.");
										} else {
											System.out.println("Nome della lista non trovato.");
										}
									}
									else if (eliminaOggetto == 2) {
										System.out.println("Inserisci il nome della categoria da eliminare...");
										String nomeCat = scanner.next();
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
										String notaCercata = scanner.next();
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
								}
								else if (eliminaModifica == 2) {
									System.out.println("Funzionalità di modifica (da implementare)...");
								}
								else if (eliminaModifica == 0) {
									ripetiModificaElimina = false; 
								}
							} while (ripetiModificaElimina);
						}
						else if (VisualizzaModifica == 0) {
							ripetiVisualizzaModifica = false;
						}
						else {
							System.out.println("Input sbagliato.");
						}
					} while (ripetiVisualizzaModifica);
				}
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

	static public String chiediInfCategoria(Scanner scanner) {
		String nome = "";
		System.out.println("Inserisci la categoria dell'articolo da inserire nella lista: ");
		nome = scanner.next();
		return nome;
	}
	
	static public ArrayList<Object> chiediInfArticolo(Scanner scanner) {
		ArrayList<Object> lista = new ArrayList<>();
		System.out.println("Inserisci il prezzo dell'articolo...");
		double num = scanner.nextDouble();
		scanner.nextLine();
		if (num < 0) {
			throw new IllegalArgumentException("Numero negativo non valido.");
		}
		lista.add(num);
		
		System.out.println("Inserisci una nota per l'articolo (oppure premi 'n' per nessuna nota):");
		String nota = scanner.nextLine();
		if (nota.equalsIgnoreCase("n")) {
			lista.add("");
		} 
		else {
			lista.add(nota);
		}
		return lista;
	}
	
	static public String chiediInfLista(Scanner scanner) {
		String nome = "";
		System.out.println("Inserisci il nome della lista...");
		nome = scanner.next();
		return nome;
	}	
}