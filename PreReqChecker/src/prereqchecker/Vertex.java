package prereqchecker;

public class Vertex {
	String name;
	Neighbor first;

	public Vertex(String name, Neighbor first) {
		this.name = name;
		this.first = first;
	}
}