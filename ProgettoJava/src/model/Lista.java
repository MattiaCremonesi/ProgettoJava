package model;

import java.util.ArrayList;

public class Lista {

	String nome;
	
	ArrayList<Articolo> ContenitoreArticolo = new ArrayList();
	
	Lista (String nome, Articolo articolo) {
		this.nome = nome;
		ContenitoreArticolo.add(articolo);
	}
	
}
