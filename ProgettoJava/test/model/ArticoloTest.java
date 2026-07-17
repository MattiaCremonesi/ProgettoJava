package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe di test JUnit per verificare tutte le funzionalità della classe Articolo.
 * * @author Davide Aime E Mattia Cremonesi
 */

public class ArticoloTest {

    @Test
    public void testCostruttoreDatiValidi() {
        Categoria cat = new Categoria("Alimentari");
        Articolo art = new Articolo(cat, 2.50, "Pasta Barilla");
        
        assertEquals("Alimentari", art.getCategoria().getNome(), "La categoria dovrebbe essere Alimentari");
        assertEquals(2.50, art.getPrezzo(), 0.001, "Il prezzo dovrebbe essere 2.50");
        assertEquals("Pasta Barilla", art.getNota(), "La nota dovrebbe essere 'Pasta Barilla'");
    }

    @Test
    public void testCostruttoreValoriDefault() {
        Articolo art = new Articolo(null, 0, null);
        
        assertEquals("non categorizzato", art.getCategoria().getNome(), "Dovrebbe impostare la categoria di default");
        assertEquals(0, art.getPrezzo(), "Il prezzo di default dovrebbe essere 0");
        assertEquals("", art.getNota(), "La nota di default dovrebbe essere una stringa vuota");
    }

    @Test
    public void testModificaProprieta() {
        Categoria alimentari = new Categoria("Alimentari");
        Articolo art = new Articolo(alimentari, 1.00, "Pane");
        
        Categoria freschi = new Categoria("Freschi");
        art.setCategoria(freschi);
        art.setPrezzo(1.80);
        art.setNota("Pane Integrale");
        
        assertEquals("Freschi", art.getCategoria().getNome());
        assertEquals(1.80, art.getPrezzo(), 0.001);
        assertEquals("Pane Integrale", art.getNota());
    }
}