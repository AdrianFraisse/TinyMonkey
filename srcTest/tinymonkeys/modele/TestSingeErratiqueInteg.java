package tinymonkeys.modele;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Adrian Fraisse
 * @version 1.1
 * 
 * Classe de test de la classe SingeErratique. Par opposition à
 * TestSingeErratiqueMock, cette classe utilise des instances des objets
 * Ile, BandeDeSingesErratiques et Pirate pour fonctionner.
 * En cela, cette classe teste le comportement de SingeErratique dans
 * son environnement réél d'exécution.
 *
 */
public class TestSingeErratiqueInteg {
	
	/**
	 * Largeur de la carte en nombre de cases.
	 */
	private static final int LARGEUR_GRILLE = 20;

	/**
	 * Hauteur de la carte en nombre de cases.
	 */
	private static final int HAUTEUR_GRILLE = 20;
	
	/**
	 * Nombre d'itérations pour le test d'équiprobabilité : 1 million de déplacements.
	 */
	private static final int NB_IT = 1000000;
	
	private SingeErratique singe;
	private Ile island;
	private Vector<SingeErratique> singes; 
	
	/**
	 * Méthode éxecutée avant chaque test.
	 */
	@Before
	public void setUp() {
		island = new Ile();
		island.creationCarte(exempleCarte());
		island.creationTresor();
		island.ajoutPirate("TDD Evangelist");
		singes = island.getSingesErratiques().getSingesErratiques();		
	}
	
	/**
	 * Verifie l'équiprobabilité de la distribution des déplacements du singe 
	 * erratique.
	 */
	@Test
	public void testEquiprobabilite() {
		// On positionne le singe au millieu de la carte. Quatre autres singes sont ajoutés.
		singe = new SingeErratique(LARGEUR_GRILLE/2, HAUTEUR_GRILLE/2, island);
		singes.add(singe);
		
		singes.add(new SingeErratique(LARGEUR_GRILLE-2, HAUTEUR_GRILLE-2, island)); // Faux positif FindBugs
		singes.add(new SingeErratique(1, 1, island));
		singes.add(new SingeErratique(LARGEUR_GRILLE-2, 1, island));
		singes.add(new SingeErratique(1, HAUTEUR_GRILLE-2, island));
		
		int deplacementsHaut = 0;
		int deplacementsBas = 0;
		int deplacementsGauche = 0;
		int deplacementsDroite = 0;
		
		int previousX = singe.x; // NOPMD by Adrian on 22/02/15 19:40 - Faux positif anomalie DU
		int previousY = singe.y; // NOPMD by Adrian on 22/02/15 19:40 - Faux positif anomalie DU
		
		for (int i = 0; i < NB_IT; ++i) {
			// Déplacement de tous les singes
			singes.forEach(elem -> elem.deplacerSinge());
			
			if (singe.x == previousX && singe.y == (previousY + 1)) ++deplacementsHaut;
			else if (singe.x == previousX && singe.y == (previousY - 1)) ++deplacementsBas;
			else if (singe.x == (previousX + 1) && singe.y == previousY) ++deplacementsDroite;
			else if (singe.x == (previousX - 1) && singe.y == previousY) ++deplacementsGauche;
			previousX = singe.x;
			previousY = singe.y;
		}
		
		assertTrue("Equiprobabilité des 4 cases - Deplacement Bas", ((float) deplacementsBas/NB_IT) > 0.24 &&  0.26 > ((float) deplacementsBas/NB_IT));
		assertTrue("Equiprobabilité des 4 cases - Deplacement Haut", ((float) deplacementsHaut/NB_IT) > 0.24 &&  0.26 > ((float) deplacementsHaut/NB_IT));
		assertTrue("Equiprobabilité des 4 cases - Deplacement Gauche", ((float) deplacementsGauche/NB_IT) > 0.24 &&  0.26 > ((float) deplacementsGauche/NB_IT));
		assertTrue("Equiprobabilité des 4 cases - Deplacement Droite", ((float) deplacementsDroite/NB_IT) > 0.24 &&  0.26 > ((float) deplacementsDroite/NB_IT));
		
	}
	
	/**
	 * Vérifie que le singe reste à sa position actuelle 
	 * si aucun déplacement ne lui est possible. 
	 */
	@Test
	public void testDeplacementSingeImpossible() {
		// On positionne le singe à l'extreme sud-est de la carte.
		singe = new SingeErratique(LARGEUR_GRILLE-2, HAUTEUR_GRILLE-2, island);
		singes.add(singe); // Faux positif FindBugs
		
		// On positionne deux autres singes, à gauche et au dessus.
		// Ainsi notre singe est bloqué par la mer et les 2 autres singes.
		singes.add(new SingeErratique(LARGEUR_GRILLE-2, HAUTEUR_GRILLE-3, island));
		singes.add(new SingeErratique(LARGEUR_GRILLE-3, HAUTEUR_GRILLE-2, island));
				
		// Tentative de déplacement du singe.
		singe.deplacerSinge();
		
		assertTrue("Singe déplacé", singe.coordonneesEgales(LARGEUR_GRILLE-2, HAUTEUR_GRILLE-2));
	}
	
	/**
	 * Test le blocage du singe dans trois directions.
	 */
	@Test
	public void testDeplacementSingeUneDirection() {
		// On positionne le singe à l'extreme sud-est de la carte.
		singe = new SingeErratique(LARGEUR_GRILLE-2, HAUTEUR_GRILLE-2, island);
		singes.add(singe); // Faux positif FindBugs
				
		// On positionne un autre singe, au dessus. 
		// Seul le déplacement gauche est donc possible.
		singes.add(new SingeErratique(LARGEUR_GRILLE-2, HAUTEUR_GRILLE-3, island));
		
		singe.deplacerSinge();

		assertTrue("Singe non déplacé sur la case gauche", singe.coordonneesEgales(LARGEUR_GRILLE-3, HAUTEUR_GRILLE-2));
	}
	
	/**
	 * Test le blocage du singe dans trois directions.
	 */
	@Test
	public void testDeplacementSingeDeuxDirection() {
		// On positionne le singe au centre de la carte.
		singe = new SingeErratique(LARGEUR_GRILLE/2, HAUTEUR_GRILLE/2, island);
		singes.add(singe); // Faux positif FindBugs
				
		// On positionne un autre singe, au dessus, et un autre, à droite. 
		// Seuls les déplacements gauche et bas sont donc possibles.
		singes.add(new SingeErratique(LARGEUR_GRILLE/2 + 1, HAUTEUR_GRILLE/2, island));
		singes.add(new SingeErratique(LARGEUR_GRILLE/2, HAUTEUR_GRILLE/2 + 1, island));
		
		singe.deplacerSinge();
		
		assertTrue("Singe déplacé à droite ou en haut", singe.coordonneesEgales(LARGEUR_GRILLE/2 - 1, HAUTEUR_GRILLE/2)
				|| singe.coordonneesEgales(LARGEUR_GRILLE/2, HAUTEUR_GRILLE/2 - 1));
	}
	
	/**
	 * Test le blocage du singe dans trois directions.
	 */
	@Test
	public void testDeplacementSingeTroisDirection() {
		// On positionne le singe au centre de la carte.
		singe = new SingeErratique(LARGEUR_GRILLE/2, HAUTEUR_GRILLE/2, island);
		singes.add(singe); // Faux positif FindBugs
				
		// On positionne un autre singe, à gauche. 
		// Seuls les déplacements droite, haut, et bas, sont donc possibles.
		singes.add(new SingeErratique(LARGEUR_GRILLE/2 - 1, HAUTEUR_GRILLE/2, island));
		
		singe.deplacerSinge();
		
		assertTrue("Singe déplacé à gauche", singe.coordonneesEgales(LARGEUR_GRILLE/2 + 1, HAUTEUR_GRILLE/2)
				|| singe.coordonneesEgales(LARGEUR_GRILLE/2, HAUTEUR_GRILLE/2 - 1)
				|| singe.coordonneesEgales(LARGEUR_GRILLE/2, HAUTEUR_GRILLE/2 + 1));
	}
	
	/**
	 * Test le non-blocage du singe par la case trésor.
	 */
	@Test
	public void testDeplacementSingeLibreTresor() {
		// On positionne le singe à l'extreme sud-est de la carte.
		singe = new SingeErratique(LARGEUR_GRILLE-2, HAUTEUR_GRILLE-2, island);
		singes.add(singe); // Faux positif FindBugs
						
		// On positionne un autre singe, au dessus. 
		// Seul le déplacement gauche est donc possible.
		singes.add(new SingeErratique(LARGEUR_GRILLE-2, HAUTEUR_GRILLE-3, island));
		
		// On positionne le trésor sur cette case.
		island.getTresor().setPosition(LARGEUR_GRILLE-3, HAUTEUR_GRILLE-2); // Faux positif FindBugs
		
		singe.deplacerSinge();

		assertTrue("Singe non déplacé sur la case trésor", singe.coordonneesEgales(LARGEUR_GRILLE-3, HAUTEUR_GRILLE-2));
		assertTrue("Le trésor a été déplacé", island.getTresor().coordonneesEgales(LARGEUR_GRILLE-3, HAUTEUR_GRILLE-2));
	}
	
	/**
	 * Test le non-blocage du singe par la case pirate, et qu'à son passage, 
	 * le singe assassine le pirate.
	 */
	@Test
	public void testDeplacementSingeLibrePirate() {
		// On positionne le singe à l'extreme sud-est de la carte.
		singe = new SingeErratique(LARGEUR_GRILLE-2, HAUTEUR_GRILLE-2, island);
		singes.add(singe); // Faux positif FindBugs
						
		// On positionne un autre singe, au dessus. 
		// Seul le déplacement gauche est donc possible.
		singes.add(new SingeErratique(LARGEUR_GRILLE-2, HAUTEUR_GRILLE-3, island));
		
		// On positionne le pirate sur cette case.
		island.getPirate().positionInitiale(LARGEUR_GRILLE-3, HAUTEUR_GRILLE-2);
		
		singe.deplacerSinge();

		assertTrue("Singe non déplacé sur la case pirate", singe.coordonneesEgales(LARGEUR_GRILLE-3, HAUTEUR_GRILLE-2));
		assertFalse("Pirate vivant", island.getPirate().isVivant());
	}
	
	/**
	 * Methode permettant de remplir une carte de l'ile selon la taille de
	 * constantes.
	 * 
	 * @return la carte de l'ile.
	 */
	private static int[][] exempleCarte() {
		final int[][] carte = new int[LARGEUR_GRILLE][HAUTEUR_GRILLE];

		int i = -1;
		while (++i < LARGEUR_GRILLE) {
			carte[i][0] = 0;
			carte[i][HAUTEUR_GRILLE - 1] = 0;
		}

		int j = -1;
		while (++j < HAUTEUR_GRILLE) {
			carte[0][j] = 0;
			carte[LARGEUR_GRILLE - 1][j] = 0;
		}
		
		int k = 0;
		int l = 0;
		while (++k < LARGEUR_GRILLE - 1) {
			while(++l < HAUTEUR_GRILLE - 1) {
				carte[k][l] = 1;
			}
		}
		return carte;
	}
	
}
