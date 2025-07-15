package br.com.doug.ants;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class NodeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7390057070375880531L;

    private String name;

    private Position position;

    @Data
    public static class Position implements Serializable {

        @Serial
        private static final long serialVersionUID = 7955564661493172195L;

        private Float x;
        private Float y;
    }

}
