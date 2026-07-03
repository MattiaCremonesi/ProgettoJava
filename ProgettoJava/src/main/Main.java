package main;
import java.util.InputMismatchException;
import java.util.Scanner;

import model.*;

public class Main {

	public static void main (String[] args) {
		
		int num = 0;
		
		Scanner scanner = new Scanner (System.in);
		try {	
			System.out.println ("Premere [1] per usare interfaccia grafica...");
			System.out.println ("Premere [2] per usare terminale...");
			num = scanner.nextInt();
			if (num == 2) {
				System.out.println ("Avvio da terminale...");
			}
			else if (num == 1) {
				System.out.println ("Avvio da interfaccia grafica...");
			}
			else {
				throw new IllegalArgumentException ("Numero sbagliato");
			}
		
		
			if (num == 2) {
				int num2 = 0;
				
				System.out.println ("Premere [1] per creare una lista di articoli...");
				System.out.println ("Premere [2] per visualizzare o modificare una lista di articoli...");
				num2 = scanner.nextInt();
				String nome = "";
				if (num2 == 1) { nome = CreaLista (); } 
				Categoria categoria = new Categoria (nome);
			}		
		} catch (InputMismatchException e) {
			System.out.printf ("%s", e);
		} catch (Exception e) {
			System.out.printf ("%s", e);
		} finally {
			scanner.close();
		}		
	}
	
	static public String CreaLista () {
		Scanner scanner = new Scanner (System.in);
		String nome = "";
		try {			
			System.out.println ("Inserisci il nome della lista: ");
			nome = scanner.next();
		} catch (InputMismatchException e) {
			System.out.println ("nome non valido");
		} catch (Exception e) {
			System.out.printf ("%s", e);
		} finally {
			scanner.close();
		}
		return nome;
	}
	
}
