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

import java.util.*;

public class ReseauNeural {
	
	private int nb_sorties_couche_finale;
	private int nb_couches_cachees;
	private int nb_neurone_par_couche;
	private ArrayList<CoucheDeNeurone> couche_neurale;
	
	public ReseauNeural(int entree,		int entee_neuron_prem_couche,
						int entree_par_neurone,
						int nb_sorties_fin,
						int couche_cachee,
						int neurone_couche){
		
		nb_sorties_couche_finale = nb_sorties_fin;
		nb_couches_cachees = couche_cachee;
		nb_neurone_par_couche = neurone_couche;
		
		couche_neurale = new ArrayList<CoucheDeNeurone>(nb_couches_cachees + 1);
		
		if(couche_cachee > 0){
			couche_neurale.add(new CoucheDeNeurone(nb_neurone_par_couche, entee_neuron_prem_couche));
		}
		for(int i = 1 ; i < nb_couches_cachees ; i++){
			couche_neurale.add(new CoucheDeNeurone(nb_neurone_par_couche, entree_par_neurone));
		}
		couche_neurale.add(new CoucheDeNeurone(nb_sorties_couche_finale, entree_par_neurone));
	}
	
	public void remplacerPoids(ArrayList<Double> poids){
		int super_indice = 0;
		for(int i = 0 ; i < nb_couches_cachees ; i++){
			super_indice = couche_neurale.get(i).remplacerPoids(poids, super_indice);
		}
		couche_neurale.get(nb_couches_cachees).remplacerPoids(poids, super_indice);
	}
	
	public ArrayList<Double> queFaire(ArrayList<Double> entree){
		for(int i = 0 ; i < nb_couches_cachees + 1 ; i++){
			entree = couche_neurale.get(i).queFaire(entree);
		}
		return entree;
	}

}
