
		package graphicsEngine;

import java.awt.Point;

/**
 * 
 * @author Troubleshooting
 *
 */
public class Matrice
{

	private final int nbEquations; // n lignes
	//	private int m ; //m colonnes
	private final float[][] e; //n <=> nbre d'equations
	private final float[] s;
	private boolean ok;

	public Matrice(int aNbEquations, float[][] tabmatrice)
	{

		e=(float[][])(tabmatrice.clone());
		nbEquations = aNbEquations;
		if (nbEquations >= 12)
		{
			throw new IllegalArgumentException("Ca va pas la tete ???");
		}

		//e = new float[nbEquations + 1][nbEquations];
		s = new float[nbEquations];

		ok = false;
	}

	/**public void initialize()
	{
		for (int i = 0; i < nbEquations; i++)
		{
			System.out.println("equation " + i);
			for (int j = 0; j < nbEquations; j++)
			{
				System.out.print("facteur " + j + " = ");
				e[j][i] = Lire.i();
				System.out.println(" ");
			}
			System.out.println(" ");
			System.out.println("resultat " + i + " = ");
			e[nbEquations][i] = Lire.i();
			System.out.println(" ");
		} // on a saisi les facteurs du système ds e[][]

		ok = true;
	}**/

	private boolean ok()
	{
		return ok;
	}

	public Point resolution()
	{	
		Point res=new Point();
		float temp;
		int a, b;

		//triangularisation
		for (int k = 0; k < nbEquations - 1; k++)
		{
			for (a = 1 + k; a < nbEquations; a++)
			{
				temp = e[k][a];
				for (b = k; b < nbEquations + 1; b++)
				{
					e[b][a] = e[b][a] * e[k][k] - e[b][k] * temp;
				}
			}
		}
		//remplacement et r�solution
		s[nbEquations - 1] = e[nbEquations][nbEquations - 1] / e[nbEquations - 1][nbEquations - 1];
		//le vecteur s commence à 0 donc s[n-1] <=> dernière inconnue
		//la matrice e commence à 0 et reçoit [colonnes][lignes] donc e[n][n-1]
		//correspond à l'élément de la colonne résultat de la dernière ligne
		//e[n-1][n-1] correspond au dernier facteur de la dernière ligne.

		for (int i = 1; i < nbEquations; i++) //décalage des colonnes
		{
			for (int j = 2; j <= nbEquations; j++)
			{ //décalage des lignes
				e[nbEquations - i][nbEquations - j] *= s[nbEquations - i];
				e[nbEquations][nbEquations - j] -= e[nbEquations - i][nbEquations - j];
				e[nbEquations - i][nbEquations - j] = 0;
			}
			s[nbEquations - (i + 1)] = e[nbEquations][nbEquations - (i + 1)] / e[nbEquations - (i + 1)][nbEquations - (i + 1)]; //on met � jour le vecteur
		}
		//affichage
		for (int i = 0; i < nbEquations; i++)
		{
			System.out.println("facteur " + i + "= " + s[i]);

		}
		
		res.setLocation((int)s[0],(int)s[2]);
		return res;
	}
}