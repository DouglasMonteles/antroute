package br.com.doug.ants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ants")
public class AntController {

    private static final Logger LOG = LoggerFactory.getLogger(AntController.class);

    public ResponseEntity<Void> receiveAntInfoFromAgent(AntDTO antDTO) {
        LOG.info("Received: {}", antDTO);
        return ResponseEntity.ok().build();
    }

}
