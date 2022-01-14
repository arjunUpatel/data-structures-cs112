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
 * SchedulePlanInputFile name is passed through the command line as args[1]
 * Read from SchedulePlanInputFile with the format:
 * 1. One line containing a course ID
 * 2. c (int): number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * SchedulePlanOutputFile name is passed through the command line as args[2]
 * Output to SchedulePlanOutputFile with the format:
 * 1. One line containing an int c, the number of semesters required to take the
 * course
 * 2. c lines, each with space separated course ID's
 */
public class SchedulePlan {
    Graph graph;
    Vertex[] adjList;
    ArrayList<String> completedCourses;
    ArrayList<String> incompleteCourses;
    ArrayList<ArrayList<String>> schedulePlan;
    String target;

    public SchedulePlan(String[] args) {
        Graph graph = new Graph(args[0]);
        StdIn.setFile(args[1]);
        StdOut.setFile(args[2]);
        target = StdIn.readString();
        completedCourses = new ArrayList<>();
        incompleteCourses = new ArrayList<>();
        schedulePlan = new ArrayList<>();
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
        while (!checkEligible(target)) {
            schedulePlan.add(new ArrayList<>());
            for (int i = 0; i < incompleteCourses.size(); i++) {
                String course = incompleteCourses.get(i);
                if (checkEligible(course)) {
                    schedulePlan.get(schedulePlan.size() - 1).add(course);
                    completedCourses.add(course);
                    incompleteCourses.remove(course);
                }
            }
        }
        StdOut.println(schedulePlan.size());
        for (int i = 0; i < schedulePlan.size(); i++) {
            for (int j = 0; j < schedulePlan.get(i).size(); j++) {
                if (j == 0)
                    StdOut.print(schedulePlan.get(i).get(j));
                else
                    StdOut.print(" " + schedulePlan.get(i).get(j));
            }
            if (i != schedulePlan.size() - 1)
                StdOut.println();
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

    public boolean checkEligible(String course) {
        if (course == null)
            return false;
        for (Neighbor ptr = adjList[findVertexNum(course)].first; ptr != null; ptr = ptr.next) {
            if (!completedCourses.contains(adjList[ptr.index].name))
                return false;
        }
        return true;
    }

    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println(
                    "Execute: java -cp bin prereqchecker.SchedulePlan adjlist.in scheduleplan.in scheduleplan.out");
            return;
        }

        SchedulePlan app = new SchedulePlan(args);
        // WRITE YOUR CODE HERE
        // 100
        // 111
    }
}
