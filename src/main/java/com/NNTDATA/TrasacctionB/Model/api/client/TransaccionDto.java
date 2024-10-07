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
    private String onError; // Mensaje de error en caso de falla


}
