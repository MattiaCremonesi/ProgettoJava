package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Classe di test JUnit per verificare tutte le funzionalità della classe ListaDiArticoli.
 * * @author IlTuoNome E IlNomeDelCompagno
 */
public class ListaDiArticoliTest {

    @Test
    public void testCostruttoreENomeLista() {
        Categoria cat = new Categoria("Alimentari");
        Articolo art = new Articolo(cat, 1.50, "Pane");
        
        // Test con nome valido
        ListaDiArticoli listaValida = new ListaDiArticoli("Spesa Sabato", art);
        assertEquals("Spesa Sabato", listaValida.getListaNome());
        assertEquals(1, listaValida.getArticoliAttivi().size());
        
        // Test con nome nullo o vuoto (valori di default)
        ListaDiArticoli listaDefault = new ListaDiArticoli("   ", null);
        assertEquals("Nuova Lista", listaDefault.getListaNome());
        assertEquals(0, listaDefault.getArticoliAttivi().size());
    }

    @Test
    public void testAggiungiECancellaArticolo() {
        Categoria cat = new Categoria("Varie");
        Articolo art = new Articolo(cat, 5.0, "Detersivo");
        ListaDiArticoli lista = new ListaDiArticoli("Mia Lista", null);
        
        // Verifica aggiunta
        lista.aggiungiArticolo(art);
        assertEquals(1, lista.getArticoliAttivi().size());
        assertTrue(lista.getArticoliAttivi().contains(art));
        
        // Verifica cancellazione (spostamento nel cestino)
        lista.cancellaArticolo(art);
        assertEquals(0, lista.getArticoliAttivi().size());
        assertEquals(1, lista.getArticoliCancellati().size());
        assertTrue(lista.getArticoliCancellati().contains(art));
    }

    @Test
    public void testRipristinaArticolo() {
        Categoria cat = new Categoria("Frutta");
        Articolo art = new Articolo(cat, 2.30, "Mele");
        ListaDiArticoli lista = new ListaDiArticoli("Spesa", art);
        
        // Lo cancelliamo
        lista.cancellaArticolo(art);
        assertEquals(1, lista.getArticoliCancellati().size());
        
        // Lo ripristiniamo
        lista.ripristinaArticolo(art);
        assertEquals(1, lista.getArticoliAttivi().size());
        assertEquals(0, lista.getArticoliCancellati().size());
        assertTrue(lista.getArticoliAttivi().contains(art));
    }

    @Test
    public void testSvuotaCancellati() {
        Categoria cat = new Categoria("Cibo");
        Articolo art = new Articolo(cat, 1.20, "Acqua");
        ListaDiArticoli lista = new ListaDiArticoli("Spesa", art);
        
        lista.cancellaArticolo(art);
        assertEquals(1, lista.getArticoliCancellati().size());
        
        // Svuotiamo il cestino definitivamente
        lista.svuotaCancellati();
        assertEquals(0, lista.getArticoliCancellati().size(), "Il cestino dovrebbe essere vuoto dopo lo svuotamento");
    }

    @Test
    public void testCalcolaPrezzoTotale() {
        Categoria cat = new Categoria("Spesa");
        Articolo a1 = new Articolo(cat, 2.50, "Pasta");
        Articolo a2 = new Articolo(cat, 3.50, "Sugo");
        
        ListaDiArticoli lista = new ListaDiArticoli("Pranzo", a1);
        lista.aggiungiArticolo(a2);
        
        // Il totale iniziale deve essere 2.50 + 3.50 = 6.00
        assertEquals(6.00, lista.calcolaPrezzoTotale(), 0.001);
        
        // Se cancelliamo un articolo, non deve più essere conteggiato nel totale attivo
        lista.cancellaArticolo(a2);
        assertEquals(2.50, lista.calcolaPrezzoTotale(), 0.001);
    }

    @Test
    public void testCercaArticoloPerPrefisso() {
        Categoria cat = new Categoria("Bevande");
        Articolo a1 = new Articolo(cat, 1.80, "Coca Cola");
        Articolo a2 = new Articolo(cat, 4.50, "Vino Rosso");
        
        ListaDiArticoli lista = new ListaDiArticoli("Festa", a1);
        lista.aggiungiArticolo(a2);
        lista.cancellaArticolo(a2); // a1 è attivo, a2 è nel cestino
        
        // 1. Ricerca case-insensitive per nota (attivo)
        Articolo trovato1 = lista.cercaArticoloPerPrefisso("cOcA");
        assertNotNull(trovato1);
        assertEquals("Coca Cola", trovato1.getNota());
        
        // 2. Ricerca per nota su un articolo cancellato
        Articolo trovato2 = lista.cercaArticoloPerPrefisso("vin");
        assertNotNull(trovato2);
        assertEquals("Vino Rosso", trovato2.getNota());
        
        // 3. Ricerca con prefisso inesistente
        assertNull(lista.cercaArticoloPerPrefisso("Inesistente"));
        
        // 4. Ricerca con stringa vuota o nulla
        assertNull(lista.cercaArticoloPerPrefisso(""));
        assertNull(lista.cercaArticoloPerPrefisso(null));
    }

    @Test
    public void testIteratorePersonalizzato() {
        Categoria cat = new Categoria("Gen");
        Articolo a1 = new Articolo(cat, 1.0, "Primo Attivo");
        Articolo a2 = new Articolo(cat, 2.0, "Secondo Attivo");
        Articolo c1 = new Articolo(cat, 3.0, "Primo Cancellato");
        
        ListaDiArticoli lista = new ListaDiArticoli("Test Iteratore", a1);
        lista.aggiungiArticolo(a2);
        lista.aggiungiArticolo(c1);
        lista.cancellaArticolo(c1); // Abbiamo 2 attivi e 1 cancellato
        
        Iterator<Articolo> it = lista.iterator();
        
        // Verifichiamo che passi in sequenza corretta
        assertTrue(it.hasNext());
        assertEquals("Primo Attivo", it.next().getNota());
        
        assertTrue(it.hasNext());
        assertEquals("Secondo Attivo", it.next().getNota());
        
        assertTrue(it.hasNext());
        assertEquals("Primo Cancellato", it.next().getNota());
        
        // Non dovrebbero esserci più elementi
        assertFalse(it.hasNext());
        
        // Chiamare next() quando hasNext() è false deve lanciare l'eccezione standard
        assertThrows(NoSuchElementException.class, () -> {
            it.next();
        });
    }
}