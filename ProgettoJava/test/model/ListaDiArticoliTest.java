package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;

import model.exception.ElementoNonTrovatoException;

/**
 * Classe di test per verificare il corretto comportamento 
 * delle funzionalità offerte dalla classe ListaDiArticoli.
 * 
 * @author Davide Aime e Mattia Cremonesi
 */
public class ListaDiArticoliTest {

    /**
     * Verifica che il costruttore della lista assegni correttamente il nome 
     * e inizializzi il contenitore degli articoli.
     */
    @Test
    public void testCostruttoreENomeLista() {
        Categoria       cat          = new Categoria("Alimentari");
        Articolo        art          = new Articolo(cat, 1.50, "Pane");
        ListaDiArticoli listaValida  = new ListaDiArticoli("Spesa Sabato", art);
        ListaDiArticoli listaDefault = new ListaDiArticoli("   ", null);

        assertEquals("Spesa Sabato", listaValida.getNome());
        assertEquals(1, listaValida.getContenitoreArticolo().size());
        
        assertEquals("Nuova Lista", listaDefault.getNome());
        assertEquals(0, listaDefault.getContenitoreArticolo().size());
    }

    /**
     * Verifica l'aggiunta di un articolo e la successiva cancellazione logica.
     */
    @Test
    public void testAggiungiECancellaArticolo() {
        Categoria       cat   = new Categoria("Varie");
        Articolo        art   = new Articolo(cat, 5.0, "Detersivo");
        ListaDiArticoli lista = new ListaDiArticoli("Mia Lista", null);
        
        lista.aggiungiArticolo(art);
        assertEquals(1, lista.getContenitoreArticolo().size());
        assertTrue(lista.getContenitoreArticolo().contains(art));
        
        lista.cancellaArticolo(art);
        assertEquals(0, lista.getContenitoreArticolo().size());
        assertEquals(1, lista.getArticoliCancellati().size());
        assertTrue(lista.getArticoliCancellati().contains(art));
    }

    /**
     * Verifica il ripristino di un articolo precedentemente cancellato.
     */
    @Test
    public void testRipristinaArticolo() {
        Categoria       cat   = new Categoria("Frutta");
        Articolo        art   = new Articolo(cat, 2.30, "Mele");
        ListaDiArticoli lista = new ListaDiArticoli("Spesa", art);
        
        lista.cancellaArticolo(art);
        assertEquals(1, lista.getArticoliCancellati().size());
        
        lista.ripristinaArticolo(art);
        assertEquals(1, lista.getContenitoreArticolo().size());
        assertEquals(0, lista.getArticoliCancellati().size());
        assertTrue(lista.getContenitoreArticolo().contains(art));
    }

    /**
     * Verifica lo svuotamento definitivo del cestino degli articoli.
     */
    @Test
    public void testSvuotaCancellati() {
        Categoria       cat   = new Categoria("Cibo");
        Articolo        art   = new Articolo(cat, 1.20, "Acqua");
        ListaDiArticoli lista = new ListaDiArticoli("Spesa", art);
        
        lista.cancellaArticolo(art);
        assertEquals(1, lista.getArticoliCancellati().size());
        
        lista.svuotaCancellati();
        assertEquals(0, lista.getArticoliCancellati().size());
    }

    /**
     * Verifica il calcolo del prezzo totale per i soli articoli attivi.
     */
    @Test
    public void testCalcolaPrezzoTotale() {
        Categoria       cat   = new Categoria("Spesa");
        Articolo        a1    = new Articolo(cat, 2.50, "Pasta");
        Articolo        a2    = new Articolo(cat, 3.50, "Sugo");
        ListaDiArticoli lista = new ListaDiArticoli("Pranzo", a1);
        
        lista.aggiungiArticolo(a2);
        assertEquals(6.00, lista.calcolaPrezzoTotale(), 0.001);
        
        lista.cancellaArticolo(a2);
        assertEquals(2.50, lista.calcolaPrezzoTotale(), 0.001);
    }

    /**
     * Verifica la ricerca di un articolo tramite prefisso.
     * 
     * @throws ElementoNonTrovatoException se l'articolo non viene trovato
     */
    @Test
    public void testCercaArticoloPerPrefisso() throws ElementoNonTrovatoException {
        Categoria       cat   = new Categoria("Bevande");
        Articolo        a1    = new Articolo(cat, 1.80, "Coca Cola");
        Articolo        a2    = new Articolo(cat, 4.50, "Vino Rosso");
        ListaDiArticoli lista = new ListaDiArticoli("Festa", a1);
        
        lista.aggiungiArticolo(a2);
        lista.cancellaArticolo(a2);
        
        Articolo trovato1 = lista.cercaArticoloPerPrefisso("cOcA");
        assertNotNull(trovato1);
        assertEquals("Coca Cola", trovato1.getNota());
        
        Articolo trovato2 = lista.cercaArticoloPerPrefisso("vin");
        assertNotNull(trovato2);
        assertEquals("Vino Rosso", trovato2.getNota());
    }

    /**
     * Verifica il corretto scorrimento dell'iteratore personalizzato.
     */
    @Test
    public void testIteratorePersonalizzato() {
        Categoria          cat   = new Categoria("Gen");
        Articolo           a1    = new Articolo(cat, 1.0, "Primo Attivo");
        Articolo           a2    = new Articolo(cat, 2.0, "Secondo Attivo");
        Articolo           c1    = new Articolo(cat, 3.0, "Primo Cancellato");
        ListaDiArticoli    lista = new ListaDiArticoli("Test Iteratore", a1);
        
        lista.aggiungiArticolo(a2);
        lista.aggiungiArticolo(c1);
        lista.cancellaArticolo(c1);
        
        Iterator<Articolo> it    = lista.iterator();
        
        assertTrue(it.hasNext());
        assertEquals("Primo Attivo", it.next().getNota());
        
        assertTrue(it.hasNext());
        assertEquals("Secondo Attivo", it.next().getNota());
        
        assertTrue(it.hasNext());
        assertEquals("Primo Cancellato", it.next().getNota());
        
        assertFalse(it.hasNext());
    }
    
    /**
     * Verifica la ricerca specifica all'interno degli articoli cancellati.
     * 
     * @throws ElementoNonTrovatoException se l'articolo non viene trovato
     */
    @Test
    public void testCercaNeiCancellati() throws ElementoNonTrovatoException {
        Categoria       cat   = new Categoria("Alimentari");
        Articolo        art1  = new Articolo(cat, 1.50, "Pasta");
        Articolo        art2  = new Articolo(cat, 2.00, "Sugo");
        ListaDiArticoli lista = new ListaDiArticoli("Spesa", art1);
        
        lista.aggiungiArticolo(art2);
        lista.cancellaArticolo(art1);
        
        Articolo trovato = lista.cercaNeiCancellati("  pAsTa  ");
        assertNotNull(trovato);
        assertEquals("Pasta", trovato.getNota());
    }
}