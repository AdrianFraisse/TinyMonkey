package tinymonkeys.modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe du singe erratique.
 * 
 * @version 1.1
 * @author Camille Constant, Adrian Fraisse
 *
 */

public class SingeErratique extends Singe {
	/**
	 * Constructeur de la classe SingeErratique.
	 * 
	 * @param x la coordonnee en abscisse du singe.
	 * @param y la coordonnee en ordonnee du singe.
	 * @param ile l'ile sur laquelle vit le singe.
	 */
	public SingeErratique(int x, int y, Ile ile) {
		super(x, y, ile);
	}

	/**
	 * Deplacement aleatoire du singe erratique.
	 */
	public void deplacerSinge() {
		final CaseVide nextCase = this.getNextRandomPos();
		if (null != nextCase) {
			// Si la case est null, on abandonne le déplacement
			this.setPosition(nextCase.x, nextCase.y);
			this.getMonkeyIsland().getPirate().tuerPirate(this);
		}
	}

	/**
	 * Produit une nouvelle position innocupée, et adjacente à celle du singe.
	 * Si toutes les positions adjacentes sont occupées, retourne null.
	 * 
	 * @return une CaseVide représentant la nouvelle position.
	 */
	protected CaseVide getNextRandomPos() {

		// Création d'une liste contenant les 4 déplacements possibles
		final List<CaseVide> listPos = new ArrayList<CaseVide>();
		listPos.add(new CaseVide(this.getX(), this.getY() + 1));
		listPos.add(new CaseVide(this.getX(), this.getY() - 1));
		listPos.add(new CaseVide(this.getX() + 1, this.getY()));
		listPos.add(new CaseVide(this.getX() - 1, this.getY()));
		
		// Mélange de la liste
		Collections.shuffle(listPos);
		
		CaseVide nextCase; // NOPMD by Adrian on 22/02/15 16:42
		do {
			// Retrait de la première case. 
			// Si la position est invalide, nouvelle tentative aléatoire.
			nextCase = listPos.remove(0);
		} while (!listPos.isEmpty()
				&& !this.getMonkeyIsland().isDeplacementPossible(nextCase.x, nextCase.y));
		return listPos.isEmpty() ? null : nextCase;
	}
}
