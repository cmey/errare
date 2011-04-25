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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
//un bestiau = 1 chromosome
public class CodeGenetique implements Serializable{
	
	private static final long serialVersionUID = 1L;


	public static double moyenne;
	

	private ArrayList<Chromosome> population;
	private int nb_chromo_dans_pop;
	private int nb_generation;
	private int nb_chromo;
	private int nb_poids;
	//private ArrayList<Chromosome> meilleur_pop;
	//private int meilleur_chromosome;
	
	public CodeGenetique(){
		this.nb_chromo = 0;
		this.nb_poids = 0;
		nb_generation = 0;
		nb_chromo_dans_pop = 0;
		population = null;
		//meilleur_pop = null;
	}
	
	public CodeGenetique(int nb_chromo, int nb_poids){
		//meilleur_chromosome = 0;
		this.nb_chromo = nb_chromo;
		this.nb_poids = nb_poids;
		nb_generation = 0;
		nb_chromo_dans_pop = nb_chromo;
		//meilleur_pop = null;
		population = new ArrayList<Chromosome>(nb_chromo_dans_pop);
		for(int i = 0 ; i < nb_chromo_dans_pop ; i++){
			population.add(new Chromosome(nb_poids));
		}
	}
	
	/*//la meilleur moiti� pond et survi. L'autre, hehe....
	public void generationSuivante(){
		int moitie = (nb_chromo_dans_pop - nb_chromo_dans_pop % 2) / 2;
		ArrayList<Chromosome> nouvelle_population = new ArrayList<Chromosome>(nb_chromo_dans_pop);
		for(int i = 0 ; i < moitie ; i++){
			Chromosome c = retirerMeilleur();
			if(c.getInteret() == 0) nouvelle_population.add(nouvelle_population.get(0)); //si chromo inutile, autant copier le meilleur
			else nouvelle_population.add(c);
		}
		for(int i = 0 ; i < moitie - 1 ; i++){
			Chromosome[] c = nouvelle_population.get(i).fusion(nouvelle_population.get(i+1));
			nouvelle_population.add(c[0]);
			nouvelle_population.add(c[1]);
		}
		//un peu de sang neuf ! (et idiot pas cons�quent ^^)
		if(nb_chromo_dans_pop % 2 == 1){
			nouvelle_population.add(new Chromosome(population.get(0).getNb_poids()));
		}
		
		population = nouvelle_population;
		mutationPopulation();
		razInteret();
		nb_generation++;
	}*/
	
	//roulette
	public void generationSuivante(){
		
		
		moyenne = calculerMoyenne();
	//	if(meilleur_pop == null || getInteretPopulation(population) >= getInteretPopulation(meilleur_pop)){
			
		//	meilleur_pop = clonePopulation();
			
			ArrayList<Chromosome> nouvelle_population = new ArrayList<Chromosome>(nb_chromo_dans_pop);
			int nbpoids = population.get(0).getNb_poids();
			initialiserRoulette();
			//System.out.println("population après initialisation roulette : "+population.size());
			Chromosome elite;
			if(population.size() == 0) elite = null;
			else {
				elite = population.get(0);
				//System.out.println("elite : "+elite.getInteret());
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
		/*}else{
			population = new ArrayList<Chromosome>(nb_chromo_dans_pop);
			for(int i = 0 ; i < meilleur_pop.size();i++){
				population.add(i, meilleur_pop.get(i).clone());
			}
			razInteret();
		}*/
		
		
	}
	
	//elitisme moyen
	/*public void generationSuivante(){
		moyenne = calculerMoyenne();
		int nb_poids = population.get(0).getNb_poids();
		ArrayList<Chromosome> nouvelle_population = new ArrayList<Chromosome>(nb_chromo_dans_pop);
		Chromosome chromo = retirerMeilleur();
		while(chromo.getInteret() >= moyenne && population.size() > 0){
			nouvelle_population.add(chromo);
			chromo = retirerMeilleur();
		}
		//System.out.println("population : "+population.size()+" | nouvelle "+nouvelle_population.size());
		for(int i = 0 ; i < nouvelle_population.size() / 2 && nouvelle_population.size() != nb_chromo_dans_pop; i++){
			Chromosome[] c = nouvelle_population.get(i).fusion(nouvelle_population.get(i+1));
			nouvelle_population.add(c[0]);
			if(nouvelle_population.size() != nb_chromo_dans_pop) nouvelle_population.add(c[1]);
		}
		while(nouvelle_population.size() < nb_chromo_dans_pop && nouvelle_population.size() > 0){
			nouvelle_population.add(nouvelle_population.get(0).clone());
		}
		while(nouvelle_population.size() < nb_chromo_dans_pop){
			nouvelle_population.add(new Chromosome(nb_poids));
		}
		
		
		population = nouvelle_population;
		//System.out.println(population.size());
		mutationPopulation();
		razInteret();
		nb_generation++;
		
	}*/
	
	
	/*
	public void generationSuivante(){
		//---elitisme � 200% ^^
		calculerMoyenne();
		ArrayList<Chromosome> nouvelle_population = new ArrayList<Chromosome>(nb_chromo_dans_pop);
		Chromosome elite = retirerMeilleur();
		for(int i = 0 ; i < nb_chromo_dans_pop ; i++){
			nouvelle_population.add(elite);
		}
		population = nouvelle_population;
		mutationPopulation();
		razInteret();
		nb_generation++;
	}*/
	
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
		//System.out.println("initialisation roulette");
		for(int i = 0 ; i < population.size() ; i++){
			if(population.get(i).getInteret() < 0) population.remove(i--).getInteret();
		}
		//System.out.println("reste dans population : "+population.size());
		ArrayList<Chromosome> pop = new ArrayList<Chromosome>(nb_chromo_dans_pop);
		for(int i = 0 ; population.size() > 0 ; i++){
			pop.add(retirerMeilleur());
		}
		//System.out.println("pop finale ds roulette : "+pop.size());
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
		//System.out.println("gagnant roulette : "+c.getInteret());
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
		//System.out.println("calcul interet pop");
		for(int i = 0 ; i < pop.size() ; i++){
			//System.out.println("   interet : "+population.get(i).getInteret());
			rep += pop.get(i).getInteret();
		}
		//System.out.println("interet final  : "+rep);
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
