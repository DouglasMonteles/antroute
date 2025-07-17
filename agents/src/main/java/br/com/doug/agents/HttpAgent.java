package br.com.doug.agents;

import br.com.doug.agents.utils.AgentUtils;
import br.com.doug.agents.utils.Performative;
import br.com.doug.ant.Ant;
import br.com.doug.http.HttpClient;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;

public class HttpAgent extends Agent {

    private static final Logger LOG = LoggerFactory.getLogger(HttpAgent.class);

    @Serial
    private static final long serialVersionUID = 201862800409891149L;

    @Override
    protected void setup() {

        addBehaviour(new HandleAntUpdateRequestBehaviour());
    }

    private class HandleAntUpdateRequestBehaviour extends CyclicBehaviour {

        @Serial
        private static final long serialVersionUID = 1258493614733760208L;

        @Override
        public void action() {
            // Observer response from the ants
            MessageTemplate antSuccessReply = MessageTemplate.MatchPerformative(Performative.HTTP_CLIENT_ANT_REQUEST);
            ACLMessage antReply = receive(antSuccessReply);

            if (antReply != null) {
                Ant ant = AgentUtils.getContentObject(antReply, Ant.class);
                LOG.info("Send ant info to server api. Ant: {}", ant);

                int responseCode;

                do {
                    responseCode = HttpClient.post("http://localhost:8080/ants", ant);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (responseCode != 200) {
                        LOG.info("Trying again...");
                    }
                } while (responseCode != 200);
            } else {
                block();
            }
        }
    }

}
