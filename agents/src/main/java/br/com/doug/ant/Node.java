package br.com.doug.ant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node {

    private String name;
    private Float distance;
    private Position position;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Position {
        private Float x;
        private Float y;
    }

}
