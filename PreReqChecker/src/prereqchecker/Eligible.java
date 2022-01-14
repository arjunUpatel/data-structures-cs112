package prereqchecker;

import java.util.*;

/**
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * EligibleInputFile name is passed through the command line as args[1]
 * Read from EligibleInputFile with the format:
 * 1. c (int): Number of courses
 * 2. c lines, each with 1 course ID
 * 
 * Step 3:
 * EligibleOutputFile name is passed through the command line as args[2]
 * Output to EligibleOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class Eligible {
	Vertex[] adjList;
	Graph graph;
	ArrayList<String> completedCourses;
	boolean eligible;

	public Eligible(String[] args) {
		graph = new Graph(args[0]);
		StdIn.setFile(args[1]);
		StdOut.setFile(args[2]);
		adjList = graph.getAdjList();
		completedCourses = new ArrayList<>();

		int c = StdIn.readInt();
		for (int i = 0; i < c; i++) {
			createdCompletedCourseList(StdIn.readString());
		}

		boolean indexer = false;
		for (int i = 0; i < adjList.length; i++) {
			eligible = true;
			if (!completedCourses.contains(adjList[i].name)) {
				for (Neighbor ptr = adjList[i].first; ptr != null; ptr = ptr.next) {
					if (!completedCourses.contains(adjList[ptr.index].name)) {
						eligible = false;
					}
				}
				if (eligible) {
					if (indexer)
						StdOut.println();
					StdOut.print(adjList[i].name);
					indexer = true;
				}
			}
		}
	}

	public int findVertexNum(String vertex) {
		for (int i = 0; i < adjList.length; i++) {
			if (vertex.equals(adjList[i].name))
				return i;
		}
		return -1;
	}

	public void createdCompletedCourseList(String course) {
		if (!completedCourses.contains(course))
			completedCourses.add(course);
		for (Neighbor ptr = adjList[findVertexNum(course)].first; ptr != null; ptr = ptr.next)
			createdCompletedCourseList(adjList[ptr.index].name);
	}

	public static void main(String[] args) {

		if (args.length < 3) {
			StdOut.println(
					"Execute: java -cp bin prereqchecker.Eligible adjlist.in eligible.in eligible.out");
			return;
		}

		Eligible app = new Eligible(args);
	}
}