package com.NNTDATA.TrasacctionB.Model.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    private Long clienteId;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
}
