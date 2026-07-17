package ui.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.Articolo;
import model.Categoria;
import model.GestioneListe;
import model.ListaDiArticoli;
import model.exception.ElementoNonTrovatoException;
import ui.gui.ListeGui;
import ui.gui.view.FinestraDettaglioLista;
import ui.gui.view.FinestraPrincipale;

/**
 * Gestisce l'interazione utente all'interno dell'interfaccia grafica.
 * Questa classe implementa il pattern di controllo (Controller) catturando
 * gli eventi generati dalle viste grafiche e traducendoli in modifiche 
 * strutturali sul modello di dominio.
 * 
 * @author Davide Aime e Mattia Cremonesi
 */
public class GuiController implements ActionListener {

    private ListeGui               framePrincipale;
    private FinestraPrincipale     vistaPrincipale;
    private FinestraDettaglioLista vistaDettaglio;

    /**
     * Costruisce un nuovo controllore associandovi le finestre dell'applicazione
     * e registrando i listener sui componenti grafici della schermata principale.
     * 
     * @param framePrincipale il contenitore grafico principale dell'applicazione
     * @param vistaPrincipale il pannello con l'elenco globale delle liste
     */
    public GuiController(ListeGui framePrincipale, 
                         FinestraPrincipale vistaPrincipale) {
        
        this.framePrincipale = framePrincipale;
        this.vistaPrincipale = vistaPrincipale;

        configuraPulsantiVistaPrincipale();
    }

    /**
     * Associa i listener e imposta gli identificativi dei comandi per 
     * i pulsanti della schermata principale.
     */
    private void configuraPulsantiVistaPrincipale() {
        vistaPrincipale.getBtnApri().addActionListener(this);
        vistaPrincipale.getBtnCrea().addActionListener(this);
        vistaPrincipale.getBtnElimina().addActionListener(this);
        vistaPrincipale.getBtnModifica().addActionListener(this);

        vistaPrincipale.getBtnApri().setActionCommand("Apri Lista");
        vistaPrincipale.getBtnCrea().setActionCommand("Nuova Lista");
        vistaPrincipale.getBtnElimina().setActionCommand("Elimina Lista");
        vistaPrincipale.getBtnModifica().setActionCommand("Modifica Lista");
    }

    /**
     * Associa i listener e imposta gli identificativi dei comandi per
     * i pulsanti della schermata di dettaglio di una lista.
     */
    private void configuraPulsantiVistaDettaglio() {
        vistaDettaglio.getBtnCerca().addActionListener(this);
        vistaDettaglio.getBtnCalcolaTotale().addActionListener(this);
        vistaDettaglio.getBtnCestina().addActionListener(this);
        vistaDettaglio.getBtnRipristina().addActionListener(this);
        vistaDettaglio.getBtnSvuotaCestino().addActionListener(this);
        vistaDettaglio.getBtnAggiungi().addActionListener(this);
        vistaDettaglio.getBtnIndietro().addActionListener(this);
        vistaDettaglio.getBtnModifica().addActionListener(this);

        vistaDettaglio.getBtnIndietro().setActionCommand("Indietro");
        vistaDettaglio.getBtnCerca().setActionCommand("Cerca");
        vistaDettaglio.getBtnCalcolaTotale().setActionCommand("Totale €");
        vistaDettaglio.getBtnAggiungi().setActionCommand("Aggiungi");
        vistaDettaglio.getBtnModifica().setActionCommand("Modifica");
        vistaDettaglio.getBtnCestina().setActionCommand("Cestina");
        vistaDettaglio.getBtnRipristina().setActionCommand("Ripristina");
        vistaDettaglio.getBtnSvuotaCestino().setActionCommand("Svuota Cestino");
    }

    /**
     * Intercetta le azioni dell'utente ed esegue il relativo metodo di 
     * supporto, mantenendo la logica pulita e modulare.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        if (comando == null) return;

        switch (comando) {
            case "Apri Lista":
                apriLista();
                break;
            case "Indietro":
                tornaIndietro();
                break;
            case "Totale €":
                calcolaTotale();
                break;
            case "Modifica":
                modificaArticolo();
                break;
            case "Aggiungi":
                aggiungiArticolo();
                break;
            case "Cestina":
                cestinaArticolo();
                break;
            case "Ripristina":
                ripristinaArticolo();
                break;
            case "Cerca":
                cercaArticolo();
                break;
            case "Svuota Cestino":
                svuotaCestino();
                break;
            case "Nuova Lista":
                creaNuovaLista();
                break;
            case "Elimina Lista":
                eliminaLista();
                break;
            case "Modifica Lista":
                rinominaLista();
                break;
            default:
                break;
        }

        aggiornaSchermate();
    }

    /**
     * Apre la finestra di dettaglio associata alla lista selezionata,
     * effettuando il cambio di vista nel pannello principale.
     */
    private void apriLista() {
        String nomeLista = vistaPrincipale.getNomeListaSelezionata();
        
        if (nomeLista != null) {
            ListaDiArticoli modelloLista = GestioneListe.getListeArticoli()
                                                        .get(nomeLista);
            
            vistaDettaglio = new FinestraDettaglioLista(modelloLista);
            configuraPulsantiVistaDettaglio();
            
            JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(
                vistaPrincipale
            );
            
            if (mainFrame != null) {
                mainFrame.setContentPane(vistaDettaglio);
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        } else {
            visualizzaAvvisoSelezione("Seleziona una lista prima di aprirla.");
        }
    }

    /**
     * Riporta l'utente alla schermata iniziale, svuotando i riferimenti
     * della vista di dettaglio e rinfrescando l'elenco.
     */
    private void tornaIndietro() {
        rinfrescaVistaPrincipale();
        framePrincipale.setContentPane(vistaPrincipale);
        framePrincipale.revalidate();
        framePrincipale.repaint();
        vistaDettaglio = null;
    }

    /**
     * Invoca il dominio per il calcolo del costo e lo mostra a video.
     */
    private void calcolaTotale() {
        if (vistaDettaglio != null) {
            double totale = vistaDettaglio.getListaModello().calcolaPrezzoTotale();
            JOptionPane.showMessageDialog(
                vistaDettaglio, 
                String.format("Il totale è: %.2f€", totale),
                "Totale Spesa",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Apre il form di modifica recuperando i dati dell'articolo
     * selezionato attualmente dall'utente.
     */
    private void modificaArticolo() {
        if (vistaDettaglio == null) return;

        int indiceArticolo = vistaDettaglio.getIndiceArticoloSelezionato();
        if (indiceArticolo == -1) {
            visualizzaAvvisoSelezione("Seleziona un articolo da modificare.");
            return;
        }

        Articolo articoloDaMod = ottieniArticoloPerIndice(indiceArticolo);
        if (articoloDaMod == null) return;

        String   prezzoAttuale = String.valueOf(articoloDaMod.getPrezzo());
        String[] nuoviDati     = vistaDettaglio.mostraFormArticolo(
            "Modifica Articolo", 
            articoloDaMod.getNota(), 
            articoloDaMod.getCategoria().getNome(), 
            prezzoAttuale
        );

        if (nuoviDati != null) {
            elaboraModificaArticolo(articoloDaMod, nuoviDati);
        }
    }

    /**
     * Applica le modifiche inserite dall'utente sull'oggetto del dominio,
     * previa verifica contro eventuali duplicati.
     */
    private void elaboraModificaArticolo(Articolo articolo, String[] nuoviDati) {
        String nuovaNota      = nuoviDati[0].trim();
        String nuovaCategoria = nuoviDati[1].trim();
        String nuovoPrezzoStr = nuoviDati[2].trim();

        if (nuovaCategoria.isEmpty()) {
            nuovaCategoria = "Non categorizzato";
        }
        
        double nuovoPrezzo = parsePrezzo(nuovoPrezzoStr);

        if (isArticoloDuplicato(nuovaNota, articolo)) {
            JOptionPane.showMessageDialog(
                vistaDettaglio, 
                "Errore: Esiste già un altro articolo con questo nome!", 
                "Articolo Duplicato", 
                JOptionPane.WARNING_MESSAGE
            );
        } else {
            articolo.setNota(nuovaNota);
            articolo.setCategoria(new Categoria(nuovaCategoria));
            articolo.setPrezzo(nuovoPrezzo);
            
            vistaDettaglio.aggiornaElencoArticoli();
            JOptionPane.showMessageDialog(
                vistaDettaglio, 
                "Articolo modificato con successo.",
                "Successo",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Apre il pop-up per permettere l'inserimento di un nuovo articolo.
     */
    private void aggiungiArticolo() {
        if (vistaDettaglio == null) return;

        String[] dati = vistaDettaglio.mostraFormArticolo(
            "Aggiungi Articolo", "", "", ""
        );
        
        if (dati != null) {
            elaboraAggiuntaArticolo(dati);
        }
    }

    /**
     * Registra un nuovo articolo all'interno della lista corrente
     * e notifica la vista di aggiornarsi.
     */
    private void elaboraAggiuntaArticolo(String[] dati) {
        String nota          = dati[0].trim();
        String nomeCategoria = dati[1].trim();
        String prezzoStr     = dati[2].trim();

        if (nomeCategoria.isEmpty()) {
            nomeCategoria = "Non categorizzato";
        }
        
        double prezzo = parsePrezzo(prezzoStr);

        if (isArticoloDuplicato(nota, null)) {
            JOptionPane.showMessageDialog(
                vistaDettaglio, 
                "Errore: Un articolo con questo nome esiste già!", 
                "Articolo Duplicato", 
                JOptionPane.WARNING_MESSAGE
            );
        } else {
            Categoria categoria     = new Categoria(nomeCategoria);
            Articolo  nuovoArticolo = new Articolo(categoria, prezzo, nota);

            GestioneListe.inserisciArticolo(nuovoArticolo);
            vistaDettaglio.getListaModello().aggiungiArticolo(nuovoArticolo);
            vistaDettaglio.aggiornaElencoArticoli();
            
            JOptionPane.showMessageDialog(
                vistaDettaglio, 
                "Articolo aggiunto con successo.",
                "Successo",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Esegue la rimozione logica spostando l'articolo nel cestino.
     */
    private void cestinaArticolo() {
        if (vistaDettaglio == null) return;

        int indiceArticolo = vistaDettaglio.getIndiceArticoloSelezionato();
        if (indiceArticolo != -1) {
            Articolo daCestinare = ottieniArticoloPerIndice(indiceArticolo);
            if(daCestinare != null) {
                GestioneListe.cancellaArticolo(daCestinare);
                vistaDettaglio.aggiornaElencoArticoli();
            }
        } else {
            visualizzaAvvisoSelezione("Articolo non selezionato.");
        }
    }

    /**
     * Ripristina un articolo eliminato riportandolo tra gli attivi.
     */
    private void ripristinaArticolo() {
        if (vistaDettaglio == null) return;

        int indiceArticolo = vistaDettaglio.getIndiceArticoloSelezionato();
        if (indiceArticolo != -1) {
            Articolo daRipristinare = ottieniArticoloPerIndice(indiceArticolo);
            if(daRipristinare != null) {
                vistaDettaglio.getListaModello().ripristinaArticolo(daRipristinare);
                GestioneListe.inserisciArticolo(daRipristinare);
                vistaDettaglio.aggiornaElencoArticoli();
            }
        } else {
            visualizzaAvvisoSelezione("Articolo non selezionato.");
        }
    }

    /**
     * Ricerca un articolo per prefisso gestendo correttamente l'eccezione
     * del dominio tramite finestre di dialogo (risolve il problema del crash).
     */
    private void cercaArticolo() {
        if (vistaDettaglio == null) return;

        String termine = vistaDettaglio.chiediStringaRicerca();
        
        if (termine != null && !termine.trim().isEmpty()) {
            try {
                Articolo a = vistaDettaglio.getListaModello()
                                           .cercaArticoloPerPrefisso(termine);
                
                JOptionPane.showMessageDialog(
                    vistaDettaglio, 
                    "Trovato: " + a.getNota() + " (" + a.getPrezzo() + "€)",
                    "Risultato Ricerca",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (ElementoNonTrovatoException e) {
                JOptionPane.showMessageDialog(
                    vistaDettaglio, 
                    "Nessun articolo trovato con questo prefisso.",
                    "Ricerca Fallita",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    /**
     * Cancella definitivamente tutti gli elementi presenti nel cestino.
     */
    private void svuotaCestino() {
        if (vistaDettaglio != null) {
            vistaDettaglio.getListaModello().svuotaCancellati();
            JOptionPane.showMessageDialog(
                vistaDettaglio, 
                "Cestino svuotato correttamente.",
                "Pulizia Completata",
                JOptionPane.INFORMATION_MESSAGE
            );
            vistaDettaglio.aggiornaElencoArticoli();
        }
    }

    /**
     * Istanzia una nuova lista vuota all'interno del gestore globale.
     */
    private void creaNuovaLista() {
        String nuovoNome = vistaPrincipale.mostraFormLista("Nuova Lista", "");
        
        if (nuovoNome != null) {
            nuovoNome = nuovoNome.trim();
            
            if (nuovoNome.isEmpty()) {
                JOptionPane.showMessageDialog(
                    framePrincipale, 
                    "Il nome della lista non può essere vuoto!", 
                    "Errore", 
                    JOptionPane.ERROR_MESSAGE
                );
            } else if (GestioneListe.getListeArticoli().containsKey(nuovoNome)) {
                JOptionPane.showMessageDialog(
                    framePrincipale, 
                    "Esiste già una lista con questo nome!", 
                    "Errore", 
                    JOptionPane.WARNING_MESSAGE
                );
            } else {
                GestioneListe.creaLista(nuovoNome, null);
                rinfrescaVistaPrincipale();
            }
        }
    }

    /**
     * Rimuove un'intera lista dal sistema, chiedendo previa conferma.
     */
    private void eliminaLista() {
        String nomeSelezionato = vistaPrincipale.getNomeListaSelezionata();
        
        if (nomeSelezionato != null) {
            int conferma = JOptionPane.showConfirmDialog(
                framePrincipale, 
                "Eliminare definitivamente la lista '" + nomeSelezionato + "'?", 
                "Conferma Eliminazione", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE
            );
            
            if (conferma == JOptionPane.YES_OPTION) {
                GestioneListe.cancellaLista(nomeSelezionato);
                rinfrescaVistaPrincipale();
            }
        } else {
            visualizzaAvvisoSelezione("Seleziona prima una lista da eliminare!");
        }
    }

    /**
     * Gestisce la ridenominazione di una lista della spesa.
     */
    private void rinominaLista() {
        String nomeAttuale = vistaPrincipale.getNomeListaSelezionata();
        
        if (nomeAttuale == null) {
            visualizzaAvvisoSelezione("Seleziona prima una lista da modificare.");
            return;
        }

        String nuovoNome = vistaPrincipale.mostraFormLista(
            "Rinomina Lista", nomeAttuale
        );
        
        if (nuovoNome != null && !nuovoNome.trim().isEmpty() 
            && !nuovoNome.equals(nomeAttuale)) {
            
            nuovoNome = nuovoNome.trim();
            migraListaDiArticoli(nomeAttuale, nuovoNome);
        }
    }

    /**
     * Metodo di supporto per aggirare la copia difensiva della mappa di gestione.
     * Crea una nuova lista con il nuovo nome e vi sposta ricorsivamente gli articoli.
     * 
     * @param nomeAttuale il nome originale della lista
     * @param nuovoNome   il nuovo identificativo richiesto
     */
    private void migraListaDiArticoli(String nomeAttuale, String nuovoNome) {
        ListaDiArticoli vecchia = GestioneListe.getListeArticoli().get(nomeAttuale);
        if (vecchia == null) return;

        GestioneListe.creaLista(nuovoNome, null);
        ListaDiArticoli nuova = GestioneListe.getListeArticoli().get(nuovoNome);

        if (nuova != null) {
            for (Articolo a : vecchia.getContenitoreArticolo()) {
                nuova.aggiungiArticolo(a);
            }
            for (Articolo a : vecchia.getArticoliCancellati()) {
                nuova.aggiungiArticolo(a);
                nuova.cancellaArticolo(a);
            }
            GestioneListe.cancellaLista(nomeAttuale);
            rinfrescaVistaPrincipale();
        }
    }

    /**
     * Esegue il rinfresco forzato dei dati all'interno delle finestre grafiche.
     */
    private void aggiornaSchermate() {
        if (vistaDettaglio != null) {
            vistaDettaglio.aggiornaElencoArticoli();
        } else {
            rinfrescaVistaPrincipale();
        }
    }

    /**
     * Interroga l'archivio globale e aggiorna l'elenco della schermata iniziale.
     */
    private void rinfrescaVistaPrincipale() {
        List<String> nomi = new ArrayList<>(
            GestioneListe.getListeArticoli().keySet()
        );
        vistaPrincipale.aggiornaElenco(nomi);
    }

    /**
     * Cerca sequenzialmente l'articolo che corrisponde alla riga selezionata.
     * 
     * @param indice la riga evidenziata nella JList
     * @return l'articolo corrispondente, o null se non trovato
     */
    private Articolo ottieniArticoloPerIndice(int indice) {
        if (indice < 0) return null;
        
        int contatore = 0;
        for (Articolo a : vistaDettaglio.getListaModello()) {
            if (contatore == indice) {
                return a;
            }
            contatore++;
        }
        return null;
    }

    /**
     * Verifica l'eventuale presenza di un articolo con lo stesso nome
     * all'interno del modello della lista della spesa corrente.
     * 
     * @param nota    il testo da verificare
     * @param escludi un articolo da ignorare (utile durante la modifica)
     * @return true se è presente un omonimo, false altrimenti
     */
    private boolean isArticoloDuplicato(String nota, Articolo escludi) {
        // FIX APPLICATO QUI: Se la nota è vuota, salta il controllo dei duplicati.
        if (nota.trim().isEmpty()) {
            return false;
        }
        
        for (Articolo a : vistaDettaglio.getListaModello()) {
            if (a != escludi && a.getNota().equalsIgnoreCase(nota)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tenta la conversione della stringa in un prezzo decimale valido,
     * supportando sia il punto che la virgola.
     * 
     * @param prezzoStr la stringa inserita nel form
     * @return il double formattato (minimo 0.0)
     */
    private double parsePrezzo(String prezzoStr) {
        if (prezzoStr == null || prezzoStr.trim().isEmpty()) {
            return 0.0;
        }
        try {
            prezzoStr = prezzoStr.replace(",", ".");
            double prezzo = Double.parseDouble(prezzoStr);
            return prezzo < 0 ? 0.0 : prezzo;
        } 
        catch (NumberFormatException ex) {
            return 0.0;
        }
    }

    /**
     * Metodo di supporto per centralizzare i popup di avvertimento 
     * quando un utente dimentica di selezionare un elemento.
     * 
     * @param messaggio il testo di avviso
     */
    private void visualizzaAvvisoSelezione(String messaggio) {
        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(
            vistaPrincipale
        );
        JOptionPane.showMessageDialog(
            mainFrame != null ? mainFrame : vistaDettaglio, 
            messaggio, 
            "Selezione Richiesta", 
            JOptionPane.WARNING_MESSAGE
        );
    }
}