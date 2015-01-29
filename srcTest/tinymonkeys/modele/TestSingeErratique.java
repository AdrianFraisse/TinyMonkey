package tinymonkeys.modele;

import static org.junit.Assert.*;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSingeErratique {
	
	private static final int NB_IT = 100000;
	private static final int ORDONNEE = 13;
	private static final int ORDONNEE_HAUT = 14;
	private static final int ORDONNEE_BAS = 12;
	private static final int ABSCISSE = 3;
	private static final int ABSCISSE_GAUCHE = 2;
	private static final int ABSCISSE_DROITE = 4;
	
	private SingeErratique singe;
	private Ile islandMock;
	private Pirate pirateMock;

	@Before
	public void setUp() throws Exception {
		islandMock = EasyMock.createMock(Ile.class);
		pirateMock = EasyMock.createMock(Pirate.class);
		singe = new SingeErratique(ABSCISSE, ORDONNEE, islandMock);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetNextRandomPos() {
		// Test case libre
		Capture<Integer> capturedX = EasyMock.newCapture();
		Capture<Integer> capturedY = EasyMock.newCapture();
		
		// La première case valide est retournée
		EasyMock.expect(islandMock.isDeplacementPossible(EasyMock.captureInt(capturedX), EasyMock.captureInt(capturedY))).andReturn(true).once();
		
		EasyMock.replay(islandMock);
		
		final CaseVide caseVide = singe.getNextRandomPos();
		
		assertNotNull(caseVide);
		assertTrue("La case générée ne correspond pas", caseVide.coordonneesEgales(capturedX.getValue().intValue(), capturedY.getValue().intValue()));
		EasyMock.verify(islandMock);
	}
	
	@Test
	public void testDeplacementSinge() {
		EasyMock.expect(islandMock.isDeplacementPossible(EasyMock.anyInt(), EasyMock.anyInt())).andReturn(true).once();
		EasyMock.expect(islandMock.getPirate()).andReturn(pirateMock).once();
		
		pirateMock.tuerPirate(singe);
		EasyMock.expectLastCall().once();
		EasyMock.replay(islandMock);
		EasyMock.replay(pirateMock);
		
		singe.deplacerSinge();
		
		EasyMock.verify(islandMock);
		EasyMock.verify(pirateMock);
		assertTrue("Singe non déplacé", 
				singe.coordonneesEgales(ABSCISSE, ORDONNEE_HAUT)
				|| singe.coordonneesEgales(ABSCISSE, ORDONNEE_BAS)
				|| singe.coordonneesEgales(ABSCISSE_GAUCHE, ORDONNEE)
				|| singe.coordonneesEgales(ABSCISSE_DROITE, ORDONNEE));
	}
	
	/**
	 * Test le blocage du singe dans trois directions
	 */
	@Test
	public void testDeplacementSingeUneDirection() {
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE_DROITE, ORDONNEE)).andStubReturn(false);
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE_GAUCHE, ORDONNEE)).andStubReturn(false);
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE, ORDONNEE_BAS)).andStubReturn(false);
		EasyMock.expect(islandMock.isDeplacementPossible(ABSCISSE, ORDONNEE_HAUT)).andReturn(true).once();
		
		EasyMock.expect(islandMock.getPirate()).andReturn(pirateMock).once();
		pirateMock.tuerPirate(singe);
		EasyMock.expectLastCall().once();
		
		EasyMock.replay(islandMock);
		EasyMock.replay(pirateMock);
		
		singe.deplacerSinge();
		
		EasyMock.verify(islandMock);
		EasyMock.verify(pirateMock);
		
		assertTrue("Singe non déplacé sur la case haute", singe.coordonneesEgales(ABSCISSE, ORDONNEE_HAUT));
	}
	
	@Test
	public void testDeplacementSingeImpossible() {
		EasyMock.expect(islandMock.isDeplacementPossible(EasyMock.anyInt(), EasyMock.anyInt())).andStubReturn(false);
		
		EasyMock.replay(islandMock);
		
		singe.deplacerSinge();
		
		EasyMock.verify(islandMock);
		
		assertTrue("Singe non déplacé", singe.coordonneesEgales(ABSCISSE, ORDONNEE));
	}
	
	@Test
	public void testEquiprobabilite4Cases() {
		EasyMock.expect(islandMock.isDeplacementPossible(EasyMock.anyInt(), EasyMock.anyInt())).andStubReturn(true);
		
		EasyMock.expect(islandMock.getPirate()).andStubReturn(pirateMock);
		pirateMock.tuerPirate(singe);
		EasyMock.expectLastCall().anyTimes();
		
		EasyMock.replay(islandMock);
		EasyMock.replay(pirateMock);
		
		int deplacementsHaut = 0;
		int deplacementsBas = 0;
		int deplacementsGauche = 0;
		int deplacementsDroite = 0;
		
		int previousX = ABSCISSE;
		int previousY = ORDONNEE;
		
		for (int i = 0; i < NB_IT; i++) {
			singe.deplacerSinge();
			if (singe.x == previousX && singe.y == (previousY + 1)) deplacementsHaut++;
			else if (singe.x == previousX && singe.y == (previousY - 1)) deplacementsBas++;
			else if (singe.x == (previousX + 1) && singe.y == previousY) deplacementsDroite++;
			else if (singe.x == (previousX - 1) && singe.y == previousY) deplacementsGauche++;
			previousX = singe.x;
			previousY = singe.y;
		}
		
		assertTrue("Equiprobabilité des 4 cases - Deplacement Bas", (((float) deplacementsBas/NB_IT) > 0.24 &&  0.26 > ((float) deplacementsBas/NB_IT)));
		assertTrue("Equiprobabilité des 4 cases - Deplacement Haut", (((float) deplacementsHaut/NB_IT) > 0.24 &&  0.26 > ((float) deplacementsHaut/NB_IT)));
		assertTrue("Equiprobabilité des 4 cases - Deplacement Gauche", (((float) deplacementsGauche/NB_IT) > 0.24 &&  0.26 > ((float) deplacementsGauche/NB_IT)));
		assertTrue("Equiprobabilité des 4 cases - Deplacement Droite", (((float) deplacementsDroite/NB_IT) > 0.24 &&  0.26 > ((float) deplacementsDroite/NB_IT)));
		
		EasyMock.verify(islandMock);
		EasyMock.verify(pirateMock);
	}
	
	@Test
	public void testEquiprobabilite3Cases() {
		
		EasyMock.expect(islandMock.isDeplacementPossible(EasyMock.anyInt(), EasyMock.anyInt())).andStubAnswer(
				new IAnswer<Boolean>() {

					@Override
					public Boolean answer() throws Throwable {
						int x = (int) EasyMock.getCurrentArguments()[0];
						int y = (int) EasyMock.getCurrentArguments()[1];
						return !(x == singe.getX() && y == singe.getY() + 1);
					}
				});
		
		EasyMock.expect(islandMock.getPirate()).andStubReturn(pirateMock);
		pirateMock.tuerPirate(singe);
		EasyMock.expectLastCall().anyTimes();
		
		EasyMock.replay(islandMock);
		EasyMock.replay(pirateMock);
		
		int deplacementsHaut = 0;
		int deplacementsBas = 0;
		int deplacementsGauche = 0;
		int deplacementsDroite = 0;
		
		int previousX = ABSCISSE;
		int previousY = ORDONNEE;
		
		for (int i = 0; i < NB_IT; i++) {
			singe.deplacerSinge();
			if (singe.x == previousX && singe.y == (previousY + 1)) deplacementsHaut++;
			else if (singe.x == previousX && singe.y == (previousY - 1)) deplacementsBas++;
			else if (singe.x == (previousX + 1) && singe.y == previousY) deplacementsDroite++;
			else if (singe.x == (previousX - 1) && singe.y == previousY) deplacementsGauche++;
			previousX = singe.x;
			previousY = singe.y;
		}
		
		assertTrue("Equiprobabilité des 4 cases - Deplacement Bas", (((float) deplacementsBas/NB_IT) > 0.32 &&  0.34 > ((float) deplacementsBas/NB_IT)));
		assertTrue("Equiprobabilité des 4 cases - Aucun deplacement Haut", (deplacementsHaut == 0));
		assertTrue("Equiprobabilité des 4 cases - Deplacement Gauche", (((float) deplacementsGauche/NB_IT) > 0.32 &&  0.34 > ((float) deplacementsGauche/NB_IT)));
		assertTrue("Equiprobabilité des 4 cases - Deplacement Droite", (((float) deplacementsDroite/NB_IT) > 0.32 &&  0.34 > ((float) deplacementsDroite/NB_IT)));
		
		EasyMock.verify(islandMock);
		EasyMock.verify(pirateMock);
	}
	
	@Test
	public void testEquiprobabilite2Cases() {
		
		EasyMock.expect(islandMock.isDeplacementPossible(EasyMock.anyInt(), EasyMock.anyInt())).andStubAnswer(
				new IAnswer<Boolean>() {

					@Override
					public Boolean answer() throws Throwable {
						int x = (int) EasyMock.getCurrentArguments()[0];
						int y = (int) EasyMock.getCurrentArguments()[1];
						return !((x == singe.getX() && y == singe.getY() + 1) || (x == singe.getX() - 1 && y == singe.getY()));
					}
				});
		
		EasyMock.expect(islandMock.getPirate()).andStubReturn(pirateMock);
		pirateMock.tuerPirate(singe);
		EasyMock.expectLastCall().anyTimes();
		
		EasyMock.replay(islandMock);
		EasyMock.replay(pirateMock);
		
		int deplacementsHaut = 0;
		int deplacementsBas = 0;
		int deplacementsGauche = 0;
		int deplacementsDroite = 0;
		
		int previousX = ABSCISSE;
		int previousY = ORDONNEE;
		
		for (int i = 0; i < NB_IT; i++) {
			singe.deplacerSinge();
			if (singe.x == previousX && singe.y == (previousY + 1)) deplacementsHaut++;
			else if (singe.x == previousX && singe.y == (previousY - 1)) deplacementsBas++;
			else if (singe.x == (previousX + 1) && singe.y == previousY) deplacementsDroite++;
			else if (singe.x == (previousX - 1) && singe.y == previousY) deplacementsGauche++;
			previousX = singe.x;
			previousY = singe.y;
		}
		
		assertTrue("Equiprobabilité des 4 cases - Deplacement Bas", (((float) deplacementsBas/NB_IT) > 0.49 &&  0.51 > ((float) deplacementsBas/NB_IT)));
		assertTrue("Equiprobabilité des 4 cases - Aucun deplacement Haut", (deplacementsHaut == 0));
		assertTrue("Equiprobabilité des 4 cases - Aucun deplacement Gauche", (deplacementsGauche == 0));
		assertTrue("Equiprobabilité des 4 cases - Deplacement Droite", (((float) deplacementsDroite/NB_IT) > 0.49 &&  0.51 > ((float) deplacementsDroite/NB_IT)));
		
		EasyMock.verify(islandMock);
		EasyMock.verify(pirateMock);
	}
	
}
