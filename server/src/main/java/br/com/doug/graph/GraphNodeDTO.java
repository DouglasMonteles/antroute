package br.com.doug.graph;

import br.com.doug.ants.NodeDTO;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class GraphNodeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4081220731178492354L;

    private NodeDTO node;
    private final List<String> edges = new ArrayList<>();

}
