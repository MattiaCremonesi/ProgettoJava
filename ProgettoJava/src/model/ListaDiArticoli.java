package model;

import java.util.ArrayList;

public class ListaDiArticoli {

	String nome;
	
	ArrayList<Articolo> ContenitoreArticolo = new ArrayList();
	
	public ListaDiArticoli (String nome, Articolo articolo) {
		this.nome = nome;
		if (articolo == null) {			 
			ContenitoreArticolo.add(articolo);
		}
	}
	
	public String getListaNome () {
		return this.nome;
	}
	
}
