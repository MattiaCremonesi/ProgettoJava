package main;

import java.util.InputMismatchException;
import java.util.Scanner;
import view.InterfacciaTestuale;
import model.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Premere [1] per usare interfaccia grafica...");
        System.out.println("Premere [2] per usare terminale...");
        
        try {
            int scelta = scanner.nextInt();
            
            if (scelta == 2) {
                System.out.println("Avvio da terminale...");
                InterfacciaTestuale cli = new InterfacciaTestuale();
                cli.avvia(scanner);
            } 
            else if (scelta == 1) {
                System.out.println("Avvio da interfaccia grafica...");
            } 
            else {
                throw new NumeroSbagliatoException ("Input iniziale non valido.");
            }
            
        } 
        catch (InputMismatchException e) {
        	System.out.printf ("%s", e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Input iniziale non valido.");
        } 
        finally {
            scanner.close();
        }
    }
}