package ads1ss11.pa2;

import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;

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

	private int[][] weightTable = null;
	private boolean[][] usedEdge = null;

	private int[][] twoOptTour = null;

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
		buildWeightTable();
		setAllEdgesUnused();
	}

	@Override
	public ArrayList<ArrayList<Integer>> findTours1() {
		int[][] tourArr = brainlessDivision();

		Main.printDebug("brainless gets "+totalWeight(tourArr));

		setAllEdgesUnused();
		setEdgesUsed(tourArr[0]);
		setEdgesUsed(tourArr[1]);
		int oldWeight = -1, newWeight = -2;
		while (true) {
			twoOpt(tourArr[0]);
			twoOpt(tourArr[1]);
			newWeight = totalWeight(tourArr);
			Main.printDebug("2-opt gets "+newWeight);
			if (newWeight == oldWeight)
				break;
			oldWeight = newWeight;
		}
		twoOptTour = tourArr;

		return intArrayArray2ArrayListOfArrayList(tourArr);
	}

	@Override
	public ArrayList<ArrayList<Integer>> findTours2() {
		int[][] tourArr = twoOptTour;

		Main.printDebug("starting with tour of weight "+totalWeight(tourArr));

		int oldWeight = -1, newWeight = -2;
		while (true) {
			threeOpt(tourArr[0]);
			threeOpt(tourArr[1]);
			newWeight = totalWeight(tourArr);
			Main.printDebug("3-opt gets "+newWeight);
			if (newWeight == oldWeight)
				break;
			oldWeight = newWeight;
		}

		return intArrayArray2ArrayListOfArrayList(tourArr);
	}

	@Override
	public ArrayList<ArrayList<Integer>> findTours3() {
		return null;
	}

	/* a brainless algorithm, that just finds two valid tours */
	private int[][] brainlessDivision() {
		int[] tour1 = new int[numNodes];
		int[] tour2 = new int[numNodes];
		for (int i=0;i<numNodes;i++)
			tour1[i] = i;
		if ((numNodes % 2) == 1) {
			for (int i=0;i<numNodes;i++)
				tour2[i] = (2*i) % numNodes;
		} else {
			for (int i=0;i<numNodes;i++)
				tour2[i] = modulo(((i<numNodes/2)?(2*i):(1-2*i)),numNodes);
		}
		return new int[][]{tour1, tour2};
	}

	/* adapted 3-opt algorithm */
	private void threeOpt(int[] tour) {
		for (int i=0;i<numNodes;i++) {
			for (int j=i+2;j<numNodes;j++) {
				for (int k=j+2;k<numNodes&&(i>0||k<numNodes-1);k++) {
					threeOptThreeNodes(tour, new int[]{i, j, k});
				}
			}
		}
	}

	private int[][] threePermutations = new int[][]{
		new int[]{0,1, 2,3, 4,5}, // identity
		new int[]{0,2, 1,4, 3,5},

		//new int[]{0,2, 1,4, 3,5},
		//new int[]{0,3, 4,2, 1,5},
		//new int[]{0,4, 3,1, 2,5},
		//new int[]{0,3, 4,1, 2,5}
	};

	/* 3-opt algorithm, part of the algorithm */
	private void threeOptThreeNodes(int[] tour, int[] sel) {
		int nodes[] = new int[]{
			tour[sel[0]], tour[(sel[0]+1)%numNodes],
			tour[sel[1]], tour[(sel[1]+1)%numNodes],
			tour[sel[2]], tour[(sel[2]+1)%numNodes]
		};
		// find best permutation
		int bestPermIdx = bestThreePerm(nodes);
		// exchange nodes
		if (bestPermIdx != 0)
			doThreePerm(tour, sel, nodes, threePermutations[bestPermIdx]);
	}

	/* 3-opt algorithm, part of the algorithm */
	private void doThreePerm(int[] tour, int[] sel, int[] nodes, int[] perm) {
		// change used state
		for (int i=0;i<3;i++) {
			setEdgeUsed(nodes[2*i], nodes[2*i+1], false);
		}
		for (int i=0;i<3;i++) {
			setEdgeUsed(nodes[perm[2*i]], nodes[perm[2*i+1]], true);
		}
		// determine what to flip
		boolean flipAll = (perm[1]!=1 && perm[2]!=1);
		boolean flipFirst = (perm[1]!=1 && perm[4]!=1);
		boolean flipSecond = (perm[3]!=3 && perm[2]!=3);
		//Main.printDebug("flip plan: "+flipFirst+" "+flipSecond+" "+flipAll);
		// and flip it
		if (flipFirst)
			flip(tour, sel[0]+1, sel[1]+1);
		if (flipSecond)
			flip(tour, sel[1]+1, sel[2]+1);
		if (flipAll)
			flip(tour, sel[0]+1, sel[2]+1);
	}

	/* flip range from inclusively start to exlusively end */
	private static void flip(int[] tour, int start, int end) {
		for (int i=0; i<(end-start)/2; i++) {
			int tmp = tour[start+i];
			tour[start+i] = tour[end-1-i];
			tour[end-1-i] = tmp;
		}
	}

	/* 3-opt algorithm, find best possible permutation in threePermutations */
	private int bestThreePerm(int[] nodes) {
		int origWeight = -1;
		int bestPerm = -1;
		int bestWeight = Integer.MAX_VALUE;
		for (int p = 0; p < threePermutations.length; p++) {
			int[] perm = threePermutations[p];
			boolean collision = false;
			int curWeight = 0;
			for (int i=0;i<3;i++) {
				curWeight += weightTable[nodes[perm[2*i]]][nodes[perm[2*i+1]]];
				if (p != 0 &&   usedEdge[nodes[perm[2*i]]][nodes[perm[2*i+1]]]) {
					collision = true;
					break;
				}
			}
			if (p == 0)
				origWeight = curWeight;
			if (collision)
				continue;
			if (curWeight < bestWeight) {
				bestWeight = curWeight;
				bestPerm = p;
			}
		}
		//if (bestPerm != 0) {
		//	Main.printDebug("origWeight: "+origWeight+
		//			" bestWeight: "+bestWeight+
		//			" bestPerm: "+bestPerm);
		//}
		return bestPerm;
	}

	/* adapted 2-opt algorithm */
	private void twoOpt(int[] tour) {
		for (int i=0;i<numNodes;i++) {
			for (int j=i+2;j<numNodes&&(i>0||j<numNodes-1);j++) {
				twoOptTwoNodes(tour, i, j);
			}
		}
	}

	/* adapted 2-opt algorithm, part of the algorithm */
	private void twoOptTwoNodes(int[] tour, int i, int j) {
		if (shouldExchange(tour, i, j)) {
			exchangeTwoEdges(tour, i, j);
		}
	}

	/* adapted 2-opt algorithm, part of the algorithm */
	private boolean shouldExchange(int[] tour, int i, int j) {
		int a = tour[i];
		int b = tour[(i+1)%numNodes];
		int c = tour[j];
		int d = tour[(j+1)%numNodes];
		if (isEdgeUsed(a,c) || isEdgeUsed(b,d))
			return false;
		int cur = weightTable[a][b] + weightTable[c][d];
		int alt = weightTable[a][c] + weightTable[b][d];
		//if (alt < cur) Main.printDebug("exchecker: alt="+alt+" < "+cur+"=cur");
		return alt < cur;
	}

	/* adapted 2-opt algorithm, part of the algorithm */
	private void exchangeTwoEdges(int[] tour, int i, int j) {
		//Main.printDebug("exchange "+i+" and "+j);
		int a = tour[i];
		int b = tour[(i+1)%numNodes];
		int c = tour[j];
		int d = tour[(j+1)%numNodes];
		setEdgeUsed(a,b,false);
		setEdgeUsed(c,d,false);
		setEdgeUsed(a,c,true);
		setEdgeUsed(b,d,true);
		flip(tour, i+1, j+1);
	}

	/* get total weight of one tour, given an array of nodes */
	private int totalWeight(int[] tour) {
		int sum=0;
		for (int i=0; i<tour.length; i++) {
			sum += weightTable[tour[i]][tour[(i+1)%tour.length]];
		}
		return sum;
	}

	/* get total weight of two tours, given an array of two arrays of nodes */
	private int totalWeight(int[][] tours) {
		return totalWeight(tours[0]) + totalWeight(tours[1]);
	}

	/* initialization of weight table */
	private void buildWeightTable() {
		weightTable = new int[numNodes][];
		for (int i=0;i<numNodes;i++) {
			weightTable[i] = new int[numNodes];
			weightTable[i][i] = 0;
		}
		for (Edge edge : edges) {
			weightTable[edge.node1][edge.node2] = edge.weight;
			weightTable[edge.node2][edge.node1] = edge.weight;
		}
	}

	/* used edges management */
	private void setAllEdgesUnused() {
		boolean newPlease = (usedEdge == null);
		if (newPlease)
			usedEdge = new boolean[numNodes][];
		for (int i=0;i<numNodes;i++) {
			if (newPlease)
				usedEdge[i] = new boolean[numNodes];
			for (int j=0;j<numNodes;j++)
				usedEdge[i][j] = false;
		}
	}

	/* used edges management */
	private void setEdgeUsed(int i, int j, boolean state) {
		usedEdge[i][j] = state;
		usedEdge[j][i] = state;
	}

	/* used edges management */
	private void setEdgesUsed(int[] tour) {
		for (int i=0;i<numNodes;i++)
			setEdgeUsed(tour[i], tour[(i+1)%numNodes], true);
	}

	/* used edges management */
	private boolean isEdgeUsed(int i, int j) {
		return usedEdge[i][j];
	}

	/* helper method, because Java's %-op sucks */
	private static int modulo(int x, int n) {
		return ((x%n)+n)%n;
	}

	/* helper method */
	private static ArrayList<ArrayList<Integer>> listOf2Lists(ArrayList<Integer> list1, ArrayList<Integer> list2) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		result.add(list1);
		result.add(list2);
		return result;
	}

	/* helper method */
	private static ArrayList<Integer> intArray2ArrayList(int[] array, int length) {
		ArrayList<Integer> list = new ArrayList<Integer>(length);
		for (int i = 0; i < length; i++)
			list.add(array[i]);
		return list;
	}

	/* helper method */
	private static ArrayList<Integer> intArray2ArrayList(int[] array) {
		return intArray2ArrayList(array, array.length);
	}

	/* helper method */
	private static ArrayList<ArrayList<Integer>> intArrayArray2ArrayListOfArrayList(int[][] array) {
		return listOf2Lists(intArray2ArrayList(array[0]), intArray2ArrayList(array[1]));
	}

}
