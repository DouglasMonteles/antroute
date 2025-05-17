package br.com.doug.ant;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AntDensityAlgorithmTest {

    @Test
    public void isSameTour() {
        AntDensityAlgorithm antDensityAlgorithm = new AntDensityAlgorithm();

        Node nodeA = new Node("A", new Node.Position(10f, 30f));
        Node nodeB = new Node("B", new Node.Position(20f, 20f));
        Node nodeC = new Node("C", new Node.Position(30f, 10f));

        Ant ant1 = new Ant("Ant1", nodeA);
        Ant ant2 = new Ant("Ant2", nodeB);

        List<Node> tour1 = List.of(nodeB, nodeC);
        List<Node> tour2 = List.of(nodeC, nodeA);

        ant1.getPathFound().addAll(tour1);
        ant2.getPathFound().addAll(tour2);

        boolean isSameTour = antDensityAlgorithm.isSameTour(ant1.getPathFound(), ant2.getPathFound());

        Assert.assertTrue(isSameTour);
        Assert.assertEquals(ant1.getPathFound(), List.of(nodeA, nodeB, nodeC));
        Assert.assertEquals(ant2.getPathFound(), List.of(nodeB, nodeC, nodeA));
    }

    @Test
    public void isNotSameTourWhenOnePathIsReverseOfTheOther() {
        AntDensityAlgorithm antDensityAlgorithm = new AntDensityAlgorithm();

        Node nodeA = new Node("A", new Node.Position(10f, 30f));
        Node nodeB = new Node("B", new Node.Position(20f, 20f));
        Node nodeC = new Node("C", new Node.Position(30f, 10f));

        Ant ant1 = new Ant("Ant1", nodeA);
        Ant ant2 = new Ant("Ant2", nodeC);

        List<Node> tour1 = List.of(nodeB, nodeC);
        List<Node> tour2 = List.of(nodeB, nodeA);

        ant1.getPathFound().addAll(tour1);
        ant2.getPathFound().addAll(tour2);

        boolean isSameTour = antDensityAlgorithm.isSameTour(ant1.getPathFound(), ant2.getPathFound());

        Assert.assertFalse(isSameTour);
        Assert.assertEquals(ant1.getPathFound(), List.of(nodeA, nodeB, nodeC));
        Assert.assertEquals(ant2.getPathFound(), List.of(nodeC, nodeB, nodeA));
    }

    @Test
    public void isNotSameTourWhenOnePathNotContainsASubListOfTheOtherPath() {
        AntDensityAlgorithm antDensityAlgorithm = new AntDensityAlgorithm();

        Node nodeA = new Node("A", new Node.Position(10f, 30f));
        Node nodeB = new Node("B", new Node.Position(20f, 20f));
        Node nodeC = new Node("C", new Node.Position(30f, 10f));
        Node nodeD = new Node("D", new Node.Position(30f, 10f));
        Node nodeE = new Node("E", new Node.Position(30f, 10f));

        Ant ant1 = new Ant("Ant1", nodeA);
        Ant ant2 = new Ant("Ant2", nodeB);

        List<Node> tour1 = List.of(nodeB, nodeC, nodeD, nodeE);
        List<Node> tour2 = List.of(nodeA, nodeC, nodeD, nodeE);

        ant1.getPathFound().addAll(tour1);
        ant2.getPathFound().addAll(tour2);

        boolean isSameTour = antDensityAlgorithm.isSameTour(ant1.getPathFound(), ant2.getPathFound());

        Assert.assertFalse(isSameTour);
        Assert.assertEquals(ant1.getPathFound(), List.of(nodeA, nodeB, nodeC, nodeD, nodeE));
        Assert.assertEquals(ant2.getPathFound(), List.of(nodeB, nodeA, nodeC, nodeD, nodeE));
    }

}
