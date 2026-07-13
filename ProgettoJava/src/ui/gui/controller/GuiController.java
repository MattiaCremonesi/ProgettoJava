package ui.gui.controller;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.GestioneListe;
import model.ListaDiArticoli;
import model.Articolo;
import model.Categoria;
import ui.gui.view.FinestraPrincipale;
import ui.gui.view.FinestraDettaglioLista;

/**
 * Gestore centralizzato degli eventi per l'interfaccia grafica principale.
 * Collega i componenti della View (FinestraPrincipale) con il Modello (GestioneListe).
 * * @author IlTuoNome E IlNomeDelCompagno
 */
public class GuiController {

	private FinestraPrincipale view;

	/**
	 * Costruttore del Controller. Associa la vista e aggancia gli ascoltatori ai bottoni.
	 * * @param view il pannello della finestra principale
	 */
	public GuiController(FinestraPrincipale view) {
		this.view = view;
		assegnaAscoltatori();
	}

	/**
	 * Associa i listener (ActionListener) ai rispettivi bottoni della View.
	 */
	private void assegnaAscoltatori() {
		
		// 1. GESTIONE BOTTONE "NUOVA LISTA"
		view.getBtnCrea().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gestisciCreazioneLista();
			}
		});

		// 2. GESTIONE BOTTONE "ELIMINA LISTA"
		view.getBtnElimina().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gestisciCancellazioneLista();
			}
		});

		// 3. GESTIONE BOTTONE "APRI LISTA"
		view.getBtnApri().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gestisciAperturaLista();
			}
		});
	}

	/**
	 * Mostra un pannello di input pop-up per creare una nuova lista 
	 * comprensiva del suo primo articolo obbligatorio.
	 */
	private void gestisciCreazioneLista() {
		// Creiamo un pannello ad hoc da mostrare dentro la finestrella pop-up
		JPanel pannelloInput = new JPanel(new GridLayout(4, 2, 5, 5));
		
		JTextField txtNomeLista = new JTextField();
		JTextField txtCategoria = new JTextField();
		JTextField txtNotaArticolo = new JTextField();
		JTextField txtPrezzoArticolo = new JTextField();

		pannelloInput.add(new JLabel("Nome Nuova Lista:"));
		pannelloInput.add(txtNomeLista);
		pannelloInput.add(new JLabel("Categoria Primo Articolo:"));
		pannelloInput.add(txtCategoria);
		pannelloInput.add(new JLabel("Nota/Nome Articolo:"));
		pannelloInput.add(txtNotaArticolo);
		pannelloInput.add(new JLabel("Prezzo Articolo (€):"));
		pannelloInput.add(txtPrezzoArticolo);

		int risultato = JOptionPane.showConfirmDialog(view, pannelloInput, 
				"Creazione Nuova Lista", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		// Se l'utente clicca su OK, validiamo ed eseguiamo la logica
		if (risultato == JOptionPane.OK_OPTION) {
			String nomeLista = txtNomeLista.getText().trim();
			String nomeCategoria = txtCategoria.getText().trim();
			String notaArticolo = txtNotaArticolo.getText().trim();
			String prezzoStr = txtPrezzoArticolo.getText().trim().replace(",", ".");

			// Controlli preventivi di validità testuale
			if (nomeLista.isEmpty() || nomeCategoria.isEmpty() || notaArticolo.isEmpty() || prezzoStr.isEmpty()) {
				JOptionPane.showMessageDialog(view, "Tutti i campi sono obbligatori per creare la lista!", "Errore", JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				double prezzo = Double.parseDouble(prezzoStr);
				if (prezzo < 0) {
					JOptionPane.showMessageDialog(view, "Il prezzo non può essere inferiore a zero.", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Creiamo gli oggetti del Modello
				Categoria nuovaCategoria = new Categoria(nomeCategoria);
				Articolo primoArticolo = new Articolo(nuovaCategoria, prezzo, notaArticolo);

				// Registriamo nell'archivio globale centralizzato
				GestioneListe.inserisciCategoria(nuovaCategoria);
				GestioneListe.inserisciArticolo(primoArticolo);
				GestioneListe.creaLista(nomeLista, primoArticolo);

				// Aggiorniamo la JList visiva
				view.aggiornaElencoListe();
				JOptionPane.showMessageDialog(view, "Lista '" + nomeLista + "' creata correttamente!", "Successo", JOptionPane.INFORMATION_MESSAGE);

			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(view, "Formato prezzo non valido! Inserisci un numero (es. 2.50).", "Errore", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Rileva la lista selezionata dall'utente e la cancella permanentemente dall'applicazione.
	 */
	private void gestisciCancellazioneLista() {
		String listaSelezionata = view.getNomeListaSelezionata();
		
		if (listaSelezionata == null) {
			JOptionPane.showMessageDialog(view, "Seleziona prima una lista dall'elenco!", "Attenzione", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int conferma = JOptionPane.showConfirmDialog(view, 
				"Sei sicuro di voler eliminare definitivamente la lista '" + listaSelezionata + "'?", 
				"Conferma Cancellazione", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (conferma == JOptionPane.YES_OPTION) {
			GestioneListe.cancellaLista(listaSelezionata);
			view.aggiornaElencoListe(); // Aggiorna l'elenco visivo svuotando la riga eliminata
		}
	}

	/**
	 * Recupera l'oggetto ListaDiArticoli selezionato e apre la finestra di dettaglio specifica.
	 */
	private void gestisciAperturaLista() {
		String nomeListaSelezionata = view.getNomeListaSelezionata();
		
		if (nomeListaSelezionata == null) {
			JOptionPane.showMessageDialog(view, "Seleziona prima una lista da aprire!", "Attenzione", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Recuperiamo l'oggetto reale ListaDiArticoli dalla mappa globale del Modello
		ListaDiArticoli modelloLista = GestioneListe.getListeArticoli().get(nomeListaSelezionata);

		if (modelloLista != null) {
			// Avviamo la seconda finestra (FinestraDettaglioLista) passandogli la lista corrente
			SwingUtilities.invokeLater(() -> {
				new FinestraDettaglioLista(modelloLista);
			});
		}
	}
}