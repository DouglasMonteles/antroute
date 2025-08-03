package br.com.doug.graph;

import br.com.doug.ants.NodeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphResultDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4278077548199329393L;

    private List<NodeDTO> shortestPath = new ArrayList<>();

    private Float shortestDistance;

}
