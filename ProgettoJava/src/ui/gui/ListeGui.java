package ui.gui;

import javax.swing.JFrame;

import ui.gui.controller.GuiController;
import ui.gui.view.FinestraPrincipale;

/**
 * Rappresenta la finestra principale e il punto di ingresso grafico 
 * per la gestione delle liste della spesa. 
 * Estende JFrame per configurare la cornice dell'applicazione e 
 * ospitare i pannelli interni della vista.
 * * @author Davide Aime e Mattia Cremonesi
 */
public class ListeGui extends JFrame {

    /**
     * Costruisce la finestra grafica configurandone i parametri di chiusura, 
     * le dimensioni, il titolo, l'ancoraggio centrale e la visibilità.
     * Associa inoltre il relativo pannello principale e il rispettivo controllore.
     */
    public ListeGui() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 950, 400);
        setTitle("Gestione Liste della Spesa - Interfaccia Grafica");

        FinestraPrincipale finestraPrincipale = new FinestraPrincipale();
        setContentPane(finestraPrincipale);

        new GuiController(this, finestraPrincipale);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
}