package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.exception.ElementoNonTrovatoException;

/**
 * Classe che rappresenta una lista della spesa.
 * Gestisce due insiemi separati: gli articoli attivi e quelli cancellati logicamente (cestino),
 * permettendo operazioni di aggiunta, rimozione, ripristino e calcolo del totale.
 * 
 * @author Davide Aime e Mattia Cremonesi
 */
public class ListaDiArticoli implements Iterable<Articolo> {

    private String nome;
    private List<Articolo> contenitoreArticolo = new ArrayList<>();
    private List<Articolo> articoliCancellati = new ArrayList<>();
    
    /**
     * Costruisce una nuova lista di articoli.
     * 
     * @param nome il nome della lista (se nullo o vuoto viene assegnato un valore di default)
     * @param articolo un primo articolo da inserire nella lista (ignorato se nullo)
     */
    public ListaDiArticoli(String nome, Articolo articolo) {
        setNome(nome);
        if (articolo != null) {             
            contenitoreArticolo.add(articolo);
        }
    }

    /**
     * Imposta il nome della lista. 
     * Se il parametro è nullo o vuoto, viene assegnato il nome "Nuova Lista".
     * 
     * @param nome il nome da assegnare alla lista
     */
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            this.nome = "Nuova Lista";
        } else {
            this.nome = nome.trim();
        }
    }
    
    /**
     * Restituisce il nome della lista.
     * 
     * @return il nome della lista
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Restituisce la lista degli articoli attualmente attivi.
     * 
     * @return la lista degli articoli attivi
     */
    public List<Articolo> getContenitoreArticolo() {
        return this.contenitoreArticolo;
    }

    /**
     * Restituisce la lista degli articoli che sono stati cancellati logicamente (cestino).
     * 
     * @return la lista degli articoli cancellati
     */
    public List<Articolo> getArticoliCancellati() {
        return this.articoliCancellati;
    }

    /**
     * Aggiunge un nuovo articolo alla lista degli articoli attivi, 
     * a patto che non sia nullo e non sia già presente.
     * 
     * @param articolo l'articolo da aggiungere
     */
    public void aggiungiArticolo(Articolo articolo) {
        if (articolo != null && !contenitoreArticolo.contains(articolo)) {
            contenitoreArticolo.add(articolo);
        }
    }

    /**
     * Cancella logicamente un articolo, rimuovendolo dagli articoli attivi
     * e spostandolo nel cestino (se non già presente).
     * 
     * @param articolo l'articolo da cancellare
     */
    public void cancellaArticolo(Articolo articolo) {
        if (articolo != null) {
            contenitoreArticolo.remove(articolo);
            if (!articoliCancellati.contains(articolo)) {
                articoliCancellati.add(articolo);
            }
        }
    }

    /**
     * Ripristina un articolo dal cestino, rimuovendolo dagli articoli cancellati
     * e reinserendolo tra gli articoli attivi (se non già presente).
     * 
     * @param articolo l'articolo da ripristinare
     */
    public void ripristinaArticolo(Articolo articolo) {
        if (articolo != null) {
            articoliCancellati.remove(articolo);
            if (!contenitoreArticolo.contains(articolo)) {
                contenitoreArticolo.add(articolo);
            }
        }
    }

    /**
     * Svuota definitivamente il cestino eliminando tutti gli articoli cancellati logicamente.
     */
    public void svuotaCancellati() {
        articoliCancellati.clear();
    }

    /**
     * Calcola la somma dei prezzi di tutti gli articoli attualmente attivi nella lista.
     * 
     * @return il prezzo totale degli articoli attivi
     */
    public double calcolaPrezzoTotale() {
        double totale = 0.0;
        for (Articolo a : contenitoreArticolo) {
            totale += a.getPrezzo();
        }
        return totale;
    }

    /**
     * Cerca un articolo tramite un prefisso testuale. 
     * La ricerca avviene in modo case-insensitive, prima tra gli articoli attivi e poi tra quelli cancellati.
     * 
     * @param prefisso il prefisso della nota dell'articolo da cercare
     * @return l'articolo corrispondente al prefisso
     * @throws ElementoNonTrovatoException se nessun articolo corrisponde al prefisso fornito
     */
    public Articolo cercaArticoloPerPrefisso(String prefisso) throws ElementoNonTrovatoException {
        if (prefisso == null || prefisso.trim().isEmpty()) {
            return null;
        }
        
        String pref = prefisso.trim().toLowerCase();
        for (Articolo a : contenitoreArticolo) {
            if (a.getNota().toLowerCase().startsWith(pref)) {
                return a;
            }
        }
        for (Articolo a : articoliCancellati) {
            if (a.getNota().toLowerCase().startsWith(pref)) {
                return a;
            }
        }
        
        throw new ElementoNonTrovatoException("Articolo non trovato.");
    }

    /**
     * Cerca un articolo specifico all'interno del cestino (articoli cancellati) confrontandone la nota.
     * La ricerca avviene in modo case-insensitive.
     * 
     * @param nota il nome (nota) esatto dell'articolo da cercare
     * @return l'articolo trovato, oppure null se non è presente nel cestino
     */
    public Articolo cercaNeiCancellati(String nota) {
        if (nota == null || nota.trim().isEmpty()) {
            return null;
        }
        String n = nota.trim().toLowerCase();
        for (Articolo a : articoliCancellati) {
            if (a.getNota().toLowerCase().equals(n)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Restituisce un iteratore che scorre tutti gli elementi gestiti dalla classe:
     * prima gli articoli attivi e successivamente quelli presenti nel cestino.
     * 
     * @return un iteratore per scorrere l'intera collezione di articoli
     */
    @Override
    public Iterator<Articolo> iterator() {
        List<Articolo> tutti = new ArrayList<>(contenitoreArticolo);
        tutti.addAll(articoliCancellati);
        return tutti.iterator();
    }
}