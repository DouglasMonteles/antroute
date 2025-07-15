package br.com.doug.ants;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class AntDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7482971292168031080L;

    private String label;
    private NodeDTO initialNode;
    private NodeDTO actualNode;
    private NodeDTO nextNode;

    private final Set<NodeDTO> tabuList = new HashSet<>();
    private final List<NodeDTO> pathFound = new ArrayList<>();

}
