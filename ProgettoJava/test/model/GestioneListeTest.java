package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.exception.NumeroSbagliatoException;

/**
 * Classe di test JUnit per verificare le funzionalità di GestioneListe.
 * * @author IlTuoNome E IlNomeDelCompagno
 */
public class GestioneListeTest {

    @BeforeEach
    public void setUp() {
        // Prima di ogni test svuotiamo lo stato statico globale per non inquinare i test successivi
        GestioneListe.svuotaTutto();
    }

    @Test
    public void testGestioneListeECreazione() {
        Categoria cat = new Categoria("Spesa");
        Articolo art = new Articolo(cat, 3.0, "Latte");
        
        // Creazione di una lista
        GestioneListe.creaLista("Spesa Lunedì", art);
        assertEquals(1, GestioneListe.getListeArticoli().size());
        assertNotNull(GestioneListe.getListeArticoli().get("Spesa Lunedì"));
        
        // Cancellazione della lista
        GestioneListe.cancellaLista("Spesa Lunedì");
        assertEquals(0, GestioneListe.getListeArticoli().size());
    }

    @Test
    public void testGestioneCategorieEArticoli() throws NumeroSbagliatoException {
        Categoria cat = new Categoria("Alimentari");
        Articolo art = new Articolo(cat, 1.50, "Pane");
        
        // Test inserimenti globali
        GestioneListe.inserisciCategoria(cat);
        GestioneListe.inserisciArticolo(art);
        
        assertEquals(1, GestioneListe.getCategorie().size());
        assertEquals(1, GestioneListe.getArticoli().size());
        
        // Test ricerche globali con successo
        assertEquals(cat, GestioneListe.cercaCategoriaGlobale("Alimentari"));
        assertEquals(art, GestioneListe.cercaArticoloGlobale("Pane"));
    }

    @Test
    public void testEccezioniRicercheFallite() {
        // Verifica che la ricerca di elementi inesistenti lanci l'eccezione corretta
        assertThrows(NumeroSbagliatoException.class, () -> {
            GestioneListe.cercaCategoriaGlobale("Inesistente");
        });
        
        assertThrows(NumeroSbagliatoException.class, () -> {
            GestioneListe.cercaArticoloGlobale("Inesistente");
        });
    }

    @Test
    public void testCancellazioneArticoloInCascata() {
        Categoria cat = new Categoria("Cibo");
        Articolo art = new Articolo(cat, 2.0, "Biscotti");
        
        GestioneListe.inserisciArticolo(art);
        GestioneListe.creaLista("Lista 1", art);
        
        // Verifichiamo che inizialmente l'articolo sia nella lista attiva
        ListaDiArticoli lista = GestioneListe.getListeArticoli().get("Lista 1");
        assertEquals(1, lista.getArticoliAttivi().size());
        
        // Cancellando l'articolo globalmente, deve sparire dai globali ed entrare nei CANCELLATI della lista
        GestioneListe.cancellaArticolo(art);
        assertEquals(0, GestioneListe.getArticoli().size());
        assertEquals(0, lista.getArticoliAttivi().size());
        assertEquals(1, lista.getArticoliCancellati().size(), "L'articolo doveva spostarsi nei cancellati della lista");
    }
}