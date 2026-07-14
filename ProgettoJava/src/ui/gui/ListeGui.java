package ui.gui;

import javax.swing.JFrame;

import ui.gui.controller.GuiController;
import ui.gui.view.FinestraPrincipale;

/**
 * Classe principale di avvio dell'interfaccia grafica.
 * Eredita da JFrame e fa da contenitore principale per l'applicazione.
 * * @author Davide Aime e Mattia Cremonesi
 */

public class ListeGui extends JFrame {

	/**
	 * Costruttore: inizializza la finestra principale del programma.
	 */
	public ListeGui() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 950, 400);
		setTitle("Gestione Liste della Spesa - Interfaccia Grafica");

		FinestraPrincipale finestraPrincipale = new FinestraPrincipale();
		setContentPane(finestraPrincipale);

		new GuiController(finestraPrincipale);
		
		setLocationRelativeTo(null);
		
		// Rende visibile l'interfaccia				
		setVisible(true);
	}

}