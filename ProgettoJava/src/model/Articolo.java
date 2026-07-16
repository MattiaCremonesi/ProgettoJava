package model;

/**
 * Modello che rappresenta un singolo articolo all'interno della lista della spesa.
 * Incapsula le informazioni fondamentali quali la categoria di appartenenza, 
 * il prezzo e un'eventuale nota testuale aggiuntiva.
 * * @author Davide Aime e Mattia Cremonesi
 */
public class Articolo {

    private Categoria categoria;
    private double    prezzo;
    private String    nota;
    
    /**
     * Costruisce un nuovo articolo inizializzandolo con i dati forniti.
     * Nel caso in cui i parametri risultino nulli o non validi, vengono 
     * applicati i valori di default previsti dalle specifiche di progetto.
     * * @param categoria la categoria assegnata; se null, diventa di default
     * @param prezzo    il costo dell'articolo; se negativo, viene forzato a 0.0
     * @param nota      informazioni aggiuntive; se null o vuota, viene svuotata
     */
    public Articolo(Categoria categoria, double prezzo, String nota) {
        this.categoria = normalizzaCategoria(categoria);
        this.prezzo    = normalizzaPrezzoIniziale(prezzo);
        this.nota      = normalizzaNota(nota);
    }   

    /**
     * Restituisce la nota associata all'articolo.
     * * @return la stringa contenente la nota (vuota se non specificata)
     */
    public String getNota() {
        return this.nota;
    }

    /**
     * Restituisce il costo registrato per l'articolo.
     * * @return il valore numerico del prezzo
     */
    public double getPrezzo() {
        return this.prezzo;
    }
    
    /**
     * Restituisce l'oggetto che rappresenta la categoria di questo articolo.
     * * @return la categoria corrente
     */
    public Categoria getCategoria() {
        return this.categoria;
    }
    
    /**
     * Aggiorna la nota testuale dell'articolo.
     * * @param nota la nuova nota (i valori nulli sono convertiti in vuoti)
     */
    public void setNota(String nota) {
        this.nota = normalizzaNota(nota);
    }
    
    /**
     * Modifica il prezzo dell'articolo. 
     * * @param prezzo il nuovo prezzo da assegnare
     * @throws IllegalArgumentException se il prezzo fornito è negativo
     */
    public void setPrezzo(double prezzo) {
        if (prezzo < 0) {
            throw new IllegalArgumentException("Errore: il prezzo non può essere negativo.");
        }
        this.prezzo = prezzo;
    }
    
    /**
     * Modifica la categoria di appartenenza dell'articolo.
     * * @param categoria la nuova categoria (se null, ripristina il default)
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = normalizzaCategoria(categoria);
    }

    /**
     * Pulisce o sostituisce la categoria se fornito un valore nullo.
     * * @param c la categoria da valutare
     * @return la categoria normalizzata o quella di default
     */
    private Categoria normalizzaCategoria(Categoria c) {
        if (c == null) {
            return new Categoria("Non categorizzato");
        }
        return c;
    }

    /**
     * Verifica se il prezzo iniziale è negativo e lo azzera se necessario.
     * * @param p il prezzo da valutare
     * @return il prezzo corretto
     */
    private double normalizzaPrezzoIniziale(double p) {
        if (p < 0) {
            return 0.0;
        }
        return p;
    }

    /**
     * Elimina gli spazi vuoti o converte la nota in stringa vuota.
     * * @param n la nota da elaborare
     * @return la nota ripulita
     */
    private String normalizzaNota(String n) {
        if (n == null || n.trim().isEmpty()) {
            return "";
        }
        return n.trim();
    }
    
    /**
     * Fornisce una rappresentazione testuale chiara e formattata dell'articolo.
     * * @return una stringa descrittiva contenente nota, categoria e prezzo
     */
    @Override
    public String toString() {
        return "Nota: " + this.nota + 
               ", Categoria: " + this.categoria.toString() + 
               ", Prezzo: " + this.prezzo + "€";
    }
}