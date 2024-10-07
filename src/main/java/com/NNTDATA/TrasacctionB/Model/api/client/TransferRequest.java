package com.NNTDATA.TrasacctionB.Model.api.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest {
    private String cuentaOrigenId; // ID de la cuenta de origen
    private String cuentaDestinoId; // ID de la cuenta de destino
    private double monto; // Monto a transferir
}
