package model.exception;

/**
 * Eccezione personalizzata utilizzata all'interno del sistema per segnalare
 * situazioni in cui un valore numerico fornito non rispetti i criteri 
 * di validità attesi o non sia coerente con le operazioni richieste.
 * * @author Davide Aime e Mattia Cremonesi
 */
public class NumeroSbagliatoException extends Exception {

    /**
     * Costruisce una nuova eccezione con un messaggio predefinito 
     * che segnala genericamente l'anomalia sul valore numerico.
     */
    public NumeroSbagliatoException() {
        super("Il valore numerico inserito non è valido per questa operazione.");
    }

    /**
     * Costruisce una nuova eccezione contenente un messaggio specifico 
     * volto a descrivere nel dettaglio la causa dell'errore.
     * * @param messaggio la descrizione dettagliata dell'errore riscontrato
     */
    public NumeroSbagliatoException(String messaggio) {
        super(messaggio);
    }
}