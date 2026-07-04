package main;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.*;

public class Main {

	public static void main (String[] args) {
		
		int interfacciaDaUsare = 0;
		
		Scanner scanner = new Scanner (System.in);
		try {	
			System.out.println ("Premere [1] per usare interfaccia grafica...");
			System.out.println ("Premere [2] per usare terminale...");
			interfacciaDaUsare = scanner.nextInt();
			if (interfacciaDaUsare == 2) {
				System.out.println ("Avvio da terminale...");
			}
			else if (interfacciaDaUsare == 1) {
				System.out.println ("Avvio da interfaccia grafica...");
			}
			else {
				throw new IllegalArgumentException ("Errore");
			}
			
			if (interfacciaDaUsare == 2) {
				int CreaVisualizzaLista = 0;
				boolean ripeti = true;
				
				do {
					System.out.println ("Premere [1] per creare una lista di articoli...");
					System.out.println ("Premere [2] per visualizzare o modificare una lista di articoli...");
					CreaVisualizzaLista = scanner.nextInt();
					String nomeCategoria = "";
					String nomeLista = "";
					ListaDiArticoli listaDiArticoli;
					
					if (CreaVisualizzaLista == 1) { 
						nomeCategoria = chiediInfCategoria (scanner); 
						Categoria categoria = new Categoria (nomeCategoria);
						ArrayList<Object> listaInfArticoli = chiediInfArticolo (scanner);
						Articolo articolo = new Articolo (
														categoria, 
														(double) listaInfArticoli.get(0), 
														(String) listaInfArticoli.get(1)
														);
						nomeLista = chiediInfLista (scanner);
						GestioneListe.InserisciCategoria(categoria);
					    GestioneListe.InserisciArticolo(articolo);
					    GestioneListe.CreaLista(nomeLista, articolo);
					    System.out.println ("Lista creata con successo");
					}
					else if (CreaVisualizzaLista == 2) {
						int VisualizzaModifica;
						int ripetiVisualizzaModifica = 1;
						do {
							System.out.println ("Premi [1] per visualizzare le tue liste...");
							System.out.println ("Premi [2] per modificare le tue liste...");
							System.out.println ("Premi [0] per tornare indietro...");
							VisualizzaModifica = scanner.nextInt();
							if (VisualizzaModifica == 1) {						
								if (GestioneListe.listeArticoli.isEmpty()) {
									System.out.println ("L'archivio delle liste è vuoto");
								}
								for (ListaDiArticoli liste : GestioneListe.listeArticoli.values()) {
									System.out.println (liste.getListaNome());
								}	
							}
							else if (VisualizzaModifica == 2) {
								boolean ripetiModificaElimina = true;
								do {
									System.out.println ("Premi [1] per eliminare una lista...");
									System.out.println ("Premi [2] per modificare un articolo o una categoria...");
									System.out.println ("Premi [0] per tornare indietro...");
									int eliminaModifica = scanner.nextInt();
									if (eliminaModifica == 1) {
										int eliminaOggetto;
										System.out.println ("Premi [1] per eliminare una lista...");
										System.out.println ("Premi [2] per eliminare una categoria...");
										System.out.println ("Premi [3] per eliminare un articolo...");
										System.out.println ("Premi [0] per tornare indietro...");
										eliminaOggetto = scanner.nextInt();
										if (eliminaOggetto == 1) {
											try {
												String nomeListaEliminare;
												System.out.println ("Inserisci il nome della lista da eliminare...");
												nomeListaEliminare = scanner.next();	 
												if (GestioneListe.listeArticoli.containsKey(nomeListaEliminare)) {
													GestioneListe.CancellaLista(nomeListaEliminare);
												}
												else {
													System.out.println ("Nome della lista non trovato");
												}
											}
											catch (InputMismatchException e) {
												System.out.printf("%s", e);
											}
											catch (Exception e) {
												System.out.printf ("%s", e);
											}
										}
										else if (eliminaOggetto == 2) {
											try {
												String nome;
												System.out.println ("Inserisci il nome della categoria da eliminare...");
												nome = scanner.next();
												boolean trovata = false;
												for (Categoria c : GestioneListe.categorie) {
													if (c.getNome().equalsIgnoreCase(nome)) {
														GestioneListe.CancellaCategoria(c);
														System.out.println ("Categoria cancellata");
														trovata = true;
														break;
													}
												}
												if (trovata == false) {
													System.out.println ("Categoria non trovata");
												}
											}
											catch (InputMismatchException e) {
												System.out.printf ("%s", e);
											}
											catch (Exception e) {
												System.out.printf ("%s", e);
											}
										}
									}
								}
								while (ripetiModificaElimina);
							}
							else if (VisualizzaModifica == 0) {
								ripetiVisualizzaModifica = 0;
							}
							else {
								System.out.println ("input sbagliato");
							}
						} 
						while (ripetiVisualizzaModifica == 1);
					}
					
				} 
				while (ripeti);	
			}		
		} 
		catch (InputMismatchException e) {
			System.out.printf ("%s", e);
		} 
		catch (Exception e) {
			System.out.printf ("%s", e);
		} 
		finally {
			scanner.close();
		}
	}
	
	static public String chiediInfCategoria (Scanner scanner) {
		String nome = "";
		try {			
			System.out.println ("Inserisci la categoria dell'articolo da inserire nella lista: ");
			nome = scanner.next();
		} 
		catch (InputMismatchException e) {
			System.out.println ("nome non valido");
		} 
		catch (Exception e) {
			System.out.printf ("%s", e);
		}
		return nome;
	}
	
	
	
	static public ArrayList<Object> chiediInfArticolo (Scanner scanner) {
		ArrayList<Object> lista = new ArrayList<>();
		try {
			System.out.println ("Inserisci il prezzo dell'articolo...");
			double num = scanner.nextDouble();
			scanner.nextLine();
			if (num < 0) {
				throw new IllegalArgumentException ("numero negativo");
			}
			lista.add (num);
			System.out.println ("Inserisci una nota per l'articolo, se no premi n");
			String nota = scanner.nextLine();
			if (nota.equals("n")) {
				lista.add("");
			}
			else {
				lista.add(nota);
			}
		} 
		catch (InputMismatchException e) {
			System.out.printf ("%s", e);
		} 
		catch (Exception e) {
			System.out.printf ("s", e);
		}
		return lista;
	}
	
	
	
	static String chiediInfLista (Scanner scanner) {
		String nome = "";
		try {
			System.out.println ("Inserisci il nome della lista...");
			nome = scanner.next();
		} 
		catch (InputMismatchException e) {
			System.out.printf ("%s", e);
		} 
		catch (Exception e) {
			System.out.printf ("%s", e);
		}
		return nome;
	}	
}
	
	
	
	
	
	
	

