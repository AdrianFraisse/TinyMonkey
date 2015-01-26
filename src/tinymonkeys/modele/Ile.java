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
	 * Une liste représentant les cases terre vides de la carte.
	 */
	private List<AbstractElement> listCasesVides;

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
		this.listCasesVides = this.genererListCasesTerre();
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
		final AbstractElement nextCase = this.prendrePositionLibreAleatoire();
		this.pirate.positionInitiale(nextCase.x, nextCase.y);
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
		final AbstractElement nextCase = this.prendrePositionLibreAleatoire();
		// Un personnage peut être présent sur une case trésor, on libére la case 
		this.listCasesVides.add(nextCase);
		this.tresor = new Tresor(nextCase.getX(), nextCase.getY());
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
	 * @return une liste d'Case
	 */
	private List<AbstractElement> genererListCasesTerre() {
		final List<AbstractElement> cases = new ArrayList<AbstractElement>();
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
	 * Trouve une case terre innocupée sur la carte, de manière aléatoire.
	 * Supprime cette case de la liste des cases libres.
	 * 
	 * @return Un Case, avec ses attributs x, y valorisés.
	 */
	protected AbstractElement prendrePositionLibreAleatoire() {
		final Random random = new Random();
		final int nextCase = random.nextInt(this.listCasesVides.size() + 1);
		return this.listCasesVides.remove(nextCase);
	}

	/**
	 * Vérifie si la case passée en paramètre est libre, i.e. c'est une case terre ne contenant aucun personnage.
	 * 
	 * @param nextCase La case à comparer
	 * @return true si la case est libre, false sinon
	 */
	public boolean isLibre(CaseVide nextCase) {
		for (AbstractElement tmpCase : this.listCasesVides) {
			if (tmpCase.coordonneesEgales(nextCase.getX(), nextCase.getY())) {
				return true;
			}
		}
		return false;
	}
	
}
