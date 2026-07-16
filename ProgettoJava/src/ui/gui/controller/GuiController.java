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
import ui.gui.ListeGui;
import ui.gui.view.FinestraDettaglioLista;
import ui.gui.view.FinestraPrincipale;

public class GuiController implements ActionListener {

	private ListeGui framePrincipale;
	private FinestraPrincipale vistaPrincipale;
	private FinestraDettaglioLista vistaDettaglio;

	public GuiController(ListeGui framePrincipale, FinestraPrincipale vistaPrincipale) {
        this.framePrincipale = framePrincipale;
        this.vistaPrincipale = vistaPrincipale;

        this.vistaPrincipale.getBtnApri().addActionListener(this);
        this.vistaPrincipale.getBtnCrea().addActionListener(this);
        this.vistaPrincipale.getBtnElimina().addActionListener(this);
        this.vistaPrincipale.getBtnModifica().addActionListener(this);
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
				vistaDettaglio.getBtnIndietro().addActionListener(this);
				
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
		
		else if (source.getText().equals("Indietro")) {
		    System.out.println("Ritorno alla schermata principale...");
		    
		    vistaPrincipale.aggiornaElencoListe(); 
		  
		    framePrincipale.setContentPane(vistaPrincipale);
		    
		    framePrincipale.revalidate();
		    framePrincipale.repaint();
		}
		
		else if (source.getText().equals("Totale €")) {
			System.out.println("Totale €");
			if (vistaDettaglio != null) {
				double totale = vistaDettaglio.getListaModello().calcolaPrezzoTotale();
				JOptionPane.showMessageDialog(vistaDettaglio, "Il totale è: " + totale + "€");
			}

		} 
		
		else if (source.getText().equals("Cestina")) {
			System.out.println("Cestina");
			int indiceArticolo = vistaDettaglio.getIndiceArticoloSelezionato();
			if (indiceArticolo != -1) {
				int contatore = 0;
				Articolo articoloDaCestinare = null;
				for (Articolo a : vistaDettaglio.getListaModello()) {
					if (contatore == indiceArticolo) {
						articoloDaCestinare = a;
						break;
					}
					contatore++;
				}
				GestioneListe.cancellaArticolo(articoloDaCestinare);
				JOptionPane.showMessageDialog(vistaDettaglio, "Articolo cestinato.");
				vistaDettaglio.aggiornaElencoArticoli();
			}
			else {
				JOptionPane.showMessageDialog(vistaDettaglio, "Articolo non selezionato.");
			}
		}
		
		else if (source.getText().equals("Ripristina")) {
			System.out.println ("Ripristina");
			int indiceArticolo = vistaDettaglio.getIndiceArticoloSelezionato();
			if (indiceArticolo != -1) {
				int contatore = 0;
				Articolo articoloDaRipristinare = null;
				for (Articolo a : vistaDettaglio.getListaModello()) {
					if (contatore == indiceArticolo) {
						articoloDaRipristinare = a;
						break;
					}
					contatore++;
				}
				vistaDettaglio.getListaModello().ripristinaArticolo(articoloDaRipristinare);
				JOptionPane.showMessageDialog(vistaDettaglio, "Articolo ripristinato.");
				vistaDettaglio.aggiornaElencoArticoli();
			}		
			else {
				JOptionPane.showMessageDialog(vistaDettaglio, "Articolo non selezionato.");
			}
		}
		
		else if (source.getText().equals("Cerca")) {
			System.out.println("Cerca");
			if (vistaDettaglio != null) {
				String termine = vistaDettaglio.chiediStringaRicerca();
				if (termine != null) {
					Articolo a = vistaDettaglio.getListaModello().cercaArticoloPerPrefisso(termine);
					if (a == null) {
						JOptionPane.showMessageDialog(vistaDettaglio, "Nessun articolo trovato.");
					}
					else {
						JOptionPane.showMessageDialog(vistaDettaglio, a.toString());
					}
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
		
		else if (source.getText().equals("Modifica Lista")) {
		    System.out.println("Modifica Lista");
		    String nomeAttuale = vistaPrincipale.getNomeListaSelezionata();
		    
		    if (nomeAttuale != null) {
		        String nuovoNome = vistaPrincipale.mostraFormLista("Rinomina Lista", nomeAttuale);
		        
		        if (nuovoNome != null && !nuovoNome.trim().isEmpty() && !nuovoNome.equals(nomeAttuale)) {
		            ListaDiArticoli lista = GestioneListe.getListeArticoli().get(nomeAttuale);
		            
		            if (lista != null) {
		                GestioneListe.getListeArticoli().remove(nomeAttuale);
		                GestioneListe.getListeArticoli().put(nuovoNome, lista);
		                vistaPrincipale.aggiornaElencoListe();
		                JOptionPane.showMessageDialog(framePrincipale, "Lista rinominata in: " + nuovoNome);
		            }
		        }
		    } 
		    else {
		        JOptionPane.showMessageDialog(framePrincipale, "Seleziona prima una lista da modificare.");
		    }
		}

		if (vistaDettaglio != null) {
			vistaDettaglio.aggiornaElencoArticoli();
		} 
		else {
			vistaPrincipale.aggiornaElencoListe();
		}
	}
}