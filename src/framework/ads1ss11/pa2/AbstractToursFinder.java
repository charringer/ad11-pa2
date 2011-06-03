package ads1ss11.pa2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import ads1.graphprinter.GraphPrinter;
import ads1.graphprinter.Traversable;
import ads1.graphprinter.attributes.*;

/**
 * Abstrakte Klasse zur Kapselung eines {@link ToursFinder} Objekts.
 * 
 * <p>
 * <b>WICHTIG:</b> Nehmen Sie keine &Auml;nderungen in dieser Klasse vor. Bei
 * der Abgabe werden diese &Auml;nderungen verworfen und es k&ouml;nnte dadurch
 * passieren, dass Ihr Programm somit nicht mehr korrekt funktioniert.
 * </p>
 */
abstract public class AbstractToursFinder {

	/**
	 * Anzahl der Knoten
	 */
	final protected int numNodes;
	/**
	 * Menge aller Kanten
	 */
	protected Set<Edge> edges;
	
	/**
	 * Der Konstruktor f&uuml;r das <code>AbstractToursFinder</code>-Objekt.
	 * 
	 * @param numNodes
	 *            Die Anzahl an Knoten im Inputgraphen. Wenn der Graph <i>n</i>
	 *            Knoten hat, sind die Knoten von 0 bis <i>n - 1</i>
	 *            durchnummeriert.
	 * 
	 * @param edges
	 *            Kanten des Inputgraphen.
	 * 
	 * @see #findTours1()
	 * @see #findTours2()
	 * @see #findTours3()
	 */
	public AbstractToursFinder(int numNodes, Set<Edge> edges) {
		this.numNodes = numNodes;
		this.edges = edges;
	}

	/**
	 * Eine der von Ihnen zu implementierenden Heuristiken.
	 * 
	 * <p>
	 * S&auml;mtliche Initialisierungen m&uuml;ssen schon im Konstruktor
	 * vorgenommen werden.
	 * </p>
	 * 
	 * <p>
	 * Sie m&uuml;ssen mindestens eine der Methoden <code>findTours1-3</code>
	 * implementieren. Wenn Sie weniger als drei Aglorithmen implementieren
	 * wollen, m&uuml;ssen die &uuml;brigen Methoden einfach <code>null</code>
	 * zur&uuml;ckliefern.
	 * </p>
	 * 
	 * @return Eine {@link ArrayList} die zwei weitere <code>ArrayList</code>s
	 *         enth&auml;lt (eine pro Tour), in der die Knoten in der
	 *         Reihenfolge angef&uuml;hrt sind, in der sie besucht werden oder
	 *         <code>null</code>, wenn <code>findTours1</code> nicht
	 *         implementiert wurde.
	 */
	abstract public ArrayList<ArrayList<Integer>> findTours1();

	/**
	 * Eine der von Ihnen zu implementierenden Heuristiken.
	 * 
	 * @see #findTours1
	 */
	abstract public ArrayList<ArrayList<Integer>> findTours2();

	/**
	 * Eine der von Ihnen zu implementierenden Heuristiken.
	 * 
	 * @see #findTours1
	 */
	abstract public ArrayList<ArrayList<Integer>> findTours3();

	
	
	private Map<Long, Integer> weights;
	
	/**
	 * TourPrinter Klasse
	 * <p>
	 * Diese Klasse erm&ouml;glicht es, die Pfade die Ihre Heurstik gefunden hat
	 * darzustellen. Weitere Informationen finden Sie in der GraphPrinter
	 * Dokumentation.
	 * </p>
	 * 
	 * @see GraphPrinter
	 */
	protected class TourPrinter implements Traversable {
		private TourPrinter[] all_nodes;
		private int node;
		private int[] node_idx;
		private int[] prev_node;
		private int[] next_node;
		private int[] prev_idx;
		private int[] next_idx;
		private TourPrinter[] children;

		private long calc_index(int node1, int node2) {
			if (node1 < node2) {
				return (((long) node1) << 32) | (long) node2;
			} else {
				return (((long) node2) << 32) | (long) node1;
			}
		}

		/**
		 * Konstruktor
		 * 
		 * <h3>Beispiel (z.B. am Ende von {@link ToursFinder#findTours1()})</h3>
		 * <p>
		 * <code>
		   TourPrinter[] arr = new TourPrinter[numNodes];
		   for (int i = 0; i < numNodes; i++) {
			 arr[i] = new TourPrinter(i, tours, arr);
		   }
		   Main.snapshot(arr, "heuristic_1_path", false);
		 </code>
		 * Wobei <code>tours</code> dem R&uuml;ckgabewert entspricht.
		 * </p>
		 * 
		 * @param node
		 *            Nummer des Knotens
		 * @param paths
		 *            L&ouml;sung einer Heuristik
		 * @param all_nodes
		 *            Array aller TourPrinter Knoten
		 */
		public TourPrinter(int node, ArrayList<ArrayList<Integer>> paths,
				TourPrinter[] all_nodes) {
			assert (paths.size() == 2);
			node_idx = new int[2];
			prev_node = new int[2];
			next_node = new int[2];
			prev_idx = new int[2];
			next_idx = new int[2];
			this.node = node;
			this.all_nodes = all_nodes;

			if (weights == null) {
				weights = new TreeMap<Long, Integer>();
				for (Edge e : edges) {
					long l = calc_index(e.node1, e.node2);
					Integer i = weights.put(l, e.weight);
					assert (i == null);
				}
			}

			for (int i = 0; i < node_idx.length; i++) {
				node_idx[i] = paths.get(i).indexOf(node);
				prev_idx[i] = (node_idx[i] + paths.get(i).size() - 1)
						% paths.get(i).size();
				prev_node[i] = paths.get(i).get(prev_idx[i]);
				next_idx[i] = (node_idx[i] + 1) % paths.get(i).size();
				next_node[i] = paths.get(i).get(next_idx[i]);

			}

		}

		@Override
		public Traversable[] getChildren() {
			if (children == null) {
				children = new TourPrinter[4];
				for (int i = 0; i < 2; i++) {
					if (node < prev_node[i])
						children[2 * i] = all_nodes[prev_node[i]];
					if (node < next_node[i])
						children[2 * i + 1] = all_nodes[next_node[i]];
				}
			}
			return children;
		}

		@Override
		public String toString() {
			return "[node=" + node + "]";
		}

		@Override
		public List<GPAttribute> getEdgeAttributes(int index) {
			if (children == null || children[index] == null)
				return null;
			List<GPAttribute> attr = new LinkedList<GPAttribute>();

			int neighbor = children[index].node;

			assert (node < neighbor);

			LineColorAttr[] col = new LineColorAttr[2];
			col[0] = LineColorAttr.BLUE;
			col[1] = LineColorAttr.RED;

			for (int i = 0; i < 2; i++) {
				if (neighbor == prev_node[i] || neighbor == next_node[i]) {
					attr.add(col[i]);
				}
			}

			assert (node < neighbor);

			long l = calc_index(node, neighbor);
			attr.add(new LabelAttr(weights.get(l) + ""));

			return attr;
		}

		@Override
		public int getID() {
			return node;
		}

		@Override
		public String getLabel() {
			return "" + node;
		}

		@Override
		public List<GPAttribute> getNodeAttributes() {
			return null;
		}

	}

}
