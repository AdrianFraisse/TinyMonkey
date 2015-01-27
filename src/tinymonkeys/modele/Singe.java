package tinymonkeys.modele;

/**
 * Classe abstraite Singe.
 * 
 * @version 1.0
 * @author Camille Constant
 */
public abstract class Singe extends AbstractElement {
	/**
	 * Ile contenant tous les elements de celle-ci.
	 */
	private Ile monkeyIsland;

	/**
	 * Constructeur de la classe Singe.
	 * 
	 * @param x la coordonnee en abscisse du singe.
	 * @param y la coordonnee en ordonnee du singe.
	 * @param ile l'ile sur laquelle vit le singe.
	 */
	public Singe(int x, int y, Ile ile) {
		super(x, y);
		this.setMonkeyIsland(ile);
	}

	/**
	 * Methode de deplacement du singe. Le deplacement est propre au type de
	 * singe (erratique, chasseur...).
	 */
	public abstract void deplacerSinge();

	/**
	 * Getter de monkeyIsland.
	 * 
	 * @return l'ile
	 */
	public Ile getMonkeyIsland() {
		return this.monkeyIsland;
	}

	/**
	 * Setter de monkeyIsland.
	 * 
	 * @param monkeyIsland l'ile à setter
	 */
	public void setMonkeyIsland(Ile monkeyIsland) {
		this.monkeyIsland = monkeyIsland;
	}
}
