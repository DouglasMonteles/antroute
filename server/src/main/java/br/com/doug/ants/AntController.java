package br.com.doug.ants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ants")
public class AntController {

    private static final Logger LOG = LoggerFactory.getLogger(AntController.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

   private final AntObserverService antService = AntObserverService.INSTANCE;

    @PostMapping
    public ResponseEntity<Void> receiveAntInfoFromAgent(@RequestBody AntDTO antDTO) {
        LOG.info("Received: {}", antDTO);
        simpMessagingTemplate.convertAndSend("/topic/ants/updates", antDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/antQuantity")
    public ResponseEntity<Void> defineAntQuantityForSimulation(@RequestBody AntSimulationDataDTO antSimulationDataDTO) {
        antService.initSimulation(antSimulationDataDTO);
        return ResponseEntity.ok().build();
    }

}
