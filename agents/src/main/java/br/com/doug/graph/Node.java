package br.com.doug.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"name", "position"})
public class Node implements Serializable {

    @Serial
    private static final long serialVersionUID = 6364157902614832565L;

    private String name;
    private Position position;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class Position implements Serializable {
        @Serial
        private static final long serialVersionUID = 7955564661493172195L;

        private Float x;
        private Float y;

    }

}
