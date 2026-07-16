package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import model.exception.ElementoNonTrovatoException;

/**
 * Classe di test JUnit pensata per verificare il corretto funzionamento della
 * classe manager statico GestioneListe. Si occupa di testare la creazione, 
 * l'eliminazione e la ricerca delle entità principali del dominio (Liste, 
 * Categorie e Articoli), garantendo il rispetto dei vincoli architetturali.
 * 
 * @author Davide Aime e Mattia Cremonesi
 */
public class GestioneListeTest {

    /**
     * Metodo di preparazione (Setup) eseguito automaticamente prima di ogni 
     * singolo test. Ha il compito fondamentale di resettare lo stato globale 
     * dell'applicazione, evitando che i dati "sporchi" di un test influenzino 
     * l'esito dei successivi (isolamento dei test).
     */
    @BeforeEach
    public void setUp() {
        GestioneListe.svuotaTutto();
    }

    /**
     * Verifica la corretta instanziazione di una nuova lista della spesa
     * e la sua successiva rimozione dall'archivio centrale, controllando
     * l'aggiornamento coerente delle dimensioni della mappa.
     */
    @Test
    public void testGestioneListeECreazione() {
        Categoria cat = new Categoria("Spesa");
        Articolo  art = new Articolo(cat, 3.0, "Latte");
        
        // FASE 1: Creazione e salvataggio della lista
        GestioneListe.creaLista("Spesa Lunedì", art);
        
        assertEquals(1, GestioneListe.getListeArticoli().size());
        assertNotNull(GestioneListe.getListeArticoli().get("Spesa Lunedì"));
        
        // FASE 2: Eliminazione e verifica della rimozione effettiva
        GestioneListe.cancellaLista("Spesa Lunedì");
        
        assertEquals(0, GestioneListe.getListeArticoli().size());
    }

    /**
     * Testa l'inserimento di categorie e articoli all'interno degli archivi
     * globali, verificando poi che i metodi di ricerca per nome restituiscano
     * esattamente le istanze appena inserite.
     * 
     * @throws ElementoNonTrovatoException se la ricerca fallisce inaspettatamente
     */
    @Test
    public void testGestioneCategorieEArticoli() throws ElementoNonTrovatoException {
        Categoria cat = new Categoria("Alimentari");
        Articolo  art = new Articolo(cat, 1.50, "Pane");
        
        // FASE 1: Inserimenti globali
        GestioneListe.inserisciCategoria(cat);
        GestioneListe.inserisciArticolo(art);
        
        assertEquals(1, GestioneListe.getCategorie().size());
        assertEquals(1, GestioneListe.getArticoli().size());
        
        // FASE 2: Ricerche globali con successo
        assertEquals(cat, GestioneListe.cercaCategoriaGlobale("Alimentari"));
        assertEquals(art, GestioneListe.cercaArticoloGlobale("Pane"));
    }

    /**
     * Verifica il comportamento difensivo della classe, assicurandosi che 
     * il sistema sollevi l'eccezione corretta quando si tenta di ricercare 
     * elementi non registrati negli archivi.
     */
    @Test
    public void testEccezioniRicercheFallite() {
        // Verifica sulla ricerca delle Categorie
        assertThrows(ElementoNonTrovatoException.class, () -> {
            GestioneListe.cercaCategoriaGlobale("Inesistente");
        });
        
        // Verifica sulla ricerca degli Articoli
        assertThrows(ElementoNonTrovatoException.class, () -> {
            GestioneListe.cercaArticoloGlobale("Inesistente");
        });
    }

    /**
     * Controlla il meccanismo di rimozione logica (cancellazione a cascata).
     * Se un articolo viene cancellato globalmente, non deve sparire 
     * definitivamente, ma deve essere spostato nel cestino (cancellati) 
     * delle liste a cui appartiene.
     */
    @Test
    public void testCancellazioneArticoloInCascata() {
        Categoria cat = new Categoria("Cibo");
        Articolo  art = new Articolo(cat, 2.0, "Biscotti");
        
        GestioneListe.inserisciArticolo(art);
        GestioneListe.creaLista("Lista 1", art);
        
        ListaDiArticoli lista = GestioneListe.getListeArticoli().get("Lista 1");
        
        // Verifichiamo che inizialmente l'articolo sia tra gli elementi attivi
        assertEquals(1, lista.getContenitoreArticolo().size());
        
        // Cancellando l'articolo, deve migrare dai globali al cestino della lista
        GestioneListe.cancellaArticolo(art);
        
        assertEquals(0, GestioneListe.getArticoli().size());
        assertEquals(0, lista.getContenitoreArticolo().size());
        
        assertEquals(
            1, 
            lista.getArticoliCancellati().size(), 
            "L'articolo non è stato spostato correttamente nei cancellati."
        );
    }
}