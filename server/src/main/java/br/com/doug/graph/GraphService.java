package br.com.doug.graph;

import br.com.doug.utils.ObjectConversorUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphService {

    public List<GraphNodeDTO> getGraph() {
        return ObjectConversorUtils.convertJsonInObject("graph.json");
    }



}
