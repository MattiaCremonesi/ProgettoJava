package ui.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import model.Articolo;
import model.Categoria;
import model.ListaDiArticoli;
import ui.gui.view.FinestraDettaglioLista;

/**
 * Classe di test rapido per verificare l'estetica della nuova FinestraDettaglioLista.
 */
public class TestDettaglio {

	public static void main(String[] args) {
		
		// 1. Creiamo dei dati di prova (Modello)
		Categoria catCibo = new Categoria("Alimentari");
		Categoria catCasa = new Categoria("Pulizia");
		
		Articolo a1 = new Articolo(catCibo, 2.50, "Latte Intero");
		Articolo a2 = new Articolo(catCibo, 1.20, "Pane Carasau");
		Articolo a3 = new Articolo(catCasa, 4.80, "Sgrassatore");
		
		// Creiamo la lista con il primo articolo
		ListaDiArticoli listaProva = new ListaDiArticoli("Spesa Esselunga", a1);
		
		// Aggiungiamo gli altri articoli
		listaProva.aggiungiArticolo(a2);
		listaProva.aggiungiArticolo(a3);
		
		// Simuliamo il fatto che il "Pane" sia stato spostato nel cestino per vedere se compare [CESTINO]
		listaProva.cancellaArticolo(a2);

		// 2. Avviamo la grafica nel thread sicuro di Swing
		SwingUtilities.invokeLater(() -> {
			// Creiamo un guscio (JFrame) per contenere il tuo nuovo JPanel custom
			JFrame frameTest = new JFrame();
			frameTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frameTest.setBounds(100, 100, 750, 400); // Gli diamo una bella larghezza per i 6 bottoni
			frameTest.setTitle("Test Visivo Dettaglio Lista");

			// Istanziamo il tuo pannello passandogli la lista di prova
			FinestraDettaglioLista pannelloDettaglio = new FinestraDettaglioLista(listaProva);
			
			// Lo impostiamo come contenuto del frame
			frameTest.setContentPane(pannelloDettaglio);
			
			// Centriamo e rendiamo visibile
			frameTest.setLocationRelativeTo(null);
			frameTest.setVisible(true);
		});
	}
}