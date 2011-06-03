package ads1ss11.pa2;

import java.util.Set;
import java.util.ArrayList;

/**
 * In dieser Klasse m&uuml;ssen Sie Ihre Algorithmen implementieren.
 * 
 * <p>
 * Sie m&uuml;ssen mindestens eine der Methoden {@link #findTours1},
 * {@link #findTours2} oder {@link #findTours3} implementieren (k&ouml;nnen aber
 * auch alle drei Methoden ausprogrammieren).
 * </p>
 * 
 * <p>
 * Da die vom Codeger&uuml;st aufgerufenen Methoden keine Parameter
 * &uuml;bergeben bekommen, m&uuml;ssen s&auml;mtliche Initialisierungen im
 * Konstruktor vorgenommen werden.
 * </p>
 * 
 * <p>
 * Sie k&ouml;nnen diese Klasse beliebig erweitern.
 * </p>
 */
public class ToursFinder extends AbstractToursFinder {

	/**
	 * Der Konstruktor f&uuml;r das <code>ToursFinder</code>-Objekt.
	 * 
	 * Diese Methode wird aufgerufen, nachdem die Inputinstanz vollst&auml;ndig
	 * eingelesen wurde. Sie erhalten die Anzahl an Knoten und die Kanten des
	 * Graphen und m&uuml;ssen in dieser Methode alle Initialisierungen
	 * vornehmen, die Sie f&uuml;r Ihre Methoden <code>findTours1-3</code>
	 * brauchen.
	 * 
	 * @param numNodes
	 *            Die Anzahl an Knoten im Inputgraphen. Wenn der Graph <i>n</i>
	 *            Knoten hat, sind die Knoten von 0 bis <i>n - 1</i>
	 *            durchnummeriert.
	 * 
	 * @param edges
	 *            Die Kanten des Inputgraphen.
	 * 
	 * @see #findTours1
	 * @see #findTours2
	 * @see #findTours3
	 */
	public ToursFinder(int numNodes, Set<Edge> edges) {
		super(numNodes, edges);
	}

	@Override
	public ArrayList<ArrayList<Integer>> findTours1() {
		return null;
	}

	@Override
	public ArrayList<ArrayList<Integer>> findTours2() {
		return null;
	}

	@Override
	public ArrayList<ArrayList<Integer>> findTours3() {
		return null;
	}

}
