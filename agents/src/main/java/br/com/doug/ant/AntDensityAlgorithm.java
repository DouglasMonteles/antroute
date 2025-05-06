package br.com.doug.ant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AntDensityAlgorithm {

    private Integer timeCounter = 0;

    private Graph graph;

    private AntDensityAlgorithm(Graph graph) {
        this.graph = graph;
    }



}
