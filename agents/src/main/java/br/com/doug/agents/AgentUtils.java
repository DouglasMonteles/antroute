package br.com.doug.agents;

import br.com.doug.exceptions.AgentException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AgentUtils.class);

    public static <T> T getContentObject(ACLMessage message, Class<T> clazz) {
        Object contentObj = null;

        try {
            contentObj =  message.getContentObject();

            if (contentObj == null) {
                LOG.error("Message content is null.");
                throw new AgentException("Message content is null.");
            }
        } catch (UnreadableException e) {
            LOG.error("Error during the extract content from message. Error: {}", e.getMessage());
        }

        return clazz.cast(contentObj);
    }

}
