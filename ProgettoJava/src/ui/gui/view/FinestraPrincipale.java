package ui.gui.view;

import javax.swing.*;
import java.awt.*;
import model.GestioneListe;

/**
 * Pannello principale. Include un metodo main per avviare l'interfaccia 
 * in isolamento per test grafici.
 */
public class FinestraPrincipale extends JPanel {

    private JList<String> listaGraficaComponente;
    private DefaultListModel<String> listaDatiModel;
    
    private JButton btnApri;
    private JButton btnCrea;
    private JButton btnElimina;
    private JButton btnModifica;

    public FinestraPrincipale() {
        
    	setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Un po' di margine esterno

		// 1. AREA CENTRALE: JList per l'elenco delle liste
		listaDatiModel = new DefaultListModel<>();
		listaGraficaComponente = new JList<>(listaDatiModel);
		listaGraficaComponente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane(listaGraficaComponente);
		
		JPanel pannelloCentro = new JPanel(new BorderLayout());
		pannelloCentro.setBorder(BorderFactory.createTitledBorder("Le tue Liste della Spesa"));
		pannelloCentro.add(scrollPane, BorderLayout.CENTER);
		
		add(pannelloCentro, BorderLayout.CENTER);

		JPanel pannelloBottoni = new JPanel(new GridLayout(1, 4, 10, 0));
		pannelloBottoni.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnApri = new JButton("Apri Lista");
        btnCrea = new JButton("Nuova Lista");
        btnModifica = new JButton("Modifica Lista");
        btnElimina = new JButton("Elimina Lista");

        pannelloBottoni.add(btnApri);
        pannelloBottoni.add(btnCrea);
        pannelloBottoni.add(btnModifica);
        pannelloBottoni.add(btnElimina);
        add(pannelloBottoni, BorderLayout.SOUTH);

        aggiornaElencoListe();
    }

    public void aggiornaElencoListe() {
        listaDatiModel.clear();
        try {
            if (GestioneListe.getListeArticoli() != null) {
                for (String nomeLista : GestioneListe.getListeArticoli().keySet()) {
                    listaDatiModel.addElement(nomeLista);
                }
            }
        } catch (Exception e) {
            System.out.println("Nota: GestioneListe non ancora inizializzata.");
        }
    }

    public String getNomeListaSelezionata() {
        return listaGraficaComponente.getSelectedValue();
    }
    
    public JButton getBtnApri() 	{ return btnApri; }
    public JButton getBtnCrea() 	{ return btnCrea; }
    public JButton getBtnModifica() 	{ return btnModifica; }
    public JButton getBtnElimina() 	{ return btnElimina; }

    /**
	 * Mostra una finestra di dialogo (pop-up) comune per l'inserimento o la modifica del nome di una lista.
	 * * @param titolo        Il titolo della finestra (es. "Nuova Lista" o "Rinomina Lista")
	 * @param nomeIniziale  Il nome di partenza (vuoto se nuova lista, altrimenti il nome attuale)
	 * @return il nuovo nome inserito dall'utente se preme OK, altrimenti null se preme Annulla.
	 */
	public String mostraFormLista(String titolo, String nomeIniziale) {

		JPanel pannelloInput = new JPanel(new BorderLayout(5, 5));
		JTextField txtNomeLista = new JTextField(nomeIniziale);
		
		pannelloInput.add(new JLabel("Nome della lista:"), BorderLayout.NORTH);
		pannelloInput.add(txtNomeLista, BorderLayout.CENTER);

		int risultato = JOptionPane.showConfirmDialog(this, pannelloInput, 
				titolo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (risultato == JOptionPane.OK_OPTION) {
			return txtNomeLista.getText().trim();
		}
		
		return null;
	}
}