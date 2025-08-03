package br.com.doug.graph;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/graph")
public class GraphController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @GetMapping
    public ResponseEntity<List<GraphNodeDTO>> graph() {
        List<GraphNodeDTO> graphNodes = graphService.getGraph();
        return ResponseEntity.ok().body(graphNodes);
    }

    @PostMapping("/result")
    public ResponseEntity<Void> result(@RequestBody GraphResultDTO graphResultDTO) {
        simpMessagingTemplate.convertAndSend("/topic/graph/result", graphResultDTO);
        return ResponseEntity.ok().build();
    }

}
