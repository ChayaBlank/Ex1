package ex1.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import ex1.src.WGraph_Algo;
import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import ex1.src.weighted_graph_algorithms;

class WGraph_AlgoTest {
	
	
	@Test
	void graphConnected() {
		weighted_graph graph = graphCreator(0, 0);
		weighted_graph_algorithms ga = new WGraph_Algo();
		ga.init(graph);
		assertTrue("graph needs to be connected", ga.isConnected());
		
		graph.addNode(1);
		assertTrue("graph needs to be connected", ga.isConnected());
		
		graph.addNode(2);
		assertFalse("graph needs to not be connected", ga.isConnected());
		
		graph.addNode(3);
		graph.addNode(4);
		graph.addNode(5);
		graph.addNode(6);
		graph.connect(1, 2, 1);
		graph.connect(1, 3, 2);
		graph.connect(1, 4, 3);
		graph.connect(1, 5, 4);
		graph.connect(2, 5, 3);
		graph.connect(3, 5, 2);
		assertFalse(ga.isConnected());
		
		graph.connect(4, 6, 2);
		assertTrue(ga.isConnected());
		
	}


	@Test
	void copyGraph() {
		weighted_graph graph = graphCreator(1000, 70000);
		weighted_graph_algorithms ga = new WGraph_Algo();
		ga.init(graph);
		weighted_graph g2 = ga.copy();
		assertEquals(graph, g2);
	}
	
	@Test
	void shortestPath() {
		weighted_graph graph = graphCreator(0, 0);
		weighted_graph_algorithms ga = new WGraph_Algo();
		ga.init(graph);
		graph.addNode(1);
		graph.addNode(2);
		graph.addNode(3);
		graph.addNode(4);
		graph.addNode(5);
		graph.addNode(6);
		graph.connect(1, 2, 1);
		graph.connect(1, 3, 2);
		graph.connect(1, 4, 3);
		graph.connect(1, 5, 4);
		graph.connect(2, 5, 3);
		graph.connect(3, 5, 2);
		assertEquals(-1, ga.shortestPathDist(1, 6));
		
		graph.connect(4, 6, 2);
		assertEquals(7, ga.shortestPathDist(3, 6));
		
		List<node_info> list = new LinkedList<>();
		list.add(graph.getNode(3));
		list.add(graph.getNode(1));
		list.add(graph.getNode(4));
		list.add(graph.getNode(6));
		assertArrayEquals(list.toArray(), ga.shortestPath(3, 6).toArray());
	}

	public weighted_graph graphCreator(int vertices, int edges) {
		weighted_graph graph = new WGraph_DS();
		for (int i = 0; i < vertices; i++) {
			graph.addNode(i);
		}

		int index = 0;
		while (index < edges) {
			int firstNum = (int) (Math.random() * vertices);
			int secondNum = (int) (Math.random() * vertices);
			double w = Math.random() * 99 + 1;
			if (firstNum != secondNum && !graph.hasEdge(firstNum, secondNum)) {
				graph.connect(firstNum, secondNum, w);
				index++;
			}
		}
		return graph;
	}
	

}
