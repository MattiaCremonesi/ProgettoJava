package ui.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.Articolo;
import model.GestioneListe;
import model.ListaDiArticoli;
import ui.gui.view.FinestraDettaglioLista;
import ui.gui.view.FinestraPrincipale;

public class GuiController implements ActionListener {

	private FinestraPrincipale vistaPrincipale;
	private FinestraDettaglioLista vistaDettaglio;

	public GuiController(FinestraPrincipale vistaPrincipale) {
		this.vistaPrincipale = vistaPrincipale;
		
		this.vistaPrincipale.getBtnApri().addActionListener(this);
		this.vistaPrincipale.getBtnCrea().addActionListener(this);
		this.vistaPrincipale.getBtnElimina().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		System.out.println("Controllo");

		if (source.getText().equals("Apri Lista")) {
			System.out.println("Apri Lista");
			String nomeLista = vistaPrincipale.getNomeListaSelezionata();
			
			if (nomeLista != null) {
				ListaDiArticoli modelloLista = GestioneListe.getListeArticoli().get(nomeLista);
				
				vistaDettaglio = new FinestraDettaglioLista(modelloLista);
				
				vistaDettaglio.getBtnCerca().addActionListener(this);
				vistaDettaglio.getBtnCalcolaTotale().addActionListener(this);
				vistaDettaglio.getBtnCestina().addActionListener(this);
				vistaDettaglio.getBtnRipristina().addActionListener(this);
				vistaDettaglio.getBtnSvuotaCestino().addActionListener(this);
				vistaDettaglio.getBtnAggiungi().addActionListener(this);
				
				JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(vistaPrincipale);
				if (mainFrame != null) {
					mainFrame.setContentPane(vistaDettaglio);
					mainFrame.revalidate();
					mainFrame.repaint();
				}
			} 
			else {
				JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(vistaPrincipale);
				JOptionPane.showMessageDialog(mainFrame, "Seleziona una lista prima di aprirla.");
			}

		} 
		else if (source.getText().equals("Totale €")) {
			System.out.println("Totale €");
			if (vistaDettaglio != null) {
				double totale = vistaDettaglio.getListaModello().calcolaPrezzoTotale();
				JOptionPane.showMessageDialog(vistaDettaglio, "Il totale è: " + totale + "€");
			}

		} 
		else if (source.getText().equals("Cerca")) {
			System.out.println("Cerca");
			if (vistaDettaglio != null) {
				String termine = vistaDettaglio.chiediStringaRicerca();
				if (termine != null) {
					Articolo a = vistaDettaglio.getListaModello().cercaArticoloPerPrefisso(termine);
					JOptionPane.showMessageDialog(vistaDettaglio, "Nota: " +a.getNota() + ", Categoria: " + a.getCategoria() + ", Prezzo: " + a.getPrezzo() + "€");
				}
				else {
					JOptionPane.showMessageDialog(vistaDettaglio, "Articolo non trovato nella lista");
				}
			}

		} 
		else if (source.getText().equals("Svuota Cestino")) {
			System.out.println("Svuota Cestino");
			if (vistaDettaglio != null) {
				vistaDettaglio.getListaModello().svuotaCancellati();
			}
			
		} 
		else if (source.getText().equals("Nuova Lista")) {
			System.out.println("Nuova Lista");
			
		} 
		else if (source.getText().equals("Elimina Lista")) {
			System.out.println("Elimina Lista"); 
		}

		// Aggiornamento della vista corrente (proprio come il tuo view.updateView())
		if (vistaDettaglio != null) {
			vistaDettaglio.aggiornaElencoArticoli();
		} 
		else {
			vistaPrincipale.aggiornaElencoListe();
		}
	}
}