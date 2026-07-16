package ui.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import model.Articolo;
import model.Categoria;
import model.GestioneListe;
import model.ListaDiArticoli;
import model.exception.ElementoNonTrovatoException;
import model.exception.NumeroSbagliatoException;

/**
 * Questa classe rappresenta l'interfaccia testuale (Command Line Interface) 
 * dell'applicazione. Funge da "View" e "Controller" per gli input da terminale: 
 * si occupa esclusivamente di interfacciarsi con l'utente, raccogliere i dati 
 * ed esporre i risultati, delegando la logica di business alle classi del dominio.
 * 
 * @author Davide Aime e Mattia Cremonesi
 */
public class InterfacciaTestuale {

    /**
     * Punto di ingresso principale del menu testuale. Mantiene vivo il ciclo 
     * di esecuzione dell'applicazione finché l'utente non decide di uscire, 
     * gestendo centralmente eventuali eccezioni di input o di dominio.
     * 
     * @param scanner lo strumento utilizzato per catturare l'input da tastiera
     */
    public void avvia(Scanner scanner) {
        boolean ripeti = true;
        
        do {
            stampaIntestazioneMenuPrincipale();
            
            try {
                ripeti = menuPrincipale(scanner);
            } catch (NumeroSbagliatoException | ElementoNonTrovatoException e) {
                System.out.println("\n[!] Errore: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("\n[!] Errore: Tipo di input non valido. "
                                 + "Inserisci un numero.");
                scanner.nextLine(); // Pulisce il buffer in caso di errore
            } catch (Exception e) {
                System.out.printf("\n[!] Errore imprevisto: %s\n", e.getMessage());
            }
        } while (ripeti);    
    }

    /**
     * Metodo privato di supporto per alleggerire la vista stampando 
     * l'intestazione grafica del menu principale.
     */
    private void stampaIntestazioneMenuPrincipale() {
        System.out.println("\n========================================");
        System.out.println("          MENU PRINCIPALE               ");
        System.out.println("========================================");
        System.out.println("[1] Crea una nuova lista della spesa");
        System.out.println("[2] Gestisci l'archivio (Visualizza/Modifica/Elimina)");
        System.out.println("[0] Esci dal programma");
        System.out.print("Scegli un'opzione: ");
    }
    
    /**
     * Raccoglie la scelta dell'utente per il menu principale e lo indirizza 
     * verso il corretto flusso di operazioni.
     * 
     * @param scanner lo scanner per acquisire l'opzione
     * @return true se il ciclo principale deve continuare, false se si deve uscire
     * @throws NumeroSbagliatoException    se viene inserito un numero non a menu
     * @throws ElementoNonTrovatoException se operazioni annidate non trovano un dato
     */
    private boolean menuPrincipale(Scanner scanner) 
            throws NumeroSbagliatoException, ElementoNonTrovatoException {
        
        int sceltaMenu = scanner.nextInt();
        scanner.nextLine(); // Consuma il newline
        
        if (sceltaMenu < 0 || sceltaMenu > 2) {
            throw new NumeroSbagliatoException(
                "Scelta non valida nel menu principale."
            );
        }
        
        switch (sceltaMenu) {
            case 0:
                System.out.println("Uscita dall'interfaccia testuale in corso...");
                return false;
            case 1:
                creaNuovaLista(scanner);
                break;
            case 2:
                avviaSottomenuGestione(scanner);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Gestisce il pannello secondario dedicato all'esplorazione e alla 
     * manipolazione delle liste esistenti.
     * 
     * @param scanner lo scanner per acquisire le selezioni dell'utente
     * @throws NumeroSbagliatoException    in caso di opzioni numeriche non previste
     * @throws ElementoNonTrovatoException in caso di entità mancanti durante la ricerca
     */
    private void avviaSottomenuGestione(Scanner scanner) 
            throws NumeroSbagliatoException, ElementoNonTrovatoException {
        
        boolean ripetiSottomenu = true;
        
        do {
            System.out.println("\n----------------------------------------");
            System.out.println("       PANNELLO DI GESTIONE LISTE         ");
            System.out.println("----------------------------------------");
            System.out.println("[1] Visualizza le tue liste");
            System.out.println("[2] Modifica o Elimina elementi globali");
            System.out.println("[0] Torna al menu principale");
            System.out.print("Scegli un'opzione: ");
            
            ripetiSottomenu = gestioneListe(scanner);
        } while (ripetiSottomenu);
    }

    /**
     * Raccoglie i dati necessari (categoria, prezzo, nota e nome lista) 
     * per instanziare una nuova lista della spesa e il suo primo articolo.
     * 
     * @param scanner lo scanner per raccogliere le informazioni
     * @throws NumeroSbagliatoException se i dati numerici inseriti sono scorretti
     */
    private void creaNuovaLista(Scanner scanner) throws NumeroSbagliatoException {
        System.out.println("\n--- CREAZIONE NUOVA LISTA ---");
        
        String    nomeCategoria = chiediInfCategoria(scanner); 
        Categoria categoria     = new Categoria(nomeCategoria);
        
        double    prezzo        = chiediPrezzo(scanner);
        String    nota          = chiediNota(scanner);
        
        Articolo  articolo      = new Articolo(categoria, prezzo, nota);
        String    nomeLista     = chiediInfLista(scanner);
        
        GestioneListe.inserisciCategoria(categoria);
        GestioneListe.inserisciArticolo(articolo);
        GestioneListe.creaLista(nomeLista, articolo);
        
        System.out.println("\n[V] Lista '" + nomeLista 
                         + "' creata con successo insieme al primo articolo!");
    }
    
    /**
     * Smista l'utente tra la visualizzazione dell'archivio e le operazioni 
     * di modifica/cancellazione globale.
     * 
     * @param scanner lo scanner per la lettura dell'input
     * @return true per rimanere in questo pannello, false per tornare indietro
     * @throws NumeroSbagliatoException    se il comando numerico è errato
     * @throws ElementoNonTrovatoException se le esplorazioni successive falliscono
     */
    private boolean gestioneListe(Scanner scanner) 
            throws NumeroSbagliatoException, ElementoNonTrovatoException {
        
        int sceltaAzione = scanner.nextInt();
        scanner.nextLine();
        
        if (sceltaAzione < 0 || sceltaAzione > 2) {
            throw new NumeroSbagliatoException("Scelta non valida in Gestione Liste.");
        }
        
        switch (sceltaAzione) {
            case 0:
                return false;
            case 1:
                eseguiMenuVisualizzazione(scanner);
                break;
            case 2:
                eseguiMenuModificaCancellazione(scanner);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Contiene il ciclo di vita del sottomenu dedicato alla visualizzazione 
     * e all'accesso specifico alle liste.
     * 
     * @param scanner lo scanner per l'interazione
     */
    private void eseguiMenuVisualizzazione(Scanner scanner) 
            throws NumeroSbagliatoException, ElementoNonTrovatoException {
        
        boolean ripetiMenu = true;
        do {
            System.out.println("\n----------------------------------------");
            System.out.println("          VISUALIZZAZIONE LISTE         ");
            System.out.println("----------------------------------------");
            System.out.println("[1] Mostra il riepilogo di TUTTE le liste");
            System.out.println("[2] Seleziona e opera su una lista specifica");
            System.out.println("[0] Torna indietro");
            System.out.print("Scegli un'opzione: ");
            
            ripetiMenu = menuVisualizzaListe(scanner);
        } while (ripetiMenu);
    }

    /**
     * Contiene il ciclo di vita del sottomenu dedicato alla modifica 
     * e alla rimozione definitiva delle entità globali.
     * 
     * @param scanner lo scanner per l'interazione
     */
    private void eseguiMenuModificaCancellazione(Scanner scanner) 
            throws NumeroSbagliatoException, ElementoNonTrovatoException {
        
        boolean ripetiMenu = true;
        do {
            System.out.println("\n----------------------------------------");
            System.out.println("         MODIFICA E CANCELLAZIONE       ");
            System.out.println("----------------------------------------");
            System.out.println("[1] Elimina globalmente (Lista/Categoria/Articolo)");
            System.out.println("[2] Modifica i dati di un articolo in linea");
            System.out.println("[0] Torna indietro");
            System.out.print("Scegli un'opzione: ");
            
            ripetiMenu = modificaCancellazione(scanner);
        } while (ripetiMenu);
    }

    /**
     * Gestisce la scelta legata alla visualizzazione di una o tutte le liste.
     * Include un controllo preventivo sullo stato di popolamento dell'archivio.
     * 
     * @param scanner lo scanner per la lettura dell'input
     * @return true se si rimane nel sottomenu, false altrimenti
     */
    private boolean menuVisualizzaListe(Scanner scanner) 
            throws NumeroSbagliatoException, ElementoNonTrovatoException {
        
        int opzione = scanner.nextInt();
        scanner.nextLine();
        
        if (opzione < 0 || opzione > 2) {
            throw new NumeroSbagliatoException("Opzione non valida.");
        }
        
        if (opzione != 0 && GestioneListe.getListeArticoli().isEmpty()) {
            System.out.println("\n[!] L'archivio è vuoto. Nessuna lista registrata.");
            return true;
        }
        
        switch (opzione) {
            case 0:
                return false;
            case 1:
                stampaTutteLeListe();
                break;
            case 2:
                selezionaEdEsploraLista(scanner);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Stampa in console un riepilogo grafico dell'intero archivio, 
     * distinguendo visivamente gli articoli attivi da quelli nel cestino.
     */
    private void stampaTutteLeListe() {
        System.out.println("\n========================================");
        System.out.println("       RIEPILOGO GENERALE ARCHIVIO      ");
        System.out.println("========================================");
        
        for (ListaDiArticoli lista : GestioneListe.getListeArticoli().values()) {
            System.out.println("\nLISTA: " + lista.getNome().toUpperCase());
            System.out.println("----------------------------------------");
            
            for (Articolo a : lista) {
                String stato    = lista.getArticoliCancellati().contains(a) 
                                  ? "[CESTINO]" : "(Attivo) ";
                String nomeCat  = a.getCategoria().getNome();
                
                System.out.printf("   %s -> %s (%s) | %.2f€\n", 
                                  stato, a.getNota(), nomeCat, a.getPrezzo());
            }
            System.out.println("----------------------------------------");
        }
    }

    /**
     * Richiede all'utente di selezionare una lista specifica dall'archivio 
     * digitandone l'indice numerico associato a video.
     * 
     * @param scanner lo scanner utilizzato per l'acquisizione
     */
    private void selezionaEdEsploraLista(Scanner scanner) 
            throws NumeroSbagliatoException, ElementoNonTrovatoException {
        
        System.out.println("Liste disponibili:");
        List<ListaDiArticoli> elencoListe = new ArrayList<>(
            GestioneListe.getListeArticoli().values()
        );
        
        for (int i = 0; i < elencoListe.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + elencoListe.get(i).getNome());
        }
        System.out.println("[0] per tornare indietro");
        
        int sceltaIndex = scanner.nextInt();
        scanner.nextLine();
        
        if (sceltaIndex == 0) {
            return;
        }
        
        if (sceltaIndex > 0 && sceltaIndex <= elencoListe.size()) {
            ListaDiArticoli listaSelezionata = elencoListe.get(sceltaIndex - 1);
            eseguiOperazioniSuLista(scanner, listaSelezionata);
        } else {
            throw new NumeroSbagliatoException("Numero di lista non valido.");
        }
    }

    /**
     * Gestisce le operazioni atomiche eseguibili su una singola lista della spesa 
     * (es. ricerche, rimozioni logiche, ripristini).
     * 
     * @param scanner          lo scanner per i parametri aggiuntivi
     * @param listaSelezionata la lista isolata su cui lavorare
     */
    private void eseguiOperazioniSuLista(Scanner scanner, 
                                         ListaDiArticoli listaSelezionata) 
            throws NumeroSbagliatoException, ElementoNonTrovatoException {
        
        System.out.println("\n========================================");
        System.out.println("  OPZIONI LISTA: " 
                           + listaSelezionata.getNome().toUpperCase());
        System.out.println("========================================");
        System.out.println("[1] Cerca articolo per prefisso (Attivo o Cestinato)");
        System.out.println("[2] Calcola il prezzo totale corrente");
        System.out.println("[3] Sposta un articolo nel CESTINO (Rimozione Logica)");
        System.out.println("[4] RIPRISTINA un articolo dal cestino");
        System.out.println("[5] SVUOTA permanentemente il cestino");
        System.out.println("[6] Aggiungi un nuovo articolo a questa lista");
        System.out.println("[0] Esci da questa lista");
        System.out.print("Scegli un'operazione: ");
        
        int op = scanner.nextInt();
        scanner.nextLine();
        
        switch (op) {
            case 1:
                cercaEStampaPerPrefisso(scanner, listaSelezionata);
                break;
            case 2:
                System.out.println("Prezzo totale degli articoli attivi: " 
                                 + listaSelezionata.calcolaPrezzoTotale() + "€");
                break;
            case 3:
                spostaArticoloInCestino(scanner, listaSelezionata);
                break;
            case 4:
                ripristinaDaCestino(scanner, listaSelezionata);
                break;
            case 5:
                listaSelezionata.svuotaCancellati();
                System.out.println("Il cestino degli articoli eliminati è vuoto.");
                break;
            case 6:
                aggiungiArticoloEsterno(scanner, listaSelezionata);
                break;
            case 0:
                break;
            default:
                throw new NumeroSbagliatoException("Operazione non valida.");
        }
    }

    /**
     * Sottometodo per delegare la rimozione logica (spostamento nel cestino)
     * di un articolo appartenente a una specifica lista.
     */
    private void spostaArticoloInCestino(Scanner scanner, 
                                         ListaDiArticoli listaSelezionata) {
        System.out.println("Inserisci la nota dell'articolo da cestinare:");
        String nota = scanner.nextLine();
        
        try {
            Articolo daCancellare = listaSelezionata.cercaArticoloPerPrefisso(nota);
            listaSelezionata.cancellaArticolo(daCancellare);
            System.out.println("Articolo '" + daCancellare.getNota() 
                             + "' spostato nel cestino.");
        } catch (ElementoNonTrovatoException e) {
            System.out.println("Articolo non trovato in questa lista.");
        }
    }

    /**
     * Raccoglie i dati per un nuovo articolo e lo inserisce sia nell'archivio 
     * globale sia all'interno della lista specificata.
     */
    private void aggiungiArticoloEsterno(Scanner scanner, 
                                         ListaDiArticoli listaSelezionata) 
            throws NumeroSbagliatoException {
        
        String    nomeCategoria = chiediInfCategoria(scanner); 
        Categoria categoria     = new Categoria(nomeCategoria);
        
        double    prezzo        = chiediPrezzo(scanner);
        String    nota          = chiediNota(scanner);
        
        Articolo  nuovoArticolo = new Articolo(categoria, prezzo, nota);
        
        GestioneListe.inserisciCategoria(categoria);
        GestioneListe.inserisciArticolo(nuovoArticolo);
        listaSelezionata.aggiungiArticolo(nuovoArticolo);
        
        System.out.println("Articolo aggiunto con successo alla lista " 
                         + listaSelezionata.getNome() + "!");
    }
    
    /**
     * Cerca un articolo tramite prefisso e, se trovato, ne stampa i dettagli base.
     */
    private void cercaEStampaPerPrefisso(Scanner scanner, 
                                         ListaDiArticoli listaSelezionata) 
            throws ElementoNonTrovatoException {
        
        System.out.println("Inserisci il prefisso della nota da cercare:");
        String   prefisso = scanner.nextLine();
        Articolo trovato  = listaSelezionata.cercaArticoloPerPrefisso(prefisso);
        
        System.out.println("Articolo trovato: " + trovato.getNota() 
                         + " | Prezzo: " + trovato.getPrezzo() + "€");
    }
    
    /**
     * Tenta di recuperare un articolo precedentemente rimosso logicamente, 
     * riattivandolo all'interno della lista.
     */
    private void ripristinaDaCestino(Scanner scanner, 
                                     ListaDiArticoli listaSelezionata) 
            throws NumeroSbagliatoException {
        
        System.out.println("Inserisci la nota dell'articolo da ripristinare:");
        String   nota           = scanner.nextLine();
        Articolo daRipristinare = listaSelezionata.cercaNeiCancellati(nota);
        
        if (daRipristinare != null) {
            listaSelezionata.ripristinaArticolo(daRipristinare);
            System.out.println("Articolo ripristinato con successo.");
        } else {
            throw new NumeroSbagliatoException(
                "Articolo non presente nel cestino dei cancellati."
            );
        }
    }
    
    /**
     * Direziona l'utente verso i sottomenu di modifica dei campi o 
     * di cancellazione irreversibile dall'archivio globale.
     */
    private boolean modificaCancellazione(Scanner scanner) 
            throws NumeroSbagliatoException, ElementoNonTrovatoException {
        
        int opzione = scanner.nextInt();
        scanner.nextLine();
        
        if (opzione < 0 || opzione > 2) {
            throw new NumeroSbagliatoException(
                "Scelta non valida in Modifica/Cancellazione."
            );
        }
        
        switch (opzione) {
            case 0:
                return false; 
            case 1:
                eseguiMenuCancellazioneGlobale(scanner);
                break;
            case 2:
                eseguiMenuModificaLinea(scanner);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Gestisce la cancellazione globale e irreversibile di un'entità del dominio.
     */
    private void eseguiMenuCancellazioneGlobale(Scanner scanner) 
            throws NumeroSbagliatoException, ElementoNonTrovatoException {
        
        boolean ripetiCanc = true;
        do {
            System.out.println("\n----------------------------------------");
            System.out.println("          ELIMINAZIONE GLOBALE          ");
            System.out.println("----------------------------------------");
            System.out.println("[1] Elimina del tutto una LISTA");
            System.out.println("[2] Elimina del tutto una CATEGORIA");
            System.out.println("[3] Elimina del tutto un ARTICOLO");
            System.out.println("[0] Torna indietro");
            System.out.print("Scegli un'opzione: ");
            
            ripetiCanc = cancellazione(scanner);
        } while (ripetiCanc);
    }

    /**
     * Gestisce il menu operativo per alterare le proprietà di un articolo esistente.
     */
    private void eseguiMenuModificaLinea(Scanner scanner) 
            throws NumeroSbagliatoException, ElementoNonTrovatoException {
        
        boolean ripetiMod = true; 
        do {
            System.out.println("\n----------------------------------------");
            System.out.println("          MODIFICA IN LINEA             ");
            System.out.println("----------------------------------------");
            System.out.println("[1] Modifica il PREZZO di un articolo");
            System.out.println("[2] Modifica la CATEGORIA di un articolo");
            System.out.println("[3] Modifica la NOTA di un articolo");
            System.out.println("[0] Torna indietro");
            System.out.print("Scegli un'opzione: ");
            
            ripetiMod = modifica(scanner);
        } while (ripetiMod);
    }
    
    /**
     * Raccoglie i nuovi dati inseriti dall'utente e sovrascrive le proprietà 
     * dell'articolo ricercato nel database applicativo.
     */
    private boolean modifica(Scanner scanner) 
            throws NumeroSbagliatoException, ElementoNonTrovatoException {
        
        int cosaModificare = scanner.nextInt();
        scanner.nextLine();
        
        if (cosaModificare < 0 || cosaModificare > 3) {
            throw new NumeroSbagliatoException("Input di modifica non valido.");
        }
        
        if (cosaModificare == 0) {
            return false;
        }
        
        System.out.println("Inserisci la nota dell'articolo da modificare: ");
        String   notaCercata     = scanner.nextLine();
        Articolo articoloTrovato = GestioneListe.cercaArticoloGlobale(notaCercata);
        
        switch (cosaModificare) {
            case 1:
                double prezzo = chiediPrezzo(scanner);
                articoloTrovato.setPrezzo(prezzo);
                System.out.println("Prezzo aggiornato a: " 
                                 + articoloTrovato.getPrezzo() + "€");
                break;
            case 2:
                String    nomeCat          = chiediInfCategoria(scanner);
                Categoria categoriaCercata = GestioneListe.cercaCategoriaGlobale(nomeCat);
                articoloTrovato.setCategoria(categoriaCercata);
                System.out.println("Categoria aggiornata in: " 
                                 + articoloTrovato.getCategoria().getNome());
                break;
            case 3:
                String nuovaNota = chiediNota(scanner);
                articoloTrovato.setNota(nuovaNota);
                System.out.println("Nota modificata in: " + articoloTrovato.getNota());
                break;
            default:
                break;
        }
        return true;
    }
    
    /**
     * Esegue l'effettiva distruzione dell'entità scelta (Lista, Categoria, Articolo)
     * chiedendo conferma visiva dei dati da eliminare.
     */
    private boolean cancellazione(Scanner scanner) 
            throws NumeroSbagliatoException, ElementoNonTrovatoException {
        
        int opzione = scanner.nextInt();
        scanner.nextLine();
        
        if (opzione < 0 || opzione > 3) {
            throw new NumeroSbagliatoException(
                "Numero di opzione cancellazione errato."
            );
        }
        
        switch (opzione) {
            case 0:
                return false;
            case 1:
                System.out.println("Inserisci il nome della lista da eliminare: ");
                String nomeLista = scanner.nextLine();     
                
                if (GestioneListe.getListeArticoli().containsKey(nomeLista)) {
                    GestioneListe.cancellaLista(nomeLista);
                    System.out.println("Lista eliminata con successo.");
                } else {
                    throw new NumeroSbagliatoException(
                        "Nome della lista non trovato nell'archivio."
                    );
                }
                break;
            case 2:
                System.out.println("Inserisci il nome della categoria da eliminare: ");
                String    nomeCat = scanner.nextLine();
                Categoria c       = GestioneListe.cercaCategoriaGlobale(nomeCat);
                
                GestioneListe.cancellaCategoria(c);
                System.out.println("Categoria cancellata.");
                break;
            case 3:
                System.out.println("Inserisci la nota dell'articolo da eliminare: ");
                String   notaCercata = scanner.nextLine();
                Articolo a           = GestioneListe.cercaArticoloGlobale(notaCercata);
                
                GestioneListe.cancellaArticolo(a);
                System.out.println("Articolo eliminato con successo.");
                break;
            default:
                break;
        }
        return true;
    }
    
    // =========================================================================
    // METODI DI SUPPORTO PER L'ACQUISIZIONE SICURA DEI DATI (FORM)
    // =========================================================================

    /**
     * Richiede l'inserimento del nome di una categoria tramite terminale.
     * 
     * @param scanner lo scanner in uso
     * @return il testo inserito
     */
    private String chiediInfCategoria(Scanner scanner) {
        System.out.println("Inserisci il nome della categoria: ");
        return scanner.nextLine();
    }
    
    /**
     * Richiede l'inserimento di un prezzo valido per un articolo, 
     * assicurandosi che il valore fornito non sia negativo.
     * 
     * @param scanner lo scanner in uso
     * @return il prezzo formattato in formato decimale
     * @throws NumeroSbagliatoException se il prezzo inserito è minore di zero
     */
    private double chiediPrezzo(Scanner scanner) throws NumeroSbagliatoException {
        System.out.println("Inserisci il prezzo dell'articolo (€): ");
        double prezzo = scanner.nextDouble();
        scanner.nextLine(); 
        
        if (prezzo < 0) {
            throw new NumeroSbagliatoException("Il prezzo non può essere negativo.");
        }
        return prezzo;
    }
    
    /**
     * Richiede la digitazione della nota (nome o descrizione) dell'articolo.
     * 
     * @param scanner lo scanner in uso
     * @return la stringa digitata dall'utente
     */
    private String chiediNota(Scanner scanner) {
        System.out.println("Inserisci la nota dell'articolo: ");
        return scanner.nextLine();
    }
    
    /**
     * Richiede il nome da assegnare alla lista della spesa da creare.
     * 
     * @param scanner lo scanner in uso
     * @return il nome desiderato per la lista
     */
    private String chiediInfLista(Scanner scanner) {
        System.out.println("Inserisci il nome della nuova lista: ");
        return scanner.nextLine();
    }
}