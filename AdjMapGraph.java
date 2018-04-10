package cs2321;

import net.datastructures.*;

/**
 * Graph implemented with an Adjacent List
 * 
 * CS 2321
 * Assignment #7: Graph
 * Last Date Modified: April 8th, 2018
 * @author Javen Zamojcin
 *
 * @param <V>
 * @param <E>
 */
public class AdjListGraph<V, E> implements Graph<V, E> {

	//Instance Variables
	
	private boolean isDirected;
	private PositionalList<Vertex<V>> vertices = new DoublyLinkedList<>();
	private PositionalList<Edge<E>> edges = new DoublyLinkedList<>();
	
	//Constructors
	
	public AdjListGraph(boolean directed) { isDirected = directed; }

	public AdjListGraph() { isDirected = false; }

	//Instance Methods
	
	/**
	 * Returns an iteration of all the edges of the graph
	 * @return Iteration of edges
	 */
	@TimeComplexity("O(m)")
	public Iterable<Edge<E>> edges() {
	/* TCJ
	 * Iterates through entire collection of edges: O(m)	
	 */
		return edges;
	}

	/**
	 * Returns an array containing the two endpoint vertices of an edge.
	 * If the graph is directed, the first vertex is the origin and the second is the destination.
	 * @param edge to be checked
	 * @return array of vertices connected to edge
	 */
	@TimeComplexity("O(1)")
	public Vertex[] endVertices(Edge<E> e) throws IllegalArgumentException {
	/* TCJ
	 * References to associated vertices are stored in edge, allowing for continous operation time: O(1)	
	 */
		 InnerEdge<E> edge = (InnerEdge<E>) validate(e);
		return edge.getEndPoints();
	}

	/**
	 * Creates and returns a new edge connected from vertex 'u' to vertex 'v'.
	 * @param origin vertex
	 * @param end vertex
	 * @param element to be stored in edge
	 * @return newly created edge
	 * @throws IllegalArgumentException if edge already exists
	 */
	@TimeComplexityExpected("O(1)")
	public Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E o)
			throws IllegalArgumentException {
	/* TCJ
	 * Adds edge with positional-aware design to end of edge collection: O(1)
	 */
		if ( getEdge(u, v) == null ) {
			InnerEdge<E> e = new InnerEdge<>(u, v, o);
			e.setPosition(edges.addLast(e));
			InnerVertex<V> origin = (InnerVertex<V>) validate(u);
			InnerVertex<V> end = (InnerVertex<V>) validate(v);
			origin.getOutgoing().put(v, e);
			end.getIncoming().put(u, e);
			return e;
		} else {
			throw new IllegalArgumentException("Edge from u to v exists");
		}
	}

	/**
	 * Creates and returns a new Vertex
	 * @param vertex to be stored
	 * @return newly created vertex
	 */
	@TimeComplexity("O(1)")
	public Vertex<V> insertVertex(V o) {
	/* TCJ
	 * Simply adds vertex to end of vertice collection: O(1)	
	 */
		InnerVertex<V> v = new InnerVertex<>(o, isDirected);
		v.setPosition(vertices.addLast(v));
		return v;
	}

	/**
	 * Returns the number of edges of the graph
	 * @return number of edges
	 */
	@TimeComplexity("O(1)")
	public int numEdges() {
	/* TCJ
	 * Continous operation: O(1)	
	 */
		return edges.size();
	}

	/**
	 * Returns the number of vertices of the graph
	 * @return number of vertices
	 */
	@TimeComplexity("O(1)")
	public int numVertices() {
	/* TCJ
	 * Continous operation: O(1)
	 */
		return vertices.size();
	}

	/**
	 * For edge 'e' incident to vertex 'v', returns the other vertex of the edge.
	 * @param vertex to be checked
	 * @param edge connected to given vertex
	 * @return vertex opposite of given vertex at edge 'e'
	 * @throws IllegalArgumentException if edge is not incident to vertex
	 */
	@TimeComplexity("O(1)")
	public Vertex<V> opposite(Vertex<V> v, Edge<E> e)
			throws IllegalArgumentException {
	/* TCJ
	 * References to associated vertices are stored in edge, allowing for continous operation time: O(1)	
	 */
		InnerEdge<E> edge = (InnerEdge<E>) validate(e);
		Vertex<V>[] endpoints = edge.getEndPoints();
		if ( endpoints[0] == v ) {
			return endpoints[1];
		} else if ( endpoints[1] == v ) {
			return endpoints[0];
		} else {
			throw new IllegalArgumentException("V is not incident to this edge");
		}
	}

	/**
	 * Removes edge from the graph
	 * @param edge to be removed
	 * @throws IllegalArgumentException if edge does not exist
	 */
	@TimeComplexityExpected("O(1)")
	public void removeEdge(Edge<E> e) throws IllegalArgumentException {
	/* TCJ
	 * Position-aware design allows for continous operation: O(1)
	 */
		InnerEdge<E> edge = (InnerEdge<E>) validate(e);
		edges.remove(edge.getPosition());
		((InnerVertex<V>) edge.endpoints[0]).incoming.remove(edge.endpoints[1]);
		((InnerVertex<V>) edge.endpoints[0]).outgoing.remove(edge.endpoints[1]);
		((InnerVertex<V>) edge.endpoints[1]).incoming.remove(edge.endpoints[0]);
		((InnerVertex<V>) edge.endpoints[1]).outgoing.remove(edge.endpoints[0]);
	}

	/**
	 * Removes vertex and all of its incident edges from graph
	 * @param Vertex to be removed
	 * @throws IllegalArgumentException if vertex does not exist
	 */
	@TimeComplexity("O(deg(v))")
	public void removeVertex(Vertex<V> v) throws IllegalArgumentException {
	/* TCJ
	 * Time complexity depends on number of connecting edges vertex 'v' has: O(degree(v))	
	 */
		InnerVertex<V> vertex = (InnerVertex<V>) validate(v);
		for (Edge<E> e : vertex.getOutgoing().values()) {
			removeEdge(e);
		}
		for (Edge<E> e : vertex.getIncoming().values()) {
			removeEdge(e);
		}
		
		vertices.remove(vertex.getPosition());
	}

	/**
	 * Replaces the element stored at a given edge
	 * @param edge
	 * @param new element
	 * @return element replaced
	 * @throws IllegalArgumentException if edge does not exist
	 */
	@TimeComplexity("O(1)")
	public E replace(Edge<E> e, E o) throws IllegalArgumentException {
	/* TCJ
	 * Continous operation: O(1)
	 */
		InnerEdge<E> edge = (InnerEdge<E>) validate(e);
		return edge.setElement(o);
	}

   /**
    * Replaces a vertex stored in graph
    * @param vertex to be replaced
    * @param new vertex
    * @return vertex replaced
    * @throws IllegalArgumentException if vertex does not exist
    */
	@TimeComplexity("O(1)")
	public V replace(Vertex<V> v, V o) throws IllegalArgumentException {
	/* TCJ
	 * Simply replaces stored element in edge, allowing for continous operation time: O(1)	
	 */
		InnerVertex<V> vertex = (InnerVertex<V>) validate(v);
		return vertex.setElement(o);
	}

	/**
	 * Returns an iteration of all the vertices of the graph
	 * @return iteration of vertices
	 */
	@TimeComplexity("O(n)")
	public Iterable<Vertex<V>> vertices() {
	/* TCJ
	 * Iterates through entire vertices collection: O(n)
	 */
		return vertices;
	}

	/**
	 * Returns the number of outgoing edges from given vertex
	 * @param vertex to be checked
	 * @return number of outgoing edges
	 * @throws IllegalArgumentException if vertex does not exist
	 */
	@Override
	@TimeComplexity("O(1)")
	public int outDegree(Vertex<V> v) throws IllegalArgumentException {
	/* TCJ
	 * Continous operation: O(1)	
	 */
		InnerVertex<V> vertex = (InnerVertex<V>) validate(v);
		return vertex.getOutgoing().size();
	}

	/**
	 * Returns the number of incoming edges from given vertex
	 * @param vertex to be checked
	 * @return number of incoming edges
	 * @throws IllegalArgumentException if vertex does not exist
	 */
	@Override
	@TimeComplexity("O(1)")
	public int inDegree(Vertex<V> v) throws IllegalArgumentException {
	/* TCJ
	 * Continous operation: O(1)	
	 */
		InnerVertex<V> vertex = (InnerVertex<V>) validate(v);
		return vertex.getIncoming().size();
	}

	/**
	 * Returns an iteration of all outgoing edges from given vertex
	 * @param vertex to be checked
	 * @return iteration of outgoing edges
	 * @throws IllegalArgumentException if vertex does not exist
	 */
	@Override
	@TimeComplexity("O(deg(v))")
	public Iterable<Edge<E>> outgoingEdges(Vertex<V> v)
			throws IllegalArgumentException {
	/* TCJ
	 * Depends on number of connected edges vertex has: O(degree(v))	
	 */	
		InnerVertex<V> vertex = (InnerVertex<V>) validate(v);
		return vertex.getOutgoing().values();
	}

	/**
	 * Returns an iteration of all incoming edges from given vertex
	 * @param vertex to be checked
	 * @return iteration of incoming edges
	 * @throws IllegalArgumentException if vertex does not exist
	 */
	@Override
	@TimeComplexity("O(deg(v))")
	public Iterable<Edge<E>> incomingEdges(Vertex<V> v)
			throws IllegalArgumentException {
	/* TCJ
	 * Depends on number of connected edges vertex has: O(degree(v))	
	 */
		InnerVertex<V> vertex = (InnerVertex<V>) validate(v);
		return vertex.getIncoming().values();
	}

	/**
	 * Returns the edge from vertex 'u' to vertex 'v'
	 * If graph is undirected, there is no difference between getEdge(u, v) and getEdge(v, u)
	 * @param origin vertex
	 * @param end vertex
	 * @return connected edge
	 * @return null if no connecting edge exists
	 * @throws IllegalArgumentException if vertex does not exist
	 */
	@Override
	@TimeComplexity("O(min(deg(u), deg(v)))")
	@TimeComplexityExpected("O(1)")
	public Edge<E> getEdge(Vertex<V> u, Vertex<V> v)
			throws IllegalArgumentException {
	/* TCJ
	 * Position aware design allows for continous operation: O(1)
	 * Worst Case - Requires an iteration through either vertice or edge collections: O(min(deg(u), deg(v)))
	 */
		InnerVertex<V> origin = (InnerVertex<V>) validate(u);
		return origin.getOutgoing().get(v);
	}
	
	//Helper Methods
	
	private Vertex<V> validate(Vertex<V> vertex) {
		
		if (!(vertex instanceof Vertex)) {
			throw new IllegalArgumentException("Invalid vertex");
		}
		
		Vertex<V> vert = (Vertex<V>) vertex;
		return vert;
	}
	
	private Edge<E> validate(Edge<E> edge) {
		
		if (!(edge instanceof Edge)) {
			throw new IllegalArgumentException("Invalid edge");
		}
		
		Edge<E> e = (Edge<E>) edge;
		return e;
	}
	
	//Helper Classes
	
	private class InnerVertex<V> implements Vertex<V> {

		private V element;
		private Position<Vertex<V>> pos;
		public Map<Vertex<V>, Edge<E>> outgoing, incoming;
		
		public InnerVertex(V elem, boolean graphIsDirected) {
			element = elem;
			outgoing = new ProbeHashMap<>();
			if (graphIsDirected) {
				incoming = new ProbeHashMap<>();
			} else {
				incoming = outgoing;
			}
		}
		
		@Override
		public V getElement() { return element; }
		
		public V setElement(V v) {
			V temp = element;
			element = v;
			return temp;
		}
		
		public void setPosition(Position<Vertex<V>> p) { pos = p; }
		
		public Position<Vertex<V>> getPosition() { return pos; }
		
		public Map<Vertex<V>, Edge<E>> getOutgoing() { return outgoing; }
		
		public Map<Vertex<V>, Edge<E>> getIncoming() { return incoming; }
	}	
	
	private class InnerEdge<E> implements Edge<E> {

		private E element;
		private Position<Edge<E>> pos;
		private Vertex<V>[] endpoints;
		
		public InnerEdge( Vertex<V> u, Vertex<V> v, E elem) {
			element = elem;
			endpoints = (Vertex<V>[]) new Vertex[] {u, v};
		}
		
		@Override
		public E getElement() { return element; }
		
		public E setElement(E e) {
			E temp = element;
			element = e;
			return temp;
		}
		
		public Vertex<V>[] getEndPoints() { return endpoints; }
		
		public void setPosition(Position<Edge<E>> p) { pos = p; }
		
		public Position<Edge<E>> getPosition() { return pos; }
	}
}
