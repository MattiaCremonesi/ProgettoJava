package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CategoriaTest {

    @Test
    public void testCostruttoreNomeValido() {
        Categoria cat = new Categoria("Alimentari");
        assertEquals("Alimentari", cat.getNome(), "Il nome della categoria dovrebbe essere 'Alimentari'");
    }

    @Test
    public void testCostruttoreValoreDefault() {
        
        Categoria catNull = new Categoria(null);
        Categoria catVuota = new Categoria("   ");
        
        assertEquals("non categorizzato", catNull.getNome(), "Dovrebbe impostare il nome di default se null");
        assertEquals("non categorizzato", catVuota.getNome(), "Dovrebbe impostare il nome di default se vuoto");
    }
}