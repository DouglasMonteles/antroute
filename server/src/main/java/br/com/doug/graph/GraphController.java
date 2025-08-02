package br.com.doug.graph;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/graph")
public class GraphController {

    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping
    public ResponseEntity<List<GraphNodeDTO>> graph() {
        List<GraphNodeDTO> graphNodes = graphService.getGraph();
        return ResponseEntity.ok().body(graphNodes);
    }

}
