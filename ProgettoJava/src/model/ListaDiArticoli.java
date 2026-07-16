package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.exception.ElementoNonTrovatoException;

public class ListaDiArticoli implements Iterable<Articolo> {

    private String nome;
    private List<Articolo> contenitoreArticolo = new ArrayList<>();
    private List<Articolo> articoliCancellati = new ArrayList<>();
    
    public ListaDiArticoli(String nome, Articolo articolo) {
        setNome(nome);
        if (articolo != null) {             
            contenitoreArticolo.add(articolo);
        }
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            this.nome = "Nuova Lista";
        } else {
            this.nome = nome.trim();
        }
    }
    
    public String getNome() {
        return this.nome;
    }

    public List<Articolo> getContenitoreArticolo() {
        return this.contenitoreArticolo;
    }

    public List<Articolo> getArticoliCancellati() {
        return this.articoliCancellati;
    }

    public void aggiungiArticolo(Articolo articolo) {
        if (articolo != null && !contenitoreArticolo.contains(articolo)) {
            contenitoreArticolo.add(articolo);
        }
    }

    public void cancellaArticolo(Articolo articolo) {
        if (articolo != null) {
            contenitoreArticolo.remove(articolo);
            if (!articoliCancellati.contains(articolo)) {
                articoliCancellati.add(articolo);
            }
        }
    }

    public void ripristinaArticolo(Articolo articolo) {
        if (articolo != null) {
            articoliCancellati.remove(articolo);
            if (!contenitoreArticolo.contains(articolo)) {
                contenitoreArticolo.add(articolo);
            }
        }
    }

    public void svuotaCancellati() {
        articoliCancellati.clear();
    }

    public double calcolaPrezzoTotale() {
        double totale = 0.0;
        for (Articolo a : contenitoreArticolo) {
            totale += a.getPrezzo();
        }
        return totale;
    }

    public Articolo cercaArticoloPerPrefisso(String prefisso) throws ElementoNonTrovatoException {
        if (prefisso == null || prefisso.trim().isEmpty()) {
            return null;
        }
        
        String pref = prefisso.trim().toLowerCase();
        for (Articolo a : contenitoreArticolo) {
            if (a.getNota().toLowerCase().startsWith(pref)) {
                return a;
            }
        }
        for (Articolo a : articoliCancellati) {
            if (a.getNota().toLowerCase().startsWith(pref)) {
                return a;
            }
        }
        
        throw new ElementoNonTrovatoException("Articolo non trovato.");
    }

    public Articolo cercaNeiCancellati(String nota) {
        if (nota == null || nota.trim().isEmpty()) {
            return null;
        }
        String n = nota.trim().toLowerCase();
        for (Articolo a : articoliCancellati) {
            if (a.getNota().toLowerCase().equals(n)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public Iterator<Articolo> iterator() {
        List<Articolo> tutti = new ArrayList<>(contenitoreArticolo);
        tutti.addAll(articoliCancellati);
        return tutti.iterator();
    }
}