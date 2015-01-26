package tinymonkeys.modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe du singe erratique.
 * 
 * @version 1.0
 * @author Camille Constant
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
			this.setPosition(nextCase.x, nextCase.y);
		}
	}

	/**
	 * Produit une nouvelle position innocupée, et adjacente à celle du singe.
	 * Si toutes les positions adjacentes sont occupées, retourne null.
	 * 
	 * @return une CaseVide représentant la nouvelle position.
	 */
	private CaseVide getNextRandomPos() {
		final Random random = new Random();
		
		// Création d'une liste contenant les 4 déplacements possibles
		final List<CaseVide> listPos = new ArrayList<CaseVide>();
		listPos.add(new CaseVide(this.getX(), this.getY() + 1));
		listPos.add(new CaseVide(this.getX(), this.getY() - 1));
		listPos.add(new CaseVide(this.getX() + 1, this.getY()));
		listPos.add(new CaseVide(this.getX() - 1, this.getY()));
		
		CaseVide nextCase = null;
		do {
			nextCase = listPos.remove(random.nextInt(listPos.size()));
		} while (!this.monkeyIsland.isLibre(nextCase) && !listPos.isEmpty());
		
		return nextCase;
	}
}
