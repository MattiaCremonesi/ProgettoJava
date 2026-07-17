package ui.gui.view;

import javax.swing.*;
import java.awt.*;
import model.ListaDiArticoli;
import model.Articolo;

/**
 * Questa classe rappresenta il pannello grafico di dettaglio di una singola
 * lista della spesa. Si occupa esclusivamente di mostrare a video gli articoli 
 * (sia quelli attivi che quelli spostati nel cestino) e di predisporre i 
 * pulsanti necessari per interagire con essi.
 * 
 * @author Davide Aime e Mattia Cremonesi
 */
@SuppressWarnings("serial")
public class FinestraDettaglioLista extends JPanel {

    private ListaDiArticoli          listaModello;
    private JList<String>            listaArticoliGrafica;
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
     * Costruisce il pannello grafico associandolo al modello della lista 
     * passato come parametro. Inizializza i componenti grafici e li dispone 
     * sullo schermo.
     * 
     * @param listaModello il modello di dominio contenente i dati da mostrare
     */
    public FinestraDettaglioLista(ListaDiArticoli listaModello) {
        this.listaModello = listaModello;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        inizializzaAreaLista();
        inizializzaAreaBottoni();

        aggiornaElencoArticoli();
    }

    /**
     * Imposta l'area centrale della finestra, dedicata all'elenco scrollabile 
     * degli articoli.
     */
    private void inizializzaAreaLista() {
        modelArticoli        = new DefaultListModel<>();
        listaArticoliGrafica = new JList<>(modelArticoli);
        
        listaArticoliGrafica.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaArticoliGrafica.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(listaArticoliGrafica);
        String      titolo     = "Articoli: " + listaModello.getNome().toUpperCase();
        
        scrollPane.setBorder(BorderFactory.createTitledBorder(titolo));
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Prepara e posiziona la pulsantiera nella parte inferiore della finestra.
     */
    private void inizializzaAreaBottoni() {
        JPanel pannelloBottoni = new JPanel(new GridLayout(1, 8, 5, 0));

        btnIndietro      = new JButton("Indietro");
        btnCerca         = new JButton("Cerca");
        btnCalcolaTotale = new JButton("Totale €");
        btnAggiungi      = new JButton("Aggiungi");
        btnModifica      = new JButton("Modifica");
        btnCestina       = new JButton("Cestina");
        btnRipristina    = new JButton("Ripristina");
        btnSvuotaCestino = new JButton("Svuota Cestino");

        pannelloBottoni.add(btnIndietro);
        pannelloBottoni.add(btnCerca);
        pannelloBottoni.add(btnCalcolaTotale);
        pannelloBottoni.add(btnAggiungi);
        pannelloBottoni.add(btnModifica);
        pannelloBottoni.add(btnCestina);
        pannelloBottoni.add(btnRipristina);
        pannelloBottoni.add(btnSvuotaCestino);

        add(pannelloBottoni, BorderLayout.SOUTH);
    }

    /**
     * Legge i dati aggiornati dal modello e li inserisce nella lista visibile 
     * a schermo, formattandoli per distinguere visivamente gli articoli attivi 
     * da quelli che si trovano nel cestino.
     */
    public void aggiornaElencoArticoli() {
        modelArticoli.clear();

        for (Articolo a : listaModello) {
            String rigaFormattata = formattaRigaArticolo(a);
            modelArticoli.addElement(rigaFormattata);
        }
    }

    /**
     * Crea una stringa formattata e allineata per rappresentare i dati di 
     * un singolo articolo nell'elenco grafico.
     * 
     * @param a l'articolo da formattare
     * @return la stringa testuale pronta per essere inserita nella vista
     */
    private String formattaRigaArticolo(Articolo a) {
        String stato = listaModello.getArticoliCancellati().contains(a) 
                       ? "[CESTINO] " : "[ATTIVO]  ";
                       
        return String.format(
            "%s %-20s | Categoria: %-15s | Prezzo: %.2f€", 
            stato, 
            a.getNota(), 
            a.getCategoria().getNome(), 
            a.getPrezzo()
        );
    }

    /**
     * Restituisce l'indice dell'elemento che l'utente ha selezionato con il mouse
     * nell'elenco grafico.
     * 
     * @return l'indice numerico della selezione, o -1 se nulla è selezionato
     */
    public int getIndiceArticoloSelezionato() { 
        return listaArticoliGrafica.getSelectedIndex(); 
    }

    /**
     * Restituisce il modello della lista della spesa attualmente mostrato.
     * 
     * @return l'oggetto ListaDiArticoli di dominio
     */
    public ListaDiArticoli getListaModello() { 
        return listaModello; 
    }

    /* Getter dei pulsanti necessari al controller per agganciare i listener */
    
    public JButton getBtnCerca()         { return btnCerca; }
    public JButton getBtnCalcolaTotale() { return btnCalcolaTotale; }
    public JButton getBtnCestina()       { return btnCestina; }
    public JButton getBtnRipristina()    { return btnRipristina; }
    public JButton getBtnSvuotaCestino() { return btnSvuotaCestino; }
    public JButton getBtnAggiungi()      { return btnAggiungi; }
    public JButton getBtnIndietro()      { return btnIndietro; }
    public JButton getBtnModifica()      { return btnModifica; }
    
    /**
     * Mostra una piccola finestra di dialogo per permettere all'utente di 
     * digitare un termine di ricerca (un prefisso o una nota intera).
     * 
     * @return il testo digitato dall'utente, oppure null se preme annulla
     */
    public String chiediStringaRicerca() {
        return JOptionPane.showInputDialog(
            this, 
            "Inserisci il prefisso o la nota da cercare:", 
            "Cerca Articolo", 
            JOptionPane.QUESTION_MESSAGE
        );
    }
    
    /**
     * Mostra un modulo di inserimento per raccogliere o modificare i dati 
     * di un articolo (nota, categoria e prezzo).
     * 
     * @param titolo            il testo da mostrare in cima alla finestra
     * @param notaIniziale      il nome o la nota pre-compilata nel campo
     * @param categoriaIniziale la categoria pre-compilata nel campo
     * @param prezzoIniziale    il prezzo numerico pre-compilato nel campo
     * @return un array di stringhe contenente i dati inseriti, oppure null se 
     *         l'utente annulla l'operazione
     */
    public String[] mostraFormArticolo(String titolo, String notaIniziale, 
                                       String categoriaIniziale, String prezzoIniziale) {
    
        JTextField txtNota      = new JTextField(notaIniziale);
        JTextField txtCategoria = new JTextField(categoriaIniziale);
        JTextField txtPrezzo    = new JTextField(prezzoIniziale);

        JPanel pannelloInput = assemblaPannelloInput(txtNota, txtCategoria, txtPrezzo);

        int risultato = JOptionPane.showConfirmDialog(
            this, 
            pannelloInput, 
            titolo, 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );

        if (risultato == JOptionPane.OK_OPTION) {
            return new String[] {
                txtNota.getText().trim(),
                txtCategoria.getText().trim(),
                txtPrezzo.getText().trim().replace(",", ".")
            };
        }
        
        return null;
    }

    /**
     * Metodo di supporto che assembla fisicamente la griglia dei campi di testo 
     * necessari per il modulo di un articolo.
     * 
     * @param tNotav il campo di testo per la nota
     * @param tCat il campo di testo per la categoria
     * @param tPrezzo il campo di testo per il prezzo
     * @return il pannello pronto da inserire nella finestra di dialogo
     */
    private JPanel assemblaPannelloInput(JTextField tNota, JTextField tCat, JTextField tPrezzo) {
        JPanel pannello = new JPanel(new GridLayout(3, 2, 10, 10));
        
        pannello.add(new JLabel("Nota / Nome Articolo:"));
        pannello.add(tNota);
        pannello.add(new JLabel("Categoria:"));
        pannello.add(tCat);
        pannello.add(new JLabel("Prezzo (€):"));
        pannello.add(tPrezzo);
        
        return pannello;
    }
}