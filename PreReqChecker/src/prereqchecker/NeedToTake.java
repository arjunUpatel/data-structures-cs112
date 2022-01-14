package prereqchecker;

import java.util.*;

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
 * NeedToTakeInputFile name is passed through the command line as args[1]
 * Read from NeedToTakeInputFile with the format:
 * 1. One line, containing a course ID
 * 2. c (int): Number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * NeedToTakeOutputFile name is passed through the command line as args[2]
 * Output to NeedToTakeOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class NeedToTake {
    Graph graph;
    Vertex[] adjList;
    ArrayList<String> completedCourses;
    ArrayList<String> incompleteCourses;
    String target;

    public NeedToTake(String[] args) {
        Graph graph = new Graph(args[0]);
        StdIn.setFile(args[1]);
        StdOut.setFile(args[2]);
        target = StdIn.readString();
        completedCourses = new ArrayList<>();
        incompleteCourses = new ArrayList<>();
        adjList = graph.getAdjList();

        int c = StdIn.readInt();
        for (int i = 0; i < c; i++) {
            createdCompletedCourseList(StdIn.readString());
        }
        for (Neighbor ptr = adjList[findVertexNum(target)].first; ptr != null; ptr = ptr.next) {
            String prereq = adjList[ptr.index].name;
            if (!completedCourses.contains(prereq)) {
                incompleteCourses.add(prereq);
                getIncompletePrereqs(prereq);
            }
        }
        boolean indexer = false;
        for (String str : incompleteCourses) {
            if (indexer)
                StdOut.println();
            StdOut.print(str);
            indexer = true;
        }
    }

    public void createdCompletedCourseList(String course) {
        if (!completedCourses.contains(course))
            completedCourses.add(course);
        for (Neighbor ptr = adjList[findVertexNum(course)].first; ptr != null; ptr = ptr.next)
            createdCompletedCourseList(adjList[ptr.index].name);
    }

    public void getIncompletePrereqs(String course) {
        if (course == null)
            return;
        else {
            for (Neighbor ptr = adjList[findVertexNum(course)].first; ptr != null; ptr = ptr.next) {
                String prereq = adjList[ptr.index].name;
                if (!completedCourses.contains(prereq)) {
                    incompleteCourses.add(prereq);
                    getIncompletePrereqs(prereq);
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

    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println(
                    "Execute: java -cp bin prereqchecker.NeedToTake adjlist.in needtotake.in needtotake.out");
            return;
        }
        NeedToTake app = new NeedToTake(args);
    }
}