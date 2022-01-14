package prereqchecker;

public class Graph {
	Vertex[] adjList;

	public Graph(String inputFile) {
		StdIn.setFile(inputFile);
		createGraph();
	}

	public Graph(String inputFile, String outputFile) {
		StdIn.setFile(inputFile);
		StdOut.setFile(outputFile);
		createGraph();
	}

	public Vertex[] getAdjlist() {
		return adjList;
	}

	public void createGraph() {
		int a = StdIn.readInt();
		adjList = new Vertex[a];
		for (int i = 0; i < a; i++)
			adjList[i] = new Vertex(StdIn.readString(), null);
		int b = StdIn.readInt();
		for (int i = 0; i < b; i++)
			setPreReq(StdIn.readString(), StdIn.readString());
	}

	public int findVertexNum(String vertex) {
		for (int i = 0; i < adjList.length; i++) {
			if (vertex.equals(adjList[i].name))
				return i;
		}
		return -1;
	}

	public void print() {
		for (int i = 0; i < adjList.length; i++) {
			StdOut.print(adjList[i].name);
			for (Neighbor ptr = adjList[i].first; ptr != null; ptr = ptr.next) {
				StdOut.print(" " + adjList[ptr.index].name);
			}
			if (i < adjList.length - 1)
				StdOut.println();
		}
	}

	public void setPreReq(String course, String prereq) {
		int i = findVertexNum(course);
		int j = findVertexNum(prereq);
		adjList[i].first = new Neighbor(j, adjList[i].first);
	}

	public Vertex[] getAdjList() {
		return adjList;
	}
}