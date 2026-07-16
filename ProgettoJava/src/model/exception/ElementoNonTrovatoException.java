package model.exception;

/**
 * Eccezione personalizzata sollevata quando un elemento ricercato
 * non è presente negli archivi di sistema.
 * * @author Davide Aime e Mattia Cremonesi
 */
public class ElementoNonTrovatoException extends Exception {

    /**
     * Costruisce una nuova eccezione con un messaggio di errore predefinito.
     */
    public ElementoNonTrovatoException() {
        super("L'elemento richiesto non è stato trovato all'interno del sistema.");
    }

    /**
     * Costruisce una nuova eccezione con un messaggio di errore specifico.
     * * @param messaggio il dettaglio dell'errore da comunicare
     */
    public ElementoNonTrovatoException(String messaggio) {
        super(messaggio);
    }
}