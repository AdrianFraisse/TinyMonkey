package tinymonkeys;

import com.greenspector.probe.wrappers.java.JavaProbeWrapper;

import tinymonkeys.controleur.Controleur;

/**
 * Classe principale de TinyMonkeys.
 * 
 * @version 1.0
 * @author Camille Constant
 *
 */
final public class TinyMonkeys {

	/**
	 * Constructeur privé de TinyMonkeys.
	 * 
	 * Ce constructeur privé assure la non-instanciation de TinyMonkeys dans un
	 * programme. (TinyMonkeys est la classe principale du programme
	 * TinyMonkeys)
	 */
	private TinyMonkeys() {
		// Constructeur privé pour assurer la non-instanciation de TinyMonkeys.
	}

	/**
	 * Main du programme.
	 * 
	 * @param args arguments.
	 */
	public static void main(String[] args) {
		final Controleur c = new Controleur();
		c.lanceEvolutionsPersonnages();
		
		final JavaProbeWrapper javaProbeWrapper = new JavaProbeWrapper();
		javaProbeWrapper.SetUpMeasures("java", 6000, 1000, false,"");
		
		new Thread(new Runnable() {
		
			public final void run() {
				try {
					javaProbeWrapper.Start();
					Thread.sleep(1000*15);
					javaProbeWrapper.StopMeasure("probe_tinymonkeys", 1);
					System.exit(0);
					
				} catch (InterruptedException e) {
					System.exit(0);
				}
				
			}
		}).start();
		
	}
	

}
