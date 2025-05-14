package br.com.doug.ant;

public interface AntAlgorithm {

    /*
     * Number of cities
     * */
    Integer N = 3;

    /*
     * Number of ants
     * */
    Integer M = 3;

    /*
     * Number of edges between towns
     * */
    Integer E = 2;

    /*
     * The coefficient RHO must be set to a value < 1 to avoid unlimited accumulation of trail
     */
    Float RHO = 0.7f;

    Float ALPHA = 1f;

    Float BETA = 1f;

    /*
    * NC is the number of cycles
    * */
    Integer NC_MAX = 3;

}
