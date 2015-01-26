package tinymonkeys.modele;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
	 * Temporisation entre chaque déplacement de singe.
	 */
	private static final int TEMPO_DEPLACEMENT = 500;

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
		// On récupère toutes les cases terre
		final List<CaseVide> casesTerre = this.monkeyIsland.genererListCasesTerre();		
		final Random random = new Random();
		
		for (int i=0; i<n; i++) {
			// On prend une case terre de manière aléatoire
			final CaseVide nextCase = casesTerre.remove(random.nextInt(casesTerre.size() + 1));
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
		// Déplace les singes, les uns après les autres, de manière temporisée
		for (int i = 0; i < this.erratiques.size(); i++) {			
			try {
				Thread.sleep(TEMPO_DEPLACEMENT);
			} catch (InterruptedException e) {
				System.err.println("Thread interrompu : " + e.getCause());
			}
			final SingeErratique singe = this.erratiques.get(i);
			singe.deplacerSinge();
			final int id = i;
			Arrays.asList(this.bandeSingesEcouteurs.getListeners(BandeDeSingesErratiquesEcouteur.class))
			 	.forEach(listener -> listener.deplacementSingeErratique(id, singe.x, singe.y));
		}
	}

}
