package model;

import java.util.Objects;

/**
 * Rappresenta una categoria merceologica da associare agli articoli della spesa.
 * Le categorie sono concepite per essere condivise globalmente all'interno
 * del sistema, evitando duplicazioni inutili in memoria.
 * * @author Davide Aime e Mattia Cremonesi
 */
public class Categoria {
    
    private final String nome;
    
    /**
     * Costruisce una nuova categoria a partire dal nome fornito.
     * Nel caso in cui il parametro sia nullo o composto solo da spazi vuoti,
     * il sistema applica automaticamente il valore di default previsto.
     * * @param nome la stringa che identifica la categoria
     */
    public Categoria(String nome) {
        this.nome = normalizzaNome(nome);
    }
    
    /**
     * Restituisce il nome identificativo della categoria.
     * * @return la stringa contenente il nome della categoria
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Pulisce la stringa in input o applica il valore di default.
     * * @param n il nome grezzo fornito
     * @return il nome normalizzato o di default
     */
    private String normalizzaNome(String n) {
        if (n == null || n.trim().isEmpty()) {
            return "Non categorizzato";
        }
        return n.trim();
    }

    /**
     * Verifica l'uguaglianza tra due categorie basandosi sul nome.
     * Questo metodo è vitale per garantire che le categorie siano 
     * correttamente condivise all'interno del sistema.
     * * @param obj l'oggetto da confrontare con questa categoria
     * @return true se le due categorie hanno lo stesso nome, false altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        Categoria altraCategoria = (Categoria) obj;
        return this.nome.equalsIgnoreCase(altraCategoria.nome);
    }

    /**
     * Calcola l'hash code della categoria basandosi sul suo nome in minuscolo,
     * in coerenza con il metodo equals.
     * * @return il valore intero di hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.nome.toLowerCase());
    }

    /**
     * Restituisce una rappresentazione testuale della categoria.
     * * @return il nome della categoria
     */
    @Override
    public String toString() {
        return this.nome;
    }
}