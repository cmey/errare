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

import java.io.*;
import java.util.*;

public class Chromosome implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public static Random rand = new Random();
	
	public static final double MAX_PERTURBATION = 0.3;
	public static final double TAUX_MUTATION = 0.3;
	public static final double TAUX_FUSION = 0.7;
	
	//nombre de poids TOTAL !
	private int nb_poids;
	private ArrayList<Double> genome;
	// incrï¿½mentï¿½ ï¿½ chaque "bonne action"
	private int interet;
	
	public Chromosome(int poids){
		nb_poids = poids;
		genome = new ArrayList<Double>(nb_poids);
		for(int i = 0; i < nb_poids ; i++){
			genome.add(rand.nextDouble() * 2 - 1);
		}
		interet = 0;
	}
	
	private Chromosome(int poids, ArrayList<Double> genom, int inte){
		nb_poids = poids;
		genome = genom;
		interet = inte;
		
		/*System.out.println("#############################");
		for(int i = 0 ; i < genom.size() ; i++){
			System.out.println("genome.add("+genome.get(i)+");");
		}*/
	}
	
	public void incrementerInteret(int i){
		interet += i;
	}
	
	//mutation pour chaque poids
	public void mutation(){
		double nouveau_poids;
		for(int i = 0 ; i < nb_poids ; i++){
			if(rand.nextDouble() < TAUX_MUTATION){
				//crï¿½ation d'une perturbation
				if(rand.nextBoolean())	nouveau_poids = genome.get(i) + rand.nextDouble() * MAX_PERTURBATION;
				else nouveau_poids = genome.get(i) - rand.nextDouble() * MAX_PERTURBATION;
				//if(nouveau_poids > 1 || nouveau_poids < -1) System.out.println("passage de "+genome.get(i)+" à "+nouveau_poids);
				genome.set(i, nouveau_poids);
			}
		}
	}
	
	public Chromosome[] fusion(Chromosome c){
		Chromosome[] ret = new Chromosome[2];
		if(rand.nextDouble() < TAUX_FUSION){
			//limite ou on va couper ces 2 chromosomes
			int limite = rand.nextInt(nb_poids);
			ArrayList<Double> gamin1 = new ArrayList<Double>(nb_poids);
			ArrayList<Double> gamin2 = new ArrayList<Double>(nb_poids);
			for(int i = 0 ; i < limite ; i++){
				gamin1.add(i, new Double(this.genome.get(i)));
				gamin2.add(i, new Double(c.genome.get(i)));
			}
			for(int i = limite ; i < nb_poids ; i++){
				gamin1.add(i, new Double(c.genome.get(i)));
				gamin2.add(i, new Double(this.genome.get(i)));
			}
			ret[0] = new Chromosome(nb_poids, gamin1, 0);
			ret[1] = new Chromosome(nb_poids, gamin2, 0);
			
		}else{
			ret[0] = this.clone();
			ret[1] = c.clone();
		}
		
		return ret;
	}

	/**
	 * @return Returns the genome.
	 */
	public ArrayList<Double> getGenome() {
		return genome;
	}

	/**
	 * @return Returns the interet.
	 */
	public int getInteret() {
		return interet;
	}
	
	public void setInteret(int i) {
		interet = i;
	}
	

	/**
	 * @return Returns the nb_poids.
	 */
	public int getNb_poids() {
		return nb_poids;
	}
	
	public Chromosome clone(){
		ArrayList<Double> tab = new ArrayList<Double>(nb_poids);
		for(int i = 0 ; i < this.genome.size() ; i++){
			tab.add(i, new Double(this.genome.get(i)));
		}
		return new Chromosome(nb_poids, tab, interet);
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.writeInt(nb_poids);
		out.writeObject(genome);
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		nb_poids = stream.readInt();
		genome = (ArrayList<Double>) stream.readObject();
	}
	
	public void printChromosome(){
		System.out.println("Resultat : "+getInteret());
		for(int i = 0 ; i < getGenome().size() ; i++){
			System.out.println("genome.add("+getGenome().get(i)+");");
		}
	}
}
