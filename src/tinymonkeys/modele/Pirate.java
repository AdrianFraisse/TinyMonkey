package tinymonkeys.modele;

import javax.swing.event.EventListenerList;

/**
 * Classe d'un pirate.
 * 
 * @version 1.0
 * @author Camille Constant
 *
 */
public class Pirate {

	/**
	 * Le chemin vers l'image du pirate.
	 */
	private String avatar;

	/**
	 * Coordonnee en abscisse du pirate (indice dans la carte).
	 */
	private int x;

	/**
	 * Coordonnee en ordonnee du pirate (indice dans la carte).
	 */
	private int y;

	/**
	 * Ile aux singes.
	 */
	private Ile monkeyIsland;

	/**
	 * Liste des écouteurs sur le pirate.
	 */
	final private EventListenerList pirateEcouteurs;

	/**
	 * Constructeur du pirate sans position ni nom renseignes mais avec l'ile
	 * associee.
	 * 
	 * @param ile l'ile contenant tous les elements de celle-ci.
	 */
	public Pirate(Ile ile) {
		this.monkeyIsland = ile;
		this.pirateEcouteurs = new EventListenerList();
	}

	/**
	 * Constructeur du pirate avec le nom renseigne.
	 * 
	 * @param ile l'ile contenant tous les elements de celle-ci.
	 * @param avatar le lien vers l'avatar du pirate.
	 */
	public Pirate(Ile ile, String avatar) {
		this.monkeyIsland = ile;
		this.avatar = avatar;
		this.pirateEcouteurs = new EventListenerList();
	}

	/**
	 * Accesseur en lecture de la position en abscisse du pirate.
	 * 
	 * @return la coordonnee en abscisse.
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * Accesseur en lecture de la position en ordonnee du pirate.
	 * 
	 * @return la coordonnee en ordonnee.
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * Place le pirate a sa position initiale.
	 * 
	 * @param x la coordonnee initiale en abscisse.
	 * @param y la coordonnee initiale en ordonnee.
	 */
	public void positionInitiale(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Methode deplacant le pirate selon la direction demandee. Si le
	 * deplacement indique positionne le pirate sur un singe, le pirate meurt.
	 * Si le deplacement indique positionne le pirate sur le tresor, le tresor
	 * est detruit.
	 * 
	 * @param dx la direction en abscisse (comprise entre -1 et 1).
	 * @param dy la direction en ordonnee (comprise entre -1 et 1).
	 */
	public void demandeDeplacement(int dx, int dy) {
		// TODO - Use listeners ? - Req. Isle for step info
	}

	/**
	 * Accesseur en lecture de l'avatar.
	 * 
	 * @return le chemin vers l'image.
	 */
	public String getAvatar() {
		return this.avatar;
	}

	/**
	 * Accesseur en ecriture de l'avatar du pirate.
	 * 
	 * @param avatar l'avatar du pirate.
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * Enregistre dans la liste des ecouteurs de pirate l'ecouteur passe en
	 * parametre.
	 * 
	 * @param ecouteur ecouteur du pirate.
	 */
	public void enregistreEcPirate(PirateEcouteur ecouteur) {
		this.pirateEcouteurs.add(PirateEcouteur.class, ecouteur);
	}
}
