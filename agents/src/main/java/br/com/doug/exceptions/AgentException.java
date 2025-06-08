package br.com.doug.exceptions;

public class AgentException extends RuntimeException {

    public AgentException(String message) {
        super(message);
    }

    public AgentException(Throwable e) {
        super(e);
    }

}
