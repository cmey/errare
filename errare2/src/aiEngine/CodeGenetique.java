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

import aiEngine.Chromosome;
//one mob = 1 chromosome
public class CodeGenetique implements Serializable{
	
	private static final long serialVersionUID = 1L;


	public static double moyenne;
	

	private ArrayList<Chromosome> population;
	private int nb_chromo_dans_pop;
	private int nb_generation;
	private int nb_chromo;
	private int nb_poids;
	
	public CodeGenetique(){
		this.nb_chromo = 0;
		this.nb_poids = 0;
		nb_generation = 0;
		nb_chromo_dans_pop = 0;
		population = null;
	}
	
	public CodeGenetique(int nb_chromo, int nb_poids){
		this.nb_chromo = nb_chromo;
		this.nb_poids = nb_poids;
		nb_generation = 0;
		nb_chromo_dans_pop = nb_chromo;
		population = new ArrayList<Chromosome>(nb_chromo_dans_pop);
		for(int i = 0 ; i < nb_chromo_dans_pop ; i++){
			population.add(new Chromosome(nb_poids));
		}
	}
	
	// dice
	public void generationSuivante(){
		moyenne = calculerMoyenne();			
		ArrayList<Chromosome> nouvelle_population = new ArrayList<Chromosome>(nb_chromo_dans_pop);
		int nbpoids = population.get(0).getNb_poids();
		initialiserRoulette();
		Chromosome elite;
		if(population.size() == 0) elite = null;
		else {
			elite = population.get(0);
		}
		while(population.size() > 1){
			Chromosome[] c = lancerRoulette().fusion(lancerRoulette());
			nouvelle_population.add(c[0]);
			nouvelle_population.add(c[1]);
		}
		while(nouvelle_population.size() < nb_chromo_dans_pop){
			if(elite == null) nouvelle_population.add(new Chromosome(nbpoids));
			else nouvelle_population.add(elite.clone());
		}
		
		population = nouvelle_population;
		
		mutationPopulation();
		razInteret();
		nb_generation++;
	}
	
	private double calculerMoyenne() {
		return (double)getInteretPopulation(population)/population.size();
	}

	private void razInteret() {
		for(int i = 0 ; i < population.size() ; i++){
			population.get(i).setInteret(0);
		}
		
	}
	
	public int indiceElite(){
		int indice_meilleur = 0;
		int interet_meilleur = 0;
		for(int i = 0; i < population.size() ; i++){
			if(population.get(i).getInteret() > interet_meilleur){
				interet_meilleur = population.get(i).getInteret();
				indice_meilleur = i;
			}
		}
		return indice_meilleur;
	}

	private Chromosome retirerMeilleur(){
		int indice_meilleur = 0;
		int interet_meilleur = 0;
		for(int i = 0; i < population.size() ; i++){
			if(population.get(i).getInteret() > interet_meilleur){
				interet_meilleur = population.get(i).getInteret();
				indice_meilleur = i;
			}
		}
		return population.remove(indice_meilleur);
	}
	
	private void initialiserRoulette(){
		for(int i = 0 ; i < population.size() ; i++){
			if(population.get(i).getInteret() < 0) population.remove(i--).getInteret();
		}
		ArrayList<Chromosome> pop = new ArrayList<Chromosome>(nb_chromo_dans_pop);
		for(int i = 0 ; population.size() > 0 ; i++){
			pop.add(retirerMeilleur());
		}
		this.population = pop;
	}
	
	private Chromosome lancerRoulette(){
		int interet_pop = getInteretPopulation(population);
		int score;
		if(interet_pop == 0) score = interet_pop;
		else score = Chromosome.rand.nextInt(getInteretPopulation(population));
		
		int score_courant = population.get(0).getInteret();
		int i = 0;
		while(score > score_courant){
			score_courant += population.get(++i).getInteret();
		}
		Chromosome c = population.remove(i);
		return c;
	}
	
	private void mutationPopulation(){
		for(int i = 0 ; i < nb_chromo_dans_pop ; i++){
			population.get(i).mutation();
		}
	}
	
	public Chromosome retournerChromosome(int indice_chromo){
		return population.get(indice_chromo);
	}
	
	public int getGeneration(){
		return nb_generation;
	}
	
	public int getInteretPopulation(ArrayList<Chromosome> pop){
		int rep = 0;
		for(int i = 0 ; i < pop.size() ; i++){
			rep += pop.get(i).getInteret();
		}
		return rep;
	}
	
	public int getInteretPopulation(){
		return  getInteretPopulation(population);
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.writeInt(nb_chromo_dans_pop);
		out.writeInt(nb_generation);
		for(int i = 0 ; i < population.size() ; i++){
			out.writeObject(population.get(i));
		}
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		nb_chromo_dans_pop = stream.readInt();
		nb_generation = stream.readInt();
		population = new ArrayList<Chromosome>(nb_chromo_dans_pop);
		for(int i = 0 ; i < nb_chromo_dans_pop ; i++){
			population.add(i, (Chromosome) stream.readObject());
		}
	}
	
	public ArrayList<Chromosome> clonePopulation(){
		ArrayList<Chromosome> pop = new ArrayList<Chromosome>(nb_chromo_dans_pop);
		for(int i = 0 ; i < population.size();i++){
			pop.add(i, population.get(i).clone());
		}
		return pop;
	}
	
	public int getNbChromosome(){
		return population.size();
	}
}
