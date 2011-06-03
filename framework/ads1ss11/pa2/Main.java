package ads1ss11.pa2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import ads1.graphprinter.GraphPrinter;
import ads1.graphprinter.Traversable;

/**
 * Diese Klasse enth&auml;lt nur die {@link #main main()}-Methode zum Starten
 * des Programms, sowie {@link #printDebug(String)} zum Ausgeben von Debug
 * Meldungen.
 * 
 * <p>
 * <b>WICHTIG:</b> Nehmen Sie keine &Auml;nderungen in dieser Klasse vor. Bei
 * der Abgabe werden diese &Auml;nderungen verworfen und es k&ouml;nnte dadurch
 * passieren, dass Ihr Programm somit nicht mehr korrekt funktioniert.
 * </p>
 */
public class Main {
	private static GraphPrinter gp = null;

	/**
	 * Erzeugt einen neuen Snapshot von einem Traversable Objekt
	 * 
	 * @param t
	 *            Array von traversierbares Objekten
	 * 
	 *            Es gen&uuml;gt einen (Start)Knoten jeder
	 *            Zusammenhangskomponente zu &uuml;bergeben.
	 * @param label
	 *            Bezeichner des Snapshots
	 * @param digraph
	 *            Gibt an ob es sich um einen gerichteten Graphen handelt
	 */
	public static void snapshot(Traversable[] t, String label, boolean digraph) {
		if (gp != null) {
			gp.put(label, t, digraph);
		}
	}

	/**
	 * &Uuml;berpr&uuml;ft ob der GraphPrinter aktiv ist
	 * 
	 * @return Liefert <code>true</code> wenn der GraphPrinter aktiv ist
	 */
	public static boolean gp() {
		return gp != null;
	}

	private static void printSnapshots() {
		if (gp != null) {
			gp.print();
		}
	}

	/**
	 * Der Name der Datei, aus der die Testinstanz auszulesen ist. Ist <code>
	 * null</code>
	 * , wenn von {@link System#in} eingelesen wird.
	 */
	private static String fileName = null;

	/** Der Schwellwert, der unterschritten werden muss */
	private static int threshold;

	/** Der abgeschnittene Pfad */
	private static String choppedFileName;

	/** Test flag f&uuml;r Laufzeit Ausgabe */
	private static boolean test = false;

	/** Debug flag f&uuml;r zus&auml;tzliche Debug Ausgaben */
	private static boolean debug = false;

	/** Anzahl der Knoten im Graph */
	private static int numNodes;

	/** Anzahl der Kanten im Graph */
	private static int numEdges;

	/** Adjazenzmatrix */
	private static int[][] am;

	/** Menge aller Kanten */
	private static HashSet<Edge> originalEdges;

	/**
	 * Gibt die Meldung <code>msg</code> aus und beendet das Programm.
	 * 
	 * @param msg
	 *            Die Meldung die ausgegeben werden soll.
	 */
	private static void bailOut(String msg) {
		System.out.println();
		System.err.println((test ? choppedFileName + ": " : "") + "ERR " + msg);
		System.exit(1);
	}

	/**
	 * Generates a chopped String representation of the filename.
	 */
	private static void chopFileName() {
		if (fileName == null) {
			choppedFileName = "System.in";
			return;
		}

		int i = fileName.lastIndexOf(File.separatorChar);

		if (i > 0)
			i = fileName.lastIndexOf(File.separatorChar, i - 1);
		if (i == -1)
			i = 0;

		choppedFileName = ((i > 0) ? "..." : "") + fileName.substring(i);
	}

	/**
	 * Gibt eine debugging Meldung aus. Wenn das Programm mit <code>-d</code>
	 * gestartet wurde, wird <code>msg</code> zusammen mit dem Dateinamen der
	 * Inputinstanz ausgegeben, ansonsten macht diese Methode nichts.
	 * 
	 * @param msg
	 *            Text der ausgegeben werden soll.
	 */
	public static void printDebug(String msg) {
		if (!debug)
			return;

		System.out.println(choppedFileName + ": DBG " + msg);
	}

	/**
	 * Gibt eine debugging Meldung aus. Wenn das Programm mit <code>-d</code>
	 * gestartet wurde, wird <code>msg</code> zusammen mit dem Dateinamen der
	 * Inputinstanz ausgegeben, ansonsten macht diese Methode nichts.
	 * 
	 * @param msg
	 *            Object das ausgegeben werden soll.
	 */
	public static void printDebug(Object msg) {
		printDebug(msg.toString());
	}

	/**
	 * Liest die Daten einer Testinstanz ein und ruft Ihre Heuristiken in
	 * {@link ToursFinder} auf. Anschlie&szlig;end werden die L&ouml;sungen auf
	 * ihre Korrektheit gepr&uuml;ft und der beste Wert mit dem Schwellwert
	 * verglichen. Ist der Wert &uuml;ber dem Schwellwert wird ein Fehler
	 * ausgegeben, ansonsten eine Erfolgsmeldung.
	 * 
	 * <p>
	 * Wenn auf der Kommandozeile die Option <code>-d</code> angegeben wird,
	 * werden s&auml;mtliche Strings, die an {@link Main#printDebug(String)}
	 * &uuml;bergeben werden, ausgegeben.
	 * </p>
	 * 
	 * <p>
	 * Der erste String in <code>args</code>, der <em>nicht</em> mit <code>-d
	 * </code>,
	 * oder <code>-t</code> beginnt, wird als der Pfad zur Datei interpretiert,
	 * aus der die Testinstanz auszulesen ist. Alle nachfolgenden Parameter
	 * werden ignoriert. Wird kein Dateiname angegeben, wird die Testinstanz
	 * &uuml;ber {@link System#in} eingelesen.
	 * </p>
	 * 
	 * @param args
	 *            Die von der Kommandozeile &uuml;bergebene Argumente. Die
	 *            Option <code>-d</code> aktiviert debug-Ausgaben &uuml;ber
	 *            {@link #printDebug(String)}, <code>-t</code> gibt
	 *            zus&auml;tzlich Dateiname und Laufzeit aus. Der erste andere
	 *            String wird als Dateiname interpretiert.
	 */
	public static void main(String[] args) {
		List<String> listArgs = new LinkedList<String>(Arrays.asList(args));
		gp = GraphPrinter.ParseArgs(listArgs);
		args = listArgs.toArray(new String[0]);

		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		long offs = end - start;
		int bestVal = Integer.MAX_VALUE;

		processArgs(args);
		chopFileName();

		try {
			// Setting security manager
			SecurityManager sm = new ADS1SecurityManager();
			System.setSecurityManager(sm);
		} catch (SecurityException e) {
			bailOut("Error: could not set security manager: " + e);
		}

		try {
			// read input
			readInput();

			start = System.currentTimeMillis();

			AbstractToursFinder tf = new ToursFinder(numNodes, originalEdges);

			// call heuristics
			bestVal = compare(bestVal, tf.findTours1());
			bestVal = compare(bestVal, tf.findTours2());
			bestVal = compare(bestVal, tf.findTours3());

			end = System.currentTimeMillis();

			// unset user object for security reasons
			tf = null;
			System.setSecurityManager(null);

			printSnapshots();

			// output result
			StringBuffer msg = new StringBuffer(test ? choppedFileName + ": "
					: "");

			long sum = end - start - offs;

			if (bestVal == Integer.MAX_VALUE) {
				msg.append("ERR keine gueltige Loesung");
			} else if (bestVal > threshold) {
				msg.append("ERR zu schlechte Loesung: Ihr Ergebnis "
						+ (bestVal) + " liegt ueber dem Schwellwert ("
						+ threshold + ")");
			} else {
				msg.append("Ihr Wert ist unter dem Schwellwert\n" + (bestVal));
			}

			if (test)
				msg.append(", Zeit: " + sum + " ms");

			System.out.println();
			System.out.println(msg.toString());

		} catch (SecurityException se) {
			bailOut("Unerlaubter Funktionsaufruf: \"" + se.toString() + "\"");
		} catch (NumberFormatException e) {
			bailOut("Falsches Inputformat: \"" + e.toString() + "\"");
		} catch (Exception e) {
			e.printStackTrace();
			bailOut("caught exception \"" + e.toString() + "\"");
		}

	}

	/**
	 * Reads the input from <code>filename</code> if it is not null, or
	 * <code>System.in</code> otherwise.
	 * 
	 * @throws Exception
	 *             if an IO- or parser-error occurs.
	 */
	private static void readInput() throws Exception {
		InputStream is = System.in;

		if (fileName != null)
			try {
				is = new FileInputStream(fileName);
			} catch (Exception e) {
				System.err.println("WARN could not open " + "file \""
						+ fileName + "\", reading " + "from System.in");
			}

		BufferedReader bin = new BufferedReader(new InputStreamReader(is));
		String[] config = new String[3];

		for (int i = 0; i < config.length; i++) {
			config[i] = bin.readLine();
			if (config[i].length() < 1) {
				i--;
				continue;
			}
			// comments
			if (config[i].charAt(0) == '#') {
				i--;
				continue;
			}
		}

		numNodes = Integer.valueOf(config[0]);
		numEdges = Integer.valueOf(config[1]);
		threshold = Integer.valueOf(config[2]);

		System.out.println();
		printDebug("Anzahl der Knoten: " + numNodes);
		printDebug("Anzahl der Kanten: " + numEdges);
		printDebug("Schwellwert:       " + threshold);

		am = new int[numNodes][numNodes];
		originalEdges = new HashSet<Edge>(numEdges);

		String line;
		while ((line = bin.readLine()) != null) {
			if (line.length() < 1)
				continue;

			// comments
			if (line.charAt(0) == '#')
				continue;

			String l[] = line.split(" ");

			if (l.length != 3)
				continue;

			int n1 = Integer.valueOf(l[0]);

			assert (n1 < numNodes);

			int n2 = Integer.valueOf(l[1]);
			int w = Integer.valueOf(l[2]);

			assert (n2 < numNodes);

			am[n1][n2] = am[n2][n1] = w;
			originalEdges.add(new Edge(n1, n2, w));
		}

		assert (originalEdges.size() == numEdges);
	}

	/**
	 * Compares a new solution to the so far best value. Checks if the new
	 * solution is valid.
	 * 
	 * @param bestVal
	 *            So far best value.
	 * @param sol
	 *            New possible solution.
	 * @return The new best value.
	 */
	private static int compare(int bestVal, ArrayList<ArrayList<Integer>> sol) {
		if (sol == null || sol.size() != 2 || sol.get(0) == null
				|| sol.get(1) == null || sol.get(0).size() != numNodes
				|| sol.get(1).size() != numNodes)
			return bestVal;

		printDebug("Wert der alten Loesung: " + bestVal);

		int newVal = checkSolution(sol);
		if (newVal == -1)
			return bestVal;

		printDebug("Wert der neuen Loesung: " + newVal);
		if (newVal < bestVal) {
			printDebug("Neue Loesung wird uebernommen");

			return newVal;
		} else {
			printDebug("Neue Loesung wird verworfen");

			return bestVal;
		}
	}

	/**
	 * &Uuml;berpr&uuml;ft die L&ouml;sung <code>sol</code> auf ihre Korrektheit
	 * und gibt ihre Kosten zur&uuml;ck.
	 * 
	 * @param sol
	 *            Zu pr&uuml;fende L&ouml;sung.
	 * @return Kosten der L&ouml;sung oder <code>-1</code>, wenn die L&ouml;sung
	 *         ung&uuml;ltig ist.
	 */
	public static int checkSolution(ArrayList<ArrayList<Integer>> sol) {
		if (sol == null || sol.size() != 2 || sol.get(0) == null
				|| sol.get(1) == null || sol.get(0).size() != numNodes
				|| sol.get(1).size() != numNodes)
			return -1;

		int lastNode;
		boolean[][] vedges = new boolean[numNodes][numNodes];
		int newVal = 0;

		for (int j = 0; j < sol.size(); j++) {
			boolean[] v = new boolean[numNodes];
			ArrayList<Integer> current = sol.get(j);

			int first = current.get(0);
			lastNode = current.get(0);
			v[lastNode] = true;

			for (int i = 1; i < current.size(); i++) {
				int curNode = current.get(i);

				if (v[curNode]) {
					printDebug("Neue Loesung ist ungueltig: Knoten " + curNode
							+ " wird mehrmals besucht");

					return -1;
				}

				if (vedges[lastNode][curNode]) {
					printDebug("Neue Loesung ist ungueltig: Kante (" + curNode
							+ "," + lastNode + ") wird mehrmals verwendet");

					return -1;
				}

				v[curNode] = true;
				vedges[lastNode][curNode] = vedges[curNode][lastNode] = true;

				newVal += am[lastNode][curNode];
				lastNode = curNode;
			}

			// check if the last edge is the same in both paths
			if (vedges[lastNode][first] || vedges[first][lastNode]) {
				printDebug("Neue Loesung ist ungueltig: Kante (" + first + ","
						+ lastNode + ") wird mehrmals verwendet");
				return -1;
			}

			// close the tour
			vedges[lastNode][first] = vedges[first][lastNode] = true;
			newVal += am[lastNode][first];

			for (int i = 0; i < numNodes; i++)
				if (!v[i]) {
					printDebug("Neue Loesung ist ungueltig: Knoten " + i
							+ " wird nicht besucht");

					return -1;
				}
		}

		return newVal;
	}

	/**
	 * &Ouml;ffnet die Eingabedatei und gibt einen {@link Scanner} zur&uuml;ck,
	 * der von ihr liest. Falls kein Dateiname angegeben wurde, wird von
	 * {@link System#in} gelesen.
	 * 
	 * @return Einen {@link Scanner} der von der Eingabedatei liest.
	 */
	private static Scanner openInputFile() {
		if (fileName != null)
			try {
				return new Scanner(new File(fileName));
			} catch (NoSuchElementException e) {
				bailOut("\"" + fileName + "\" is empty");
			} catch (Exception e) {
				bailOut("could not open \"" + fileName + "\" for reading");
			}

		return new Scanner(System.in);

	}

	/**
	 * Interpretiert die Parameter, die dem Programm &uuml;bergeben wurden und
	 * gibt einen {@link Scanner} zur&uuml;ck, der von der Testinstanz liest.
	 * 
	 * @param args
	 *            Die Eingabeparameter
	 * @return Einen {@link Scanner} der von der Eingabedatei liest.
	 */
	private static Scanner processArgs(String[] args) {
		for (String a : args) {
			if (a.equals("-t")) {
				test = true;
			} else if (a.equals("-d")) {
				debug = test = true;
			} else if (fileName == null) {
				fileName = a;

				break;
			}
		}

		return openInputFile();
	}

	/**
	 * The constructor is private to hide it from JavaDoc.
	 * 
	 */
	private Main() {
	}

}
