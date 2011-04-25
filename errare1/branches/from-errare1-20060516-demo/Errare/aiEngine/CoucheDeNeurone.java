/*Errare is a free and crossplatform MMORPG project.
Copyright (C) 2006  Guillaume PERSONNETTAZ

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.*/

package aiEngine;

import java.util.ArrayList;

public class CoucheDeNeurone {
	
	private int nb_neurone;
	private ArrayList<Neurone> tab_neurone;
	
	public CoucheDeNeurone(int nbneurone, int entree_par_neurone){
		nb_neurone = nbneurone;
		tab_neurone = new ArrayList<Neurone>(nb_neurone);
		for(int i = 0 ; i < nb_neurone ; i++){
			tab_neurone.add(new Neurone(entree_par_neurone));
		}
		
	}

	public int remplacerPoids(ArrayList<Double> poids, int indice){
		int indice_grd_tab = indice;
		for(int i = 0 ; i < nb_neurone ; i++){
			indice_grd_tab = tab_neurone.get(i).remplacerPoids(poids, indice_grd_tab);
		}
		return indice_grd_tab;
	}
	
	public ArrayList<Double> queFaire(ArrayList<Double> entree){
		ArrayList<Double> sortie = new ArrayList<Double>();
		double reponse;
		for(int i = 0 ; i < nb_neurone ; i++){
			reponse = tab_neurone.get(i).queFaire(entree);
			sortie.add(reponse);
		}
		
		return sortie;
	}

}
