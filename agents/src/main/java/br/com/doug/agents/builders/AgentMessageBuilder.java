package br.com.doug.agents.builders;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class AgentMessageBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(AgentMessageBuilder.class);

    private final ACLMessage message;

    public AgentMessageBuilder(ACLMessage message) {
        this.message = message;
    }

    public AgentMessageBuilder(int performative) {
        this.message = new ACLMessage(performative);
    }

    public AgentMessageBuilder setPerformative(int performative) {
        this.message.setPerformative(performative);
        return this;
    }

    public AgentMessageBuilder setContentObject(Serializable contentObject) {
        try {
            this.message.setContentObject(contentObject);
        } catch (IOException e) {
            LOG.error("I/O error when set content object. Error: {}", e.getMessage());
        }
        return this;
    }

    public AgentMessageBuilder setReceiver(AID aid) {
        this.message.addReceiver(aid);
        return this;
    }

    public AgentMessageBuilder setReceivers(List<AID> names) {
        names.forEach(this.message::addReceiver);
        return this;
    }

    public ACLMessage build() {
        return message;
    }

}
