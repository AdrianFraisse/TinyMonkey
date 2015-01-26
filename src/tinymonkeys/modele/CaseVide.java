package tinymonkeys.modele;

import java.util.Random;

/**
 * 
 * Une case de la carte.
 * 
 * @author Adrian Fraisse
 * @version 1.0
 *
 */
public class CaseVide extends AbstractElement {

	/**
	 * Constructeur.
	 * 
	 * @param x coordonnée en absysse de la case.
	 * @param y coordonnée en ordonnées de la case.
	 */
	public CaseVide(int x, int y) {
		super(x, y);
	}
	
	/**
	 * Méthode static retournant une nouvelle instance aléatoire de case vide.
	 * 
	 * @param largeurCarte la largeur de la carte
	 * @param longueurCarte la longeur de la carte
	 * @return une CaseVide
	 */
	public static CaseVide genererCaseAleatoire(int largeurCarte, int longueurCarte) {
		final Random random = new Random();
		return new CaseVide(random.nextInt(largeurCarte + 1), random.nextInt(longueurCarte + 1));
	}
}
