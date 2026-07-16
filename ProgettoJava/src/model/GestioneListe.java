package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.exception.ElementoNonTrovatoException;

/**
 * Classe di gestione centralizzata che mantiene l'archivio globale delle liste,
 * delle categorie e degli articoli all'interno del sistema.
 * Fornisce metodi statici per la creazione, cancellazione e ricerca, 
 * agendo come unico punto di accesso per le operazioni di dominio globali.
 * * @author Davide Aime e Mattia Cremonesi
 */
public class GestioneListe {

    private static Map<String, ListaDiArticoli> listeArticoli = new HashMap<>();
    private static List<Categoria>              categorie     = new ArrayList<>(); 
    private static List<Articolo>               articoli      = new ArrayList<>();
    
    /**
     * Crea una nuova lista inserendola nell'archivio globale.
     * * @param nome             il nome identificativo della lista
     * @param articoloIniziale l'articolo inserito alla creazione
     */
    public static void creaLista(String nome, Articolo articoloIniziale) {
        if (nome != null && !nome.trim().isEmpty()) {
            ListaDiArticoli nuovaLista = new ListaDiArticoli(nome, articoloIniziale);
            listeArticoli.put(nome, nuovaLista);
        }
    }

    /**
     * Rinomina una lista esistente aggiornandone il riferimento globale.
     * * @param vecchioNome il nome attuale della lista
     * @param nuovoNome   il nuovo nome da assegnare
     */
    public static void rinominaLista(String vecchioNome, String nuovoNome) {
        if (vecchioNome != null && nuovoNome != null && !nuovoNome.trim().isEmpty()) {
            ListaDiArticoli lista = listeArticoli.remove(vecchioNome);
            if (lista != null) {
                listeArticoli.put(nuovoNome, lista);
            }
        }
    }
    
    /**
     * Rimuove permanentemente una lista dall'archivio globale.
     * * @param nome il nome della lista da eliminare
     */
    public static void cancellaLista(String nome) {
        if (nome != null) {
            listeArticoli.remove(nome);
        }
    }
    
    /**
     * Inserisce una nuova categoria nell'elenco globale se non è già presente.
     * * @param categoria la categoria condivisa da aggiungere
     */
    public static void inserisciCategoria(Categoria categoria) {
        if (categoria != null && !categorie.contains(categoria)) {
            categorie.add(categoria);
        }
    }
    
    /**
     * Rimuove una categoria dall'elenco globale.
     * * @param categoria la categoria da rimuovere
     */
    public static void cancellaCategoria(Categoria categoria) {
        if (categoria != null) {
            categorie.remove(categoria);
        }
    }

    /**
     * Restituisce o crea la categoria condivisa di default del sistema.
     * * @return l'oggetto Categoria corrispondente al default di progetto
     */
    public static Categoria getCategoriaDefault() {
        try {
            return cercaCategoriaGlobale("Non categorizzato");
        } catch (ElementoNonTrovatoException e) {
            Categoria def = new Categoria("Non categorizzato");
            inserisciCategoria(def);
            return def;
        }
    }
    
    /**
     * Inserisce un nuovo articolo nell'elenco globale se non è già presente.
     * * @param articolo l'articolo condiviso da aggiungere
     */
    public static void inserisciArticolo(Articolo articolo) {
        if (articolo != null && !articoli.contains(articolo)) {
            articoli.add(articolo);
        }
    }
    
    /**
     * Rimuove un articolo dall'elenco globale e delega alle singole liste
     * il compito di spostarlo nello stato di cancellazione logica.
     * * @param articolo l'articolo da rimuovere
     */
    public static void cancellaArticolo(Articolo articolo) {
        if (articolo == null) {
            return;
        }
        
        articoli.remove(articolo);
        
        for (ListaDiArticoli lista : listeArticoli.values()) {
            lista.cancellaArticolo(articolo);
        }
    }
    
    /**
     * Cerca globalmente un articolo registrato nel sistema tramite la sua nota.
     * * @param nota la nota dell'articolo da cercare
     * @return l'articolo individuato
     * @throws ElementoNonTrovatoException se nessun articolo corrisponde alla nota
     */
    public static Articolo cercaArticoloGlobale(String nota) 
            throws ElementoNonTrovatoException {
        
        if (nota != null) {
            for (Articolo a : articoli) {
                if (a.getNota().equalsIgnoreCase(nota.trim())) {
                    return a;
                }
            }
        }
        throw new ElementoNonTrovatoException(
            "Articolo non trovato con la nota inserita."
        );
    }

    /**
     * Cerca globalmente una categoria registrata nel sistema tramite il suo nome.
     * * @param nome il nome della categoria da cercare
     * @return la categoria individuata
     * @throws ElementoNonTrovatoException se nessuna categoria corrisponde al nome
     */
    public static Categoria cercaCategoriaGlobale(String nome) 
            throws ElementoNonTrovatoException {
        
        if (nome != null) {
            for (Categoria c : categorie) {
                if (c.getNome().equalsIgnoreCase(nome.trim())) {
                    return c;
                }
            }
        }
        throw new ElementoNonTrovatoException("Categoria non trovata nel sistema.");
    }
    
    /**
     * Restituisce una copia dell'archivio delle liste correnti.
     * * @return una mappa contenente le liste di articoli
     */
    public static Map<String, ListaDiArticoli> getListeArticoli() {
        return new HashMap<>(listeArticoli);
    }

    /**
     * Restituisce una copia dell'elenco delle categorie condivise.
     * * @return la lista delle categorie
     */
    public static ArrayList<Categoria> getCategorie() {
        return new ArrayList<>(categorie);
    }

    /**
     * Restituisce una copia dell'elenco degli articoli condivisi.
     * * @return la lista degli articoli
     */
    public static ArrayList<Articolo> getArticoli() {
        return new ArrayList<>(articoli);
    }
    
    /**
     * Rimuove tutti gli elementi dalle collezioni globali, ripristinando
     * il sistema al suo stato iniziale vuoto.
     */
    public static void svuotaTutto() {
        listeArticoli.clear();
        categorie.clear();
        articoli.clear();
    }
}