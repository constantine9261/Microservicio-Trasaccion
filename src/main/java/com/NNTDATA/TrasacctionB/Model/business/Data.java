package com.NNTDATA.TrasacctionB.Model.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@lombok.Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Data {
    private Long cuentaId; // Asegúrate de que el tipo coincida
    private String numeroCuenta;
    private BigDecimal saldo; // Usa BigDecimal para el saldo
    private String tipoCuenta;
    private List<Cliente> clientes;
}
