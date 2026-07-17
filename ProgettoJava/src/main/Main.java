package main;

import java.util.InputMismatchException;
import java.util.Scanner;

import model.exception.NumeroSbagliatoException;
import ui.view.InterfacciaTestuale;
import ui.gui.ListeGui;

/**
 * Classe di avvio dell'applicazione.
 * Gestisce la scelta iniziale dell'utente per l'interfaccia 
 * da utilizzare (grafica o testuale) e fa partire il programma.
 */
public class Main {

    /**
     * Metodo principale che avvia il programma, mostrando il menu 
     * di scelta iniziale e gestendo eventuali errori di inserimento.
     * Ripete l'operazione finché non viene fornito un input valido.
     * 
     * @param args argomenti passati da riga di comando (non utilizzati)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean avvioRiuscito = false;
        
        while (!avvioRiuscito) {
            System.out.println("Premere [1] per usare interfaccia grafica...");
            System.out.println("Premere [2] per usare terminale...");
            
            try {
                gestisciSceltaIniziale(scanner);
                avvioRiuscito = true; // Se arriviamo qui, non ci sono state eccezioni
            } catch (NumeroSbagliatoException e) {
                System.out.println("Numero sbagliato: " + e.getMessage() + "\nRiprova.\n");
            } catch (InputMismatchException e) {
                System.out.println("Errore: Devi inserire un numero intero, "
                                 + "non lettere o simboli.\nRiprova.\n");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Errore generico in avvio: " + e.getMessage() + "\nRiprova.\n");
                scanner.nextLine();
            }
        }
    }
    
    /**
     * Legge l'input dell'utente e istanzia l'interfaccia corrispondente.
     * 
     * @param scanner lo scanner utilizzato per leggere l'input da tastiera
     * @throws NumeroSbagliatoException se viene inserito un numero diverso da 1 o 2
     */
    private static void gestisciSceltaIniziale(Scanner scanner) 
            throws NumeroSbagliatoException {
        
        int scelta = scanner.nextInt();
        
        if (scelta == 1) {
            System.out.println("Avvio da interfaccia grafica...");
            new ListeGui();
        } else if (scelta == 2) {
            System.out.println("Avvio da terminale...");
            InterfacciaTestuale view = new InterfacciaTestuale();
            view.avvia(scanner);
        } else {
            throw new NumeroSbagliatoException("Input iniziale non valido. "
                                             + "Scegliere 1 oppure 2.");
        }
    }
}