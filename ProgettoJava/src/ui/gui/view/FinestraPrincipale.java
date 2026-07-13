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

    public FinestraPrincipale() {
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

        pannelloBottoni.add(btnApri);
        pannelloBottoni.add(btnCrea);
        pannelloBottoni.add(btnElimina);
        add(pannelloBottoni, BorderLayout.EAST);

        aggiornaElencoListe();
    }

    public void aggiornaElencoListe() {
        listaDatiModel.clear();
        // Controllo di sicurezza: se GestioneListe non è inizializzata, non facciamo nulla
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


}