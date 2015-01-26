package tinymonkeys.modele;

import java.util.Vector;

import javax.swing.event.EventListenerList;

/**
 * Classe d'une bande de singes erratiques.
 * 
 * @version 1.0
 * @author Camille Constant
 *
 */
public class BandeDeSingesErratiques extends Thread {

	/**
	 * Vecteur contenant l'ensemble des singes erratiques.
	 */
	private Vector<SingeErratique> erratiques;

	/**
	 * L'ile.
	 */
	private Ile monkeyIsland;

	/**
	 * Liste des écouteurs sur la bande de singes erratiques.
	 */
	final private EventListenerList bandeSingesEcouteurs;

	/**
	 * Constructeur d'une bande de singes erratiques vide.
	 * 
	 * @param ile l'ile contenant l'ensemble des elements de celle-ci.
	 */
	public BandeDeSingesErratiques(Ile ile) {
		super();
		this.erratiques = new Vector<SingeErratique>();
		this.monkeyIsland = ile;
		this.bandeSingesEcouteurs = new EventListenerList();
	}

	/**
	 * Accesseur en lecture a l'ensemble des singes erratiques.
	 * 
	 * @return le vecteur de singes erratiques.
	 */
	public Vector<SingeErratique> getSingesErratiques() {
		return this.erratiques;
	}

	/**
	 * Ajout du nombre indique de singes erratiques a des positions libres
	 * aleatoires.
	 * 
	 * @param n le nombre de singes a ajouter.
	 */
	public void ajoutSingesErratiques(int n) {
		for (int i=0; i<n; i++) {
			final AbstractElement nextCase = this.monkeyIsland.prendrePositionLibreAleatoire();
			this.erratiques.add(new SingeErratique(nextCase.x, nextCase.y, this.monkeyIsland));
		}
	}

	/**
	 * Enregistre dans la liste des ecouteurs de bande de singes l'ecouteur
	 * passe en parametre.
	 * 
	 * @param ecouteur ecouteur de la bande de singes.
	 */
	public void enregistreEcBandeSinges(BandeDeSingesErratiquesEcouteur ecouteur) {
		this.bandeSingesEcouteurs.add(BandeDeSingesErratiquesEcouteur.class,
				ecouteur);
	}

	@Override
	public void run() {
		// TODO
	}

}
