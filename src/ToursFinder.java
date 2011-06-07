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
		ArrayList<ArrayList<Integer>> paths = brainlessDivision();
		return paths;
	}

	@Override
	public ArrayList<ArrayList<Integer>> findTours2() {
		return null;
	}

	@Override
	public ArrayList<ArrayList<Integer>> findTours3() {
		return null;
	}

	private ArrayList<ArrayList<Integer>> brainlessDivision() {
		ArrayList<Integer> tour1 = new ArrayList<Integer>(numNodes);
		ArrayList<Integer> tour2 = new ArrayList<Integer>(numNodes);
		for (int i=0;i<numNodes;i++)
			tour1.add(i);
		if ((numNodes % 2) == 1) {
			for (int i=0;i<numNodes;i++)
				tour2.add((2*i) % numNodes);
		} else {
			for (int i=0;i<numNodes;i++)
				tour2.add(modulo(((i<numNodes/2)?(2*i):(1-2*i)),numNodes));
		}
		return listOf2Lists(tour1, tour2);
	}

	private int modulo(int x, int n) {
		return ((x%n)+n)%n;
	}

	private ArrayList<ArrayList<Integer>> listOf2Lists(ArrayList<Integer> list1, ArrayList<Integer> list2) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		result.add(list1);
		result.add(list2);
		return result;
	}

	private ArrayList<Integer> intArray2ArrayList(int[] array, int length) {
		ArrayList<Integer> list = new ArrayList<Integer>(length);
		for (int i = 0; i < length; i++)
			list.add(array[i]);
		return list;
	}

	private ArrayList<Integer> intArray2ArrayList(int[] array) {
		return intArray2ArrayList(array, array.length);
	}

}
