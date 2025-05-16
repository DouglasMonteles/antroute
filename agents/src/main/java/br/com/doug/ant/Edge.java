package br.com.doug.ant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Edge {

    private Node nodeA;
    private Node nodeB;
    private Float distance;
    private Float visibility;

    /*
    * Intensity of trail τ_ij(t)  on edge (i,j) at time t
    * */
    private Float intensityOfTrail = 0f;

    /*
    * ∆τ_k_ij(t,t+1) is the quantity per unit of length of trail substance (pheromone in real ants) laid on
    * edge (i,j) by the k-th ant between time t and t+1.
    * */
    private Float pheromoneOnEdge = 0f;

    private boolean isBidirectional;

    public void incrementPheromone(Float pheromoneOnEdge) {
        this.pheromoneOnEdge += pheromoneOnEdge;
    }

}
