package main;

import java.util.InputMismatchException;
import java.util.Scanner;

import model.*;
import model.exception.NumeroSbagliatoException;
import ui.view.InterfacciaTestuale;
import ui.gui.view.*;
import ui.gui.ListeGui;


public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Premere [1] per usare interfaccia grafica...");
        System.out.println("Premere [2] per usare terminale...");
        
        try {
        	scelta (scanner);
        } 
        catch (NumeroSbagliatoException e) {
        	System.out.println("Numero sbagliato: " + e.getMessage());
        }
        catch (InputMismatchException e) {
        	System.out.println("Errore: Devi inserire un numero intero, non lettere o simboli.");
        }
        catch (Exception e) {
        	System.out.println("Errore di configurazione: " + e.getMessage());
        } 
        finally {
            scanner.close();
        }
    }
    
    static void scelta (Scanner scanner) throws NumeroSbagliatoException {
    	
    	int scelta = scanner.nextInt();
        
        if (scelta == 2) {
            System.out.println("Avvio da terminale...");
            InterfacciaTestuale view = new InterfacciaTestuale();
            view.avvia(scanner);
        } 
        else if (scelta == 1) {
            System.out.println("Avvio da interfaccia grafica...");
            new ListeGui();
		}
        else {
            throw new NumeroSbagliatoException ("Input iniziale non valido.");
        }
    }
    
}








