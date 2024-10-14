package com.NNTDATA.TrasacctionB.Model.api.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransaccionDto {

    private String tipo; // Depósito, Retiro, Transferencia
    private BigDecimal monto;
    private LocalDateTime fecha;
    private String tipoTransaccion;
    private Integer cuentaOrigenId;
    private Integer cuentaDestinoId; // Puede ser null si no es transferencia
    private BigDecimal saldoOrigen; // Saldo de la cuenta de origen
    private BigDecimal saldoDestino; // Saldo de la cuenta de destino
    private String onError; // Mensaje de error en caso de falla


    // Método para crear un nuevo TransaccionDto con un monto actualizado
    public TransaccionDto withUpdatedMonto(BigDecimal nuevoMonto) {
        return TransaccionDto.builder()
                .tipo(this.tipo)
                .monto(nuevoMonto)
                .fecha(this.fecha)
                .tipoTransaccion(this.tipoTransaccion)
                .cuentaOrigenId(this.cuentaOrigenId)
                .cuentaDestinoId(this.cuentaDestinoId)
                .saldoOrigen(this.saldoOrigen)
                .saldoDestino(this.saldoDestino)
                .onError(this.onError)
                .build();
    }

    // Método para crear un nuevo TransaccionDto con un saldo de origen actualizado
    public TransaccionDto withSaldoOrigen(BigDecimal nuevoSaldoOrigen) {
        return TransaccionDto.builder()
                .tipo(this.tipo)
                .monto(this.monto)
                .fecha(this.fecha)
                .tipoTransaccion(this.tipoTransaccion)
                .cuentaOrigenId(this.cuentaOrigenId)
                .cuentaDestinoId(this.cuentaDestinoId)
                .saldoOrigen(nuevoSaldoOrigen)
                .saldoDestino(this.saldoDestino)
                .onError(this.onError)
                .build();
    }

    // Método para crear un nuevo TransaccionDto con un saldo de destino actualizado
    public TransaccionDto withSaldoDestino(BigDecimal nuevoSaldoDestino) {
        return TransaccionDto.builder()
                .tipo(this.tipo)
                .monto(this.monto)
                .fecha(this.fecha)
                .tipoTransaccion(this.tipoTransaccion)
                .cuentaOrigenId(this.cuentaOrigenId)
                .cuentaDestinoId(this.cuentaDestinoId)
                .saldoOrigen(this.saldoOrigen)
                .saldoDestino(nuevoSaldoDestino)
                .onError(this.onError)
                .build();
    }

}
