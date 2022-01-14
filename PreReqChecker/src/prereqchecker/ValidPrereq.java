package prereqchecker;


/**
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
 * ValidPreReqInputFile name is passed through the command line as args[1]
 * Read from ValidPreReqInputFile with the format:
 * 1. 1 line containing the proposed advanced course
 * 2. 1 line containing the proposed prereq to the advanced course
 * 
 * Step 3:
 * ValidPreReqOutputFile name is passed through the command line as args[2]
 * Output to ValidPreReqOutputFile with the format:
 * 1. 1 line, containing either the word "YES" or "NO"
 */
public class ValidPrereq {
	Graph graph;
	String course1, course2;
	Vertex[] adjList;

	public ValidPrereq(String[] args) {
		Graph graph = new Graph(args[0]);
		StdIn.setFile(args[1]);
		StdOut.setFile(args[2]);
		course1 = StdIn.readString();
		course2 = StdIn.readString();
		graph.setPreReq(course1, course2);
		adjList = graph.getAdjList();
		if (validPrereq(course2))
			StdOut.print("YES");
		else
			StdOut.print("NO");
	}

	public boolean validPrereq(String course) {
		if(course.equals(course1))
			return false;
		else{
			for (Neighbor ptr = adjList[findVertexNum(course)].first; ptr != null; ptr = ptr.next) {
				if(!validPrereq(adjList[ptr.index].name))
					return false;
			}
		}
		return true;
	}

	public int findVertexNum(String vertex) {
		for (int i = 0; i < adjList.length; i++) {
			if (vertex.equals(adjList[i].name))
				return i;
		}
		return -1;
	}

	public static void main(String[] args) {

		if (args.length < 3) {
			StdOut.println(
					"Execute: java -cp bin prereqchecker.ValidPrereq adjlist.in validprereq.in validprereq.out");
			return;
		}
		// javac -d bin src/prereqchecker/*.java
		// java -cp bin prereqchecker.ValidPrereq adjlist.in validprereq.in validprereq.out
		ValidPrereq app = new ValidPrereq(args);
	}
}