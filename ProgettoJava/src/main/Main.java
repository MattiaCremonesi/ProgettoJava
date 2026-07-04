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
				ArrayList<Lista> archivioListe = new ArrayList<>();
				
				do {
					System.out.println ("Premere [1] per creare una lista di articoli...");
					System.out.println ("Premere [2] per visualizzare o modificare una lista di articoli...");
					CreaVisualizzaLista = scanner.nextInt();
					String nomeCategoria = "";
					String nomeLista = "";
					Lista lista;
					
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
						lista = new Lista (nomeLista, articolo);
						archivioListe.add(lista);
					}
					else if (CreaVisualizzaLista == 2) {
						for (Lista liste : archivioListe) {
							System.out.println (liste);
						}
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
	}
	
	static public String chiediInfCategoria (Scanner scanner) {
		String nome = "";
		try {			
			System.out.println ("Inserisci la categoria dell'articolo da inserire nella lista: ");
			nome = scanner.next();
		} catch (InputMismatchException e) {
			System.out.println ("nome non valido");
		} catch (Exception e) {
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
		} catch (InputMismatchException e) {
			System.out.printf ("%s", e);
		} catch (Exception e) {
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
	
	
	
	
	
	
	

