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
	private JButton btnIndietro;
	private JButton btnModifica;

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
		btnModifica = new JButton("Modifica");
		btnIndietro = new JButton("Indietro");

		pannelloBottoni.add(btnIndietro);
		pannelloBottoni.add(btnCerca);
		pannelloBottoni.add(btnCalcolaTotale);
		pannelloBottoni.add(btnAggiungi);
		pannelloBottoni.add(btnModifica);
		pannelloBottoni.add(btnCestina);
		pannelloBottoni.add(btnRipristina);
		pannelloBottoni.add(btnSvuotaCestino);

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
	public JButton getBtnIndietro() { return btnIndietro; }
	public JButton getBtnModifica() { return btnModifica; }
	
	/**
	 * Mostra un pop-up che chiede all'utente di inserire il testo da cercare.
	 * @return la stringa inserita dall'utente.
	 */
	public String chiediStringaRicerca() {
	    return JOptionPane.showInputDialog(this, "Inserisci il prefisso o la nota da cercare:", "Cerca Articolo", JOptionPane.QUESTION_MESSAGE);
	}
	
	/**
	 * Mostra una finestra di dialogo (pop-up) condivisa sia per l'inserimento che per la modifica.
	 * * @param titolo             Il titolo della finestra (es. "Aggiungi Articolo" o "Modifica Articolo")
	 * @param notaIniziale       Valore di partenza per la nota (vuoto se nuovo articolo)
	 * @param categoriaIniziale  Valore di partenza per la categoria (vuoto se nuovo articolo)
	 * @param prezzoIniziale     Valore di partenza per il prezzo (vuoto se nuovo articolo)
	 * @String[]                 Un array contenente {nota, categoria, prezzo} se l'utente preme OK, altrimenti null.
	 */
	public String[] mostraFormArticolo(String titolo, String notaIniziale, String categoriaIniziale, String prezzoIniziale) {
	
		JPanel pannelloInput = new JPanel(new GridLayout(3, 2, 10, 10));
		
		JTextField txtNota = new JTextField(notaIniziale);
		JTextField txtCategoria = new JTextField(categoriaIniziale);
		JTextField txtPrezzo = new JTextField(prezzoIniziale);

		pannelloInput.add(new JLabel("Nota / Nome Articolo:"));
		pannelloInput.add(txtNota);
		pannelloInput.add(new JLabel("Categoria:"));
		pannelloInput.add(txtCategoria);
		pannelloInput.add(new JLabel("Prezzo (€):"));
		pannelloInput.add(txtPrezzo);

		int risultato = JOptionPane.showConfirmDialog(this, pannelloInput, 
				titolo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (risultato == JOptionPane.OK_OPTION) {
			return new String[] {
				txtNota.getText().trim(),
				txtCategoria.getText().trim(),
				txtPrezzo.getText().trim().replace(",", ".")
			};
		}
		
		return null;
	}
}