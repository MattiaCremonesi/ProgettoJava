package ui.gui.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Questa classe rappresenta il pannello grafico principale dell'applicazione.
 * Ha il compito di mostrare a video l'elenco di tutte le liste della spesa
 * attualmente create e di mettere a disposizione i pulsanti per poterle 
 * aprire, creare, modificare o eliminare.
 * 
 * @author Davide Aime e Mattia Cremonesi
 */
@SuppressWarnings("serial")
public class FinestraPrincipale extends JPanel {

    private JList<String>            listaGrafica;
    private DefaultListModel<String> listaDatiModel;
    
    private JButton                  btnApri;
    private JButton                  btnCrea;
    private JButton                  btnElimina;
    private JButton                  btnModifica;

    /**
     * Costruisce il pannello principale, preparando e posizionando i vari 
     * elementi grafici (la lista al centro e i bottoni in basso) all'interno 
     * del layout generale.
     */
    public FinestraPrincipale() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inizializzaAreaLista();
        inizializzaAreaBottoni();
    }

    /**
     * Metodo di supporto privato che costruisce l'area centrale della finestra,
     * dedicata a mostrare visivamente i nomi delle liste della spesa all'interno
     * di un pannello scorrevole.
     */
    private void inizializzaAreaLista() {
        listaDatiModel = new DefaultListModel<>();
        listaGrafica   = new JList<>(listaDatiModel);
        
        listaGrafica.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(listaGrafica);
        JPanel      pannello   = new JPanel(new BorderLayout());
        
        pannello.setBorder(BorderFactory.createTitledBorder("Liste Spesa"));
        pannello.add(scrollPane, BorderLayout.CENTER);
        
        add(pannello, BorderLayout.CENTER);
    }

    /**
     * Metodo di supporto privato che predispone la pulsantiera nella parte
     * inferiore della finestra, in modo che l'utente possa interagire con
     * le liste selezionate.
     */
    private void inizializzaAreaBottoni() {
        JPanel pannelloBottoni = new JPanel(new GridLayout(1, 4, 10, 0));
        pannelloBottoni.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        btnApri     = new JButton("Apri");
        btnCrea     = new JButton("Nuova");
        btnModifica = new JButton("Modifica");
        btnElimina  = new JButton("Elimina");

        pannelloBottoni.add(btnApri);
        pannelloBottoni.add(btnCrea);
        pannelloBottoni.add(btnModifica); 
        pannelloBottoni.add(btnElimina);
        
        add(pannelloBottoni, BorderLayout.SOUTH);
    }

    /**
     * Svuota la lista attualmente visibile e la ripopola con i dati più recenti.
     * Viene solitamente richiamato dal controller ogni volta che una lista viene
     * creata, eliminata o rinominata nel modello.
     * 
     * @param nomiListe l'elenco testuale dei nomi delle liste da mostrare
     */
    public void aggiornaElenco(List<String> nomiListe) {
        listaDatiModel.clear();
        for (String nome : nomiListe) {
            listaDatiModel.addElement(nome);
        }
    }

    /**
     * Restituisce il nome della lista attualmente evidenziata dall'utente 
     * con il cursore del mouse.
     * 
     * @return la stringa selezionata, oppure null se la selezione è vuota
     */
    public String getNomeListaSelezionata() {
        return listaGrafica.getSelectedValue();
    }
    
    /* Getter dei pulsanti necessari al controller per agganciare i listener */
    
    public JButton getBtnApri()     { return btnApri;     }
    public JButton getBtnCrea()     { return btnCrea;     }
    public JButton getBtnModifica() { return btnModifica; }
    public JButton getBtnElimina()  { return btnElimina;  }

    /**
     * Fa apparire una piccola finestra di dialogo di tipo prompt che permette 
     * all'utente di digitare o modificare il nome di una lista.
     * 
     * @param titolo       il testo da mostrare nella barra del titolo del pop-up
     * @param nomeIniziale l'eventuale stringa di partenza da mostrare nel campo,
     *                     utile ad esempio quando si vuole rinominare una lista
     * @return la stringa digitata dall'utente (ripulita dagli spazi laterali), 
     *         oppure null se l'utente chiude la finestra o preme su Annulla
     */
    public String mostraFormLista(String titolo, String nomeIniziale) {
        JPanel     pannelloInput = new JPanel(new BorderLayout(5, 5));
        JTextField txtNome       = new JTextField(nomeIniziale);
        
        pannelloInput.add(new JLabel("Nome lista:"), BorderLayout.NORTH);
        pannelloInput.add(txtNome, BorderLayout.CENTER);

        int risultato = JOptionPane.showConfirmDialog(
            this, 
            pannelloInput, 
            titolo, 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );

        if (risultato == JOptionPane.OK_OPTION) {
            return txtNome.getText().trim();
        }
        
        return null;
    }
}