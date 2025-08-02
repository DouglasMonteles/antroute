package br.com.doug.ants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7390057070375880531L;

    private String name;

    private Position position;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Position implements Serializable {

        @Serial
        private static final long serialVersionUID = 7955564661493172195L;

        private Float x;
        private Float y;
    }

}
