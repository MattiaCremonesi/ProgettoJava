package ui.gui.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.ListaDiArticoli;
import model.Articolo;
import model.Categoria;
import model.GestioneListe;

/**
 * Finestra di dettaglio per una specifica lista della spesa.
 * Mostra gli articoli attivi e i cancellati in due schede separate (JTabbedPane)
 * usando tabelle grafiche (JTable).
 * * @author IlTuoNome E IlNomeDelCompagno
 */
@SuppressWarnings("serial")
public class FinestraDettaglioLista extends JFrame {

	private ListaDiArticoli listaModello;

	// Componenti per la scheda degli Articoli Attivi
	private JTable tabellaAttivi;
	private DefaultTableModel modelAttivi;
	private JButton btnAggiungi;
	private JButton btnCestina;

	// Componenti per la scheda del Cestino
	private JTable tabellaCancellati;
	private DefaultTableModel modelCancellati;
	private JButton btnRipristina;
	private JButton btnSvuotaCestino;

	// Etichetta globale per il prezzo totale
	private JLabel lblPrezzoTotale;

	/**
	 * Costruttore: riceve il modello della lista specifica da visualizzare.
	 * * @param listaModello la lista di articoli su cui operare
	 */
	public FinestraDettaglioLista(ListaDiArticoli listaModello) {
		this.listaModello = listaModello;

		// Impostazioni base del Frame secondario
		setTitle("Dettaglio Lista: " + listaModello.getListaNome().toUpperCase());
		setSize(600, 450);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chiude solo questa finestra, non tutto il programma
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(10, 10));

		// 1. Pannello Superiore (Titolo)
		JPanel panelNord = new JPanel();
		JLabel lblTitolo = new JLabel("Contenuto di: " + listaModello.getListaNome());
		lblTitolo.setFont(new Font("Arial", Font.BOLD, 16));
		panelNord.add(lblTitolo);
		add(panelNord, BorderLayout.NORTH);

		// 2. Creazione della struttura a schede (JTabbedPane)
		JTabbedPane tabbedPane = new JTabbedPane();

		// Inizializzazione delle due schede
		JPanel schedaAttivi = creaSchedaAttivi();
		JPanel schedaCancellati = creaSchedaCancellati();

		tabbedPane.addTab("Articoli Attivi", schedaAttivi);
		tabbedPane.addTab("Cestino / Eliminati", schedaCancellati);
		add(tabbedPane, BorderLayout.CENTER);

		// 3. Pannello Inferiore (Prezzo Totale)
		JPanel panelSud = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelSud.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
		lblPrezzoTotale = new JLabel("Prezzo Totale: 0.00 €");
		lblPrezzoTotale.setFont(new Font("Arial", Font.BOLD, 14));
		panelSud.add(lblPrezzoTotale);
		add(panelSud, BorderLayout.SOUTH);

		// 4. Aggancia gli eventi ai bottoni ed esegui il primo caricamento dati
		agganciaEventiDettaglio();
		aggiornaTabelleESud();
		
		setVisible(true);
	}

	/**
	 * Costruisce la prima scheda contenente la tabella degli articoli attivi e i relativi bottoni.
	 */
	private JPanel creaSchedaAttivi() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		
		// Intestazioni colonne della tabella
		String[] colonne = {"Nota / Nome", "Categoria", "Prezzo (€)"};
		modelAttivi = new DefaultTableModel(colonne, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { return false; } // Celle non modificabili al doppio click
		};
		tabellaAttivi = new JTable(modelAttivi);
		tabellaAttivi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		panel.add(new JScrollPane(tabellaAttivi), BorderLayout.CENTER);

		// Pannello Bottoni Laterale
		JPanel panelBottoni = new JPanel(new GridLayout(2, 1, 5, 5));
		btnAggiungi = new JButton("Aggiungi");
		btnCestina = new JButton("Cestina");
		panelBottoni.add(btnAggiungi);
		panelBottoni.add(btnCestina);
		
		JPanel panelEst = new JPanel(new BorderLayout());
		panelEst.add(panelBottoni, BorderLayout.NORTH);
		panel.add(panelEst, BorderLayout.EAST);

		return panel;
	}

	/**
	 * Costruisce la seconda scheda contenente la tabella del cestino e i relativi bottoni.
	 */
	private JPanel creaSchedaCancellati() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		
		String[] colonne = {"Nota / Nome", "Categoria", "Prezzo (€)"};
		modelCancellati = new DefaultTableModel(colonne, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};
		tabellaCancellati = new JTable(modelCancellati);
		tabellaCancellati.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		panel.add(new JScrollPane(tabellaCancellati), BorderLayout.CENTER);

		// Pannello Bottoni Laterale
		JPanel panelBottoni = new JPanel(new GridLayout(2, 1, 5, 5));
		btnRipristina = new JButton("Ripristina");
		btnSvuotaCestino = new JButton("Svuota Cestino");
		panelBottoni.add(btnRipristina);
		panelBottoni.add(btnSvuotaCestino);
		
		JPanel panelEst = new JPanel(new BorderLayout());
		panelEst.add(panelBottoni, BorderLayout.NORTH);
		panel.add(panelEst, BorderLayout.EAST);

		return panel;
	}

	/**
	 * Ricarica i dati dal Modello e ridisegna le righe delle tabelle, aggiornando anche il totale.
	 */
	public void aggiornaTabelleESud() {
		// Svuota i vecchi dati visivi
		modelAttivi.setRowCount(0);
		modelCancellati.setRowCount(0);

		// Usiamo il nostro iteratore personalizzato! Raggruppa gli articoli nelle rispettive tabelle
		for (Articolo a : listaModello) {
			Object[] riga = { a.getNota(), a.getCategoria().getNome(), String.format("%.2f", a.getPrezzo()) };
			
			if (listaModello.getArticoliCancellati().contains(a)) {
				modelCancellati.addRow(riga);
			} else {
				modelAttivi.addRow(riga);
			}
		}

		// Aggiorna l'etichetta del Prezzo Totale richiamando la business logic del modello
		double totale = listaModello.calcolaPrezzoTotale();
		lblPrezzoTotale.setText(String.format("Prezzo Totale Articoli Attivi: %.2f €", totale));
	}

	/**
	 * Gestisce internamente le azioni elementari dei bottoni di questa sotto-finestra.
	 */
	private void agganciaEventiDettaglio() {
		
		// 1. BOTTONE AGGIUNGI ARTICOLO
		btnAggiungi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel popup = new JPanel(new GridLayout(3, 2, 5, 5));
				JTextField txtCategoria = new JTextField();
				JTextField txtNota = new JTextField();
				JTextField txtPrezzo = new JTextField();

				popup.add(new JLabel("Categoria:")); popup.add(txtCategoria);
				popup.add(new JLabel("Nota / Nome Articolo:")); popup.add(txtNota);
				popup.add(new JLabel("Prezzo (€):")); popup.add(txtPrezzo);

				int ris = JOptionPane.showConfirmDialog(FinestraDettaglioLista.this, popup, 
						"Aggiungi Articolo", JOptionPane.OK_CANCEL_OPTION);
				
				if (ris == JOptionPane.OK_OPTION) {
					String catNome = txtCategoria.getText().trim();
					String nota = txtNota.getText().trim();
					String prezzoStr = txtPrezzo.getText().trim().replace(",", ".");

					if (catNome.isEmpty() || nota.isEmpty() || prezzoStr.isEmpty()) {
						JOptionPane.showMessageDialog(FinestraDettaglioLista.this, "Tutti i campi sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE);
						return;
					}

					try {
						double prezzo = Double.parseDouble(prezzoStr);
						if (prezzo < 0) {
							JOptionPane.showMessageDialog(FinestraDettaglioLista.this, "Il prezzo non può essere negativo.", "Errore", JOptionPane.ERROR_MESSAGE);
							return;
						}

						Categoria c = new Categoria(catNome);
						Articolo nuovo = new Articolo(c, prezzo, nota);

						// Sincronizziamo modello globale e modello locale della lista
						GestioneListe.inserisciCategoria(c);
						GestioneListe.inserisciArticolo(nuovo);
						listaModello.aggiungiArticolo(nuovo);

						aggiornaTabelleESud();
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(FinestraDettaglioLista.this, "Formato prezzo non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		// 2. BOTTONE CESTINA ARTICOLO (SPOSTA NEL CESTINO)
		btnCestina.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int rigaSelezionata = tabellaAttivi.getSelectedRow();
				if (rigaSelezionata == -1) {
					JOptionPane.showMessageDialog(FinestraDettaglioLista.this, "Seleziona un articolo da cestinare!", "Attenzione", JOptionPane.WARNING_MESSAGE);
					return;
				}

				// Recuperiamo la nota (colonna 0) della riga evidenziata per cercare l'oggetto reale
				String notaArticolo = (String) tabellaAttivi.getValueWithConversion(rigaSelezionata, 0);
				// Usiamo il metodo di ricerca per prefisso/nota per estrarre l'articolo
				Articolo daCestinare = listaModello.cercaArticoloPerPrefisso(notaArticolo);

				if (daCestinare != null) {
					listaModello.cancellaArticolo(daCestinare); // Sposta nel cestino
					aggiornaTabelleESud();
				}
			}
		});

		// 3. BOTTONE RIPRISTINA ARTICOLO
		btnRipristina.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int rigaSelezionata = tabellaCancellati.getSelectedRow();
				if (rigaSelezionata == -1) {
					JOptionPane.showMessageDialog(FinestraDettaglioLista.this, "Seleziona un articolo da ripristinare dal cestino!", "Attenzione", JOptionPane.WARNING_MESSAGE);
					return;
				}

				String notaArticolo = (String) tabellaCancellati.getValueWithConversion(rigaSelezionata, 0);
				// Usiamo il metodo che ha fatto il tuo compagno per cercare nel cestino!
				Articolo daRipristinare = listaModello.cercaNeiCancellati(notaArticolo);

				if (daRipristinare != null) {
					listaModello.ripristinaArticolo(daRipristinare); // Ripristina
					aggiornaTabelleESud();
				}
			}
		});

		// 4. BOTTONE SVUOTA CESTINO
		btnSvuotaCestino.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (listaModello.getArticoliCancellati().isEmpty()) {
					JOptionPane.showMessageDialog(FinestraDettaglioLista.this, "Il cestino è già vuoto!", "Info", JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				int conferma = JOptionPane.showConfirmDialog(FinestraDettaglioLista.this, 
						"Vuoi svuotare permanentemente il cestino di questa lista?", "Conferma", JOptionPane.YES_NO_OPTION);
				
				if (conferma == JOptionPane.YES_OPTION) {
					listaModello.svuotaCancellati();
					aggiornaTabelleESud();
				}
			}
		});
	}

	/**
	 * Metodo helper per rimediare in modo sicuro al recupero dei valori dalle celle della JTable.
	 */
	private Object getValueWithConversion(JTable tabella, int riga, int colonna) {
		return tabella.getModel().getValueAt(riga, colonna);
	}
}