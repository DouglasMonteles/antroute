package br.com.doug.ants;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AntSimulationDataDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1319616032647301290L;

    private Integer antQuantity;

}
