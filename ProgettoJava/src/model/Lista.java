package model;

import java.util.ArrayList;

public class Lista {

	String nome;
	
	ArrayList<Articolo> ContenitoreArticolo = new ArrayList();
	
	public Lista (String nome, Articolo articolo) {
		this.nome = nome;
		if (articolo == null) {			 
			ContenitoreArticolo.add(articolo);
		}
	}
	
}
