package tinymonkeys.modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.event.EventListenerList;

/**
 * Classe Ile.
 * 
 * @version 1.1
 * @author Camille Constant, Adrian Fraisse
 *
 */
public class Ile {
	/**
	 * La carte de l'ile : une matrice indiquant si chaque case est de type mer
	 * (0) ou terre (1).
	 */
	private int[][] carte;
	
	/**
	 * Les singes erratiques.
	 */
	private BandeDeSingesErratiques erratiques;

	/**
	 * Le tresor.
	 */
	private Tresor tresor;

	/**
	 * Le pirate du joueur.
	 */
	private Pirate pirate;

	/**
	 * Liste des écouteurs sur l'ile.
	 */
	final private EventListenerList ileEcouteurs;

	/**
	 * Constructeur de la classe Ile.
	 */
	public Ile() {
		this.carte = null;
		this.erratiques = new BandeDeSingesErratiques(this);
		this.tresor = null;
		this.pirate = new Pirate(this);
		this.ileEcouteurs = new EventListenerList();
	}

	/**
	 * Indique la largeur de la carte en nombre de cases.
	 * 
	 * @return la largeur de la carte.
	 */
	public int getLargeurCarte() {
		return this.carte.length;
	}

	/**
	 * Indique la longueur de la carte en nombre de cases.
	 * 
	 * @return la longueur de la carte.
	 */
	public int getLongueurCarte() {
		return this.carte[0].length;
	}

	/**
	 * Permet l'acces en lecture a la valeur de la carte aux coordonnees
	 * indiquees.
	 * 
	 * @param x la coordonnee en abscisse.
	 * @param y la coordonnee en ordonnee.
	 * @return la valeur de la case de la carte aux coordonnees indiquees.
	 */
	public int valeurCarte(int x, int y) {
		return this.carte[x][y];
	}

	/**
	 * Creation de la carte.
	 * 
	 * @param carte la matrice terre-mer.
	 */
	public void creationCarte(int[][] carte) {
		this.carte = carte.clone();
	}

	/**
	 * Mise à jour de la carte.
	 * 
	 * @param carte la matrice terre-mer.
	 */
	public void setCarte(int[][] carte) {
		this.carte = carte.clone();

	}

	/**
	 * Accesseur en lecture du pirate de l'ile.
	 * 
	 * @return le pirate.
	 */
	public Pirate getPirate() {
		return this.pirate;
	}

	/**
	 * Creation du pirate sur l'ile.
	 * 
	 * @param avatar le lien vers l'image du pirate.
	 */
	public void ajoutPirate(String avatar) {
		this.pirate.setAvatar(avatar);
		
		// On évite de mettre le pirate immédiatement sur un singe
		CaseVide newCase = null;
		do {
			newCase = CaseVide.genererCaseAleatoire(this.getLargeurCarte(), this.getLongueurCarte());
		} while (!this.isLibre(newCase.x, newCase.y));
		
		this.pirate.positionInitiale(newCase.x, newCase.y);
	}

	/**
	 * Methode permettant de faire la demande de deplacement du pirate. Cette
	 * methode fait suite a un appui sur une fleche directionnelle du clavier.
	 * 
	 * @param dx la direction en abscisse.
	 * @param dy la direction en ordonnee.
	 */
	public void demandeDeplacementPirate(int dx, int dy) {
		this.pirate.demandeDeplacement(dx, dy);
	}

	/**
	 * Accesseur en lecture de la bande de singes erratiques.
	 * 
	 * @return la bande de singes erratiques.
	 */
	public BandeDeSingesErratiques getSingesErratiques() {
		return this.erratiques;
	}

	/**
	 * Ajout du nombre indique de singes erratiques dans la liste des singes
	 * erratiques.
	 * 
	 * @param n le nombre de singes erratiques a ajouter.
	 */
	public void ajoutSingesErratiques(int n) {
		this.erratiques.ajoutSingesErratiques(n);
	}

	/**
	 * Accesseur en lecture du tresor.
	 * 
	 * @return le tresor.
	 */
	public Tresor getTresor() {
		return this.tresor;
	}

	/**
	 * Creation du tresor a une position aleatoire.
	 */
	public void creationTresor() {
		CaseVide newCase = null;
		
		// Positionnement du trésor sur une case vide aléatoire
		do {
			newCase = CaseVide.genererCaseAleatoire(this.getLargeurCarte(), this.getLongueurCarte());
		} while (this.valeurCarte(newCase.x, newCase.y) != 1);
		
		this.tresor = new Tresor(newCase.x, newCase.y);
	}

	/**
	 * Suppression du tresor.
	 */
	public void suppressionTresor() {
		// TODO : Est-ce vraiment le comportement attendu ?
		this.tresor = null;
	}

	/**
	 * Enregistre dans la liste des ecouteurs de l'ile l'ecouteur passe en
	 * parametre.
	 * 
	 * @param ecouteur ecouteur de l'ile.
	 */
	public void enregistreEcIle(IleEcouteur ecouteur) {
		this.ileEcouteurs.add(IleEcouteur.class, ecouteur);
	}
	
	/**
	 * Génére une liste contenant toutes les cases terre.
	 * 
	 * @return une liste de CaseVide
	 */
	protected List<CaseVide> genererListCasesTerre() {
		final List<CaseVide> cases = new ArrayList<CaseVide>();
		for (int i=0; i<this.getLargeurCarte(); i++) {
			for (int j=0; j<this.getLongueurCarte(); j++) {
				if (this.valeurCarte(i, j) == 1) {
					cases.add(new CaseVide(i, j));
				}
			}
		}
		return cases;
	}
	
	/**
	 * Vérifie si la case aux cordonnées passées en paramètre est libre, 
	 * i.e. c'est une case terre ne contenant pas d'autre personnage (exception faite du pirate).
	 * 
	 * @param x abscisse de la case
	 * @param y ordonnée de la case 
	 * @return true si la case est vide
	 */
	protected boolean isLibre(int x, int y) {
		boolean res = false;
		if (x <= this.getLargeurCarte() && y <= this.getLongueurCarte() && this.valeurCarte(x, y) == 1) {
			for (SingeErratique singe : this.erratiques.getSingesErratiques()) {
				if (singe.coordonneesEgales(x, y)) {
					res = false;
					break;
				}
			}
		}
		return res;
	}
	
}
