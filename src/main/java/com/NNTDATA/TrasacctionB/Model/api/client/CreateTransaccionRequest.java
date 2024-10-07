package com.NNTDATA.TrasacctionB.Model.api.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTransaccionRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer cuentaOrigenId;  // ID de la cuenta de origen

  private Integer cuentaDestinoId;  // ID de la cuenta de destino (opcional, solo para transferencias)

  private BigDecimal monto;  // Monto de la transacción

  private String tipoTransaccion; // Puede ser "Depósito", "Retiro", etc.

  private String onError; // Mensaje de error en caso de falla


}
