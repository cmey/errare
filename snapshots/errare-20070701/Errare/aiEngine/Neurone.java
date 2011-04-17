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

public class Neurone {
	
	
	private int nb_entree;
	private ArrayList<Double> poids; //poids entre -1 et 1
	
	public Neurone(int nb){
		nb_entree = nb;
		poids = new ArrayList<Double>(nb_entree + 1);
		for(int i = 0 ; i < nb_entree ; i++){
			poids.add(null);
		}
	}

	public int remplacerPoids(ArrayList<Double> nouv_poids, int indice) {
		int indice_grd_tab = indice;
		ArrayList<Double> nouveaux_poids = new ArrayList<Double>(nb_entree + 1);
		for(int i = indice_grd_tab ; i < indice_grd_tab + nb_entree + 1 ; i++){
			nouveaux_poids.add(nouv_poids.get(i));
		}
		poids = nouveaux_poids;
		return indice_grd_tab + nb_entree + 1;
	}
	
	public double queFaire(ArrayList<Double> entree){
		double sortie = 0;
		for(int i = 0 ; i < nb_entree ; i++){
			sortie += poids.get(i) * entree.get(i);
		}
		sortie -= poids.get(nb_entree);
		return sigmoid(sortie);
		//return sortie;
	}

	public int getNb_Entree(){
		return nb_entree;
	}
	
	private double sigmoid(double sortie){
		return 1/(1+Math.exp(-sortie)) - 0.5;
	}
}
