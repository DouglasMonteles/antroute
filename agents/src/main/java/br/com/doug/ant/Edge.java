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
    private Float trailIntensity = 0f;
    private boolean isBidirectional = true;

}
