package ex1.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

import ex1.src.WGraph_DS;
import ex1.src.weighted_graph;

public class WGraph_DSTest {

	@Test
	// checks if a vertex can be connected to himself
	void oneVertexEdge() {
		weighted_graph graph = new WGraph_DS();
		graph.addNode(1);
		graph.connect(1, 1, 1);
		if (graph.hasEdge(1, 1) || graph.edgeSize() != 0)
			fail("You cant connect a vertex to himself");
	}

	@Test
	void negativeVertix() {
		weighted_graph graph = new WGraph_DS();
		graph.addNode(-1);
		if (graph.getNode(-1) != null)
			fail("cannot add a negative vertex");
	}

	@Test
	void removeNode() {
		weighted_graph graph = graphCreator(30, 50);
		int edgesOne = graph.getV(1).size();
		int numOfEdges = graph.edgeSize();
		graph.removeNode(1);
		assertEquals(numOfEdges - edgesOne, graph.edgeSize());
		assertEquals(29, graph.nodeSize());
		graph.removeNode(1);
		graph.removeNode(2);
		graph.removeNode(3);
		graph.removeNode(4);
		graph.removeNode(5);
		assertEquals(25, graph.nodeSize());
	}

	@Test
	void removeNodeEdges() {
		weighted_graph graph = new WGraph_DS();
		graph.addNode(1);
		graph.addNode(2);
		graph.addNode(3);
		graph.addNode(4);
		graph.addNode(5);
		graph.connect(1, 2, Math.random()*99 + 1);
		graph.connect(1, 3, Math.random()*99 + 1);
		graph.connect(1, 4, Math.random()*99 + 1);
		graph.connect(1, 5, Math.random()*99 + 1);
		graph.connect(2, 5, Math.random()*99 + 1);
		graph.connect(3, 5, Math.random()*99 + 1);
		graph.connect(4, 6, Math.random()*99 + 1);
		assertFalse(graph.hasEdge(4, 6));
		
		int i = 2;
		while(i <= 5) {
			graph.removeEdge(1, i++);
		}
		
		assertEquals(2, graph.edgeSize());
		assertFalse(graph.hasEdge(1, 2));
	}

	@Test
	void createMillionNodes() {
		weighted_graph graph = new WGraph_DS();
		
		for (int i = 0; i < 1000000; i++) {
			graph.addNode(i);
		}
		assertEquals(1000000, graph.nodeSize());
		
		int firstNum = 0;
		int secondNum = 1;
		int index = 0;
		while (index < 10000000) {
			double w = Math.random() * 99 + 1;
			graph.connect(firstNum, secondNum++, w);
			index++;
			if (secondNum == 999999) {
				firstNum++;
				secondNum = firstNum + 1;
			}
		}
		assertEquals(10000000, graph.edgeSize());
	}
	

	public weighted_graph graphCreator(int vertices, int edges) {
		weighted_graph graph = new WGraph_DS();
		for (int i = 0; i < vertices; i++) {
			graph.addNode(i);
		}
		
		int firstNum = 0;
		int secondNum = 1;
		int index = 0;
		while (index < edges) {
			double w = Math.random() * 99 + 1;
			graph.connect(firstNum, secondNum++, w);
			index++;
			if (secondNum == vertices - 1) {
				firstNum++;
				secondNum = firstNum + 1;
			}
		}
		return graph;
	}
	
}
