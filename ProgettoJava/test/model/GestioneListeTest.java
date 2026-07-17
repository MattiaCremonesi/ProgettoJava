package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import model.exception.ElementoNonTrovatoException;

/**
 * Classe di test per verificare il corretto funzionamento della
 * classe GestioneListe. Si occupa di testare la creazione, 
 * l'eliminazione e la ricerca delle entità principali del dominio.
 * 
 * @author Davide Aime e Mattia Cremonesi
 */
public class GestioneListeTest {

    /**
     * Resetta lo stato globale dell'applicazione prima di ogni test.
     */
    @BeforeEach
    public void setUp() {
        GestioneListe.svuotaTutto();
    }

    /**
     * Verifica la creazione di una nuova lista della spesa
     * e la sua successiva rimozione dall'archivio.
     */
    @Test
    public void testGestioneListeECreazione() {
        Categoria cat = new Categoria("Spesa");
        Articolo  art = new Articolo(cat, 3.0, "Latte");
        
        GestioneListe.creaLista("Spesa Lunedì", art);
        
        assertEquals(1, GestioneListe.getListeArticoli().size());
        assertNotNull(GestioneListe.getListeArticoli().get("Spesa Lunedì"));
        
        GestioneListe.cancellaLista("Spesa Lunedì");
        
        assertEquals(0, GestioneListe.getListeArticoli().size());
    }

    /**
     * Testa l'inserimento di categorie e articoli negli archivi globali
     * e il loro corretto recupero tramite ricerca per nome.
     * 
     * @throws ElementoNonTrovatoException se la ricerca fallisce inaspettatamente
     */
    @Test
    public void testGestioneCategorieEArticoli() throws ElementoNonTrovatoException {
        Categoria cat = new Categoria("Alimentari");
        Articolo  art = new Articolo(cat, 1.50, "Pane");
        
        GestioneListe.inserisciCategoria(cat);
        GestioneListe.inserisciArticolo(art);
        
        assertEquals(1, GestioneListe.getCategorie().size());
        assertEquals(1, GestioneListe.getArticoli().size());
        
        assertEquals(cat, GestioneListe.cercaCategoriaGlobale("Alimentari"));
        assertEquals(art, GestioneListe.cercaArticoloGlobale("Pane"));
    }

    /**
     * Verifica che vengano sollevate le eccezioni corrette 
     * in caso di ricerca di elementi non esistenti.
     */
    @Test
    public void testEccezioniRicercheFallite() {
        assertThrows(ElementoNonTrovatoException.class, () -> {
            GestioneListe.cercaCategoriaGlobale("Inesistente");
        });
        
        assertThrows(ElementoNonTrovatoException.class, () -> {
            GestioneListe.cercaArticoloGlobale("Inesistente");
        });
    }

    /**
     * Verifica che l'eliminazione globale di un articolo lo sposti
     * automaticamente nel cestino delle liste a cui appartiene (cancellazione a cascata).
     */
    @Test
    public void testCancellazioneArticoloInCascata() {
        Categoria       cat   = new Categoria("Cibo");
        Articolo        art   = new Articolo(cat, 2.0, "Biscotti");
        
        GestioneListe.inserisciArticolo(art);
        GestioneListe.creaLista("Lista 1", art);
        
        ListaDiArticoli lista = GestioneListe.getListeArticoli().get("Lista 1");
        
        assertEquals(1, lista.getContenitoreArticolo().size());
        
        GestioneListe.cancellaArticolo(art);
        
        assertEquals(0, GestioneListe.getArticoli().size());
        assertEquals(0, lista.getContenitoreArticolo().size());
        assertEquals(1, lista.getArticoliCancellati().size());
    }
}