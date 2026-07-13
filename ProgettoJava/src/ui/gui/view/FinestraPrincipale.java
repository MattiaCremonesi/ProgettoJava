package ui.gui.view;

import javax.swing.*;
import java.awt.*;
import model.GestioneListe;

/**
 * Pannello visivo principale che elenca le liste create.
 * Modificato in JPanel per rispecchiare l'architettura spiegata a lezione.
 */

public class FinestraPrincipale extends JPanel {

	private JList<String> listaGraficaComponente;
	private DefaultListModel<String> listaDatiModel;
	
	private JButton btnApri;
	private JButton btnCrea;
	private JButton btnElimina;

	public FinestraPrincipale() {
		// Impostiamo il layout direttamente sul pannello
		setLayout(new BorderLayout(10, 10));

		// Creazione JList
		listaDatiModel = new DefaultListModel<>();
		listaGraficaComponente = new JList<>(listaDatiModel);
		listaGraficaComponente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listaGraficaComponente.setFont(new Font("Arial", Font.PLAIN, 14));
		
		JScrollPane scrollPane = new JScrollPane(listaGraficaComponente);
		
		JPanel pannelloCentro = new JPanel(new BorderLayout());
		pannelloCentro.setBorder(BorderFactory.createTitledBorder("Le tue Liste della Spesa"));
		pannelloCentro.add(scrollPane, BorderLayout.CENTER);
		
		add(pannelloCentro, BorderLayout.CENTER);

		// Creazione Pannello Bottoni
		JPanel pannelloBottoni = new JPanel(new GridLayout(3, 1, 5, 10));
		pannelloBottoni.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

		btnApri = new JButton("Apri Lista");
		btnCrea = new JButton("Nuova Lista");
		btnElimina = new JButton("Elimina Lista");

		btnApri.setFont(new Font("Arial", Font.BOLD, 12));
		btnCrea.setFont(new Font("Arial", Font.BOLD, 12));
		btnElimina.setFont(new Font("Arial", Font.BOLD, 12));

		pannelloBottoni.add(btnApri);
		pannelloBottoni.add(btnCrea);
		pannelloBottoni.add(btnElimina);

		add(pannelloBottoni, BorderLayout.EAST);

		aggiornaElencoListe();
	}

	public void aggiornaElencoListe() {
		listaDatiModel.clear();
		for (String nomeLista : GestioneListe.getListeArticoli().keySet()) {
			listaDatiModel.addElement(nomeLista);
		}
	}

	public String getNomeListaSelezionata() {
		return listaGraficaComponente.getSelectedValue();
	}

	public JButton getBtnApri() { return btnApri; }
	public JButton getBtnCrea() { return btnCrea; }
	public JButton getBtnElimina() { return btnElimina; }
}