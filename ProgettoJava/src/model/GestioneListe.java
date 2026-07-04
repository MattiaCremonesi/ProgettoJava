package model;

import java.util.ArrayList;
import java.util.HashMap;

public class GestioneListe {

	public static HashMap<String, ListaDiArticoli> listeArticoli = new HashMap<>();
	
	public static ArrayList<Categoria> categorie = new ArrayList<>(); 
	
	public static ArrayList<Articolo> articoli = new ArrayList<>();
	
	public static void CreaLista (String nome, Articolo articoloIniziale) {
		ListaDiArticoli nuovaLista = new ListaDiArticoli(nome, articoloIniziale);
		listeArticoli.put(nome, nuovaLista);
	}
	
	public static void CancellaLista (String nome) {
		listeArticoli.remove(nome);
	}
	
	public static void InserisciCategoria (Categoria categoria) {
		categorie.add(categoria);
	}
	
	public static void CancellaCategoria (Categoria categoria) {
		categorie.remove(categoria);
	}
	
	public static void InserisciArticolo (Articolo articolo) {
		articoli.add(articolo);
	}
	
	public static void CancellaArticolo (Articolo articolo) {
		articoli.remove(articolo);
	}
	
	
}
