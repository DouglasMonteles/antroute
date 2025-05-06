package br.com.doug.ant;

import lombok.Data;

@Data
public class AntAlgorithm {

    /*
    The coefficient RHO must be set to a value < 1 to avoid unlimited accumulation of trail
    */
    private final Float RHO = 0.5f;

    private final Float ALPHA = 0f;

    private final Float BETA = 0f;

    private final Graph graph = new Graph();

}
