package com.NNTDATA.TrasacctionB.Model.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Document(collection = "transacciones")
public class TransaccionEntity implements Serializable {

    @Id
    private String id;
    private String tipo; // Depósito, Retiro, Transferencia
    private BigDecimal monto;
    private LocalDateTime fecha;
    private Integer cuentaOrigenId;
    private Integer cuentaDestinoId; // Puede ser null si no es transferencia
    private String onError; // Mensaje de error en caso de falla
}
