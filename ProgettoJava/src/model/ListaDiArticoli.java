package model;

import java.util.ArrayList;
import java.util.Iterator;

public class ListaDiArticoli implements Iterable<Articolo> {

	String nome;
	
	private ArrayList<Articolo> contenitoreArticolo = new ArrayList<>();
	private ArrayList<Articolo> articoliCancellati = new ArrayList<>();
	
	public ListaDiArticoli (String nome, Articolo articolo) {
		this.nome = nome;
		if (articolo != null) {			 
			contenitoreArticolo.add(articolo);
		}
	}
	
	public String getListaNome () {
		return this.nome;
	}
	
	public void cancellaArticolo (Articolo a) {
		contenitoreArticolo.remove (a);
		if (contenitoreArticolo.remove (a)) {
			articoliCancellati.add (a);
		}
	}
	
	public void aggiungiArticolo (Articolo a) {
		if (a != null) {
			contenitoreArticolo.add(a);
		}
	}

	@Override
	public Iterator<Articolo> iterator() {
		return contenitoreArticolo.iterator();
	}
	
}
