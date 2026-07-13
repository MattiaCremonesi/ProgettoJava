package ui.gui.view;

import javax.swing.*;
import java.awt.*;
import model.ListaDiArticoli;
import model.Articolo;

/**
 * Pannello di dettaglio di una specifica lista.
 * Layout basato sullo schema a singolo elenco centrale e bottoni orizzontali in basso.
 * * @author Davide Aime e Mattia Cremonesi
 */
@SuppressWarnings("serial")
public class FinestraDettaglioLista extends JPanel {

	private ListaDiArticoli listaModello;

	private JList<String> listaArticoliGrafica;
	private DefaultListModel<String> modelArticoli;

	private JButton btnCerca;
	private JButton btnCalcolaTotale;
	private JButton btnCestina;
	private JButton btnRipristina;
	private JButton btnSvuotaCestino;
	private JButton btnAggiungi;

	/**
	 * Costruttore: riceve il modello della lista selezionata.
	 */
	public FinestraDettaglioLista(ListaDiArticoli listaModello) {
		this.listaModello = listaModello;
		
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		modelArticoli = new DefaultListModel<>();
		listaArticoliGrafica = new JList<>(modelArticoli);
		listaArticoliGrafica.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaArticoliGrafica.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		
		JScrollPane scrollPane = new JScrollPane(listaArticoliGrafica);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Articoli nella lista selezionata: " + listaModello.getListaNome().toUpperCase()));
		add(scrollPane, BorderLayout.CENTER);

		JPanel pannelloBottoni = new JPanel(new GridLayout(1, 6, 8, 0));

		btnCerca = new JButton("Cerca");
		btnCalcolaTotale = new JButton("Totale €");
		btnCestina = new JButton("Cestina");
		btnRipristina = new JButton("Ripristina");
		btnSvuotaCestino = new JButton("Svuota Cestino");
		btnAggiungi = new JButton("Aggiungi");

		pannelloBottoni.add(btnCerca);
		pannelloBottoni.add(btnCalcolaTotale);
		pannelloBottoni.add(btnCestina);
		pannelloBottoni.add(btnRipristina);
		pannelloBottoni.add(btnSvuotaCestino);
		pannelloBottoni.add(btnAggiungi);

		add(pannelloBottoni, BorderLayout.SOUTH);

		aggiornaElencoArticoli();
	}

	/**
	 * Ricarica gli articoli prendendoli dal modello e li inserisce nella JList distinguendo lo stato.
	 */
	public void aggiornaElencoArticoli() {
		modelArticoli.clear();

		for (Articolo a : listaModello) {
			String stato = "[ATTIVO]  ";
			if (listaModello.getArticoliCancellati().contains(a)) {
				stato = "[CESTINO] ";
			}
			
			String riga = String.format("%s %-20s | Categoria: %-15s | Prezzo: %.2f€", 
					stato, a.getNota(), a.getCategoria().getNome(), a.getPrezzo());
			
			modelArticoli.addElement(riga);
		}
	}

	/**
	 * Restituisce l'indice dell'articolo selezionato nella JList.
	 */
	public int getIndiceArticoloSelezionato() {
		return listaArticoliGrafica.getSelectedIndex();
	}
	
	/**
	 * Consente di recuperare il modello dati della lista associata per le operazioni di business.
	 */
	public ListaDiArticoli getListaModello() {
		return listaModello;
	}

	// Metodi Getter per i bottoni, fondamentali per il lavoro del tuo compagno (Controller)
	public JButton getBtnCerca() { return btnCerca; }
	public JButton getBtnCalcolaTotale() { return btnCalcolaTotale; }
	public JButton getBtnCestina() { return btnCestina; }
	public JButton getBtnRipristina() { return btnRipristina; }
	public JButton getBtnSvuotaCestino() { return btnSvuotaCestino; }
	public JButton getBtnAggiungi() { return btnAggiungi; }
	
	/**
	 * Mostra un pop-up che chiede all'utente di inserire il testo da cercare.
	 * @return la stringa inserita dall'utente.
	 */
	public String chiediStringaRicerca() {
	    return JOptionPane.showInputDialog(this, "Inserisci il prefisso o la nota da cercare:", "Cerca Articolo", JOptionPane.QUESTION_MESSAGE);
	}
}