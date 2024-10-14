package com.NNTDATA.TrasacctionB.business.service.impl;


import com.NNTDATA.TrasacctionB.Model.api.client.CreateTransaccionRequest;
import com.NNTDATA.TrasacctionB.Model.api.client.TransaccionDto;
import com.NNTDATA.TrasacctionB.Model.entity.TransaccionEntity;
import com.NNTDATA.TrasacctionB.business.repository.ITransaccionRepository;
import com.NNTDATA.TrasacctionB.business.service.ITransaccionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TransaccionServiceImpl implements ITransaccionService {

    @Autowired
    private final ITransaccionRepository itransaccionRepository;

    @Autowired
    private CuentaClient cuentaClient;

    @Override
    public Mono<TransaccionDto> realizarDeposito(CreateTransaccionRequest request) {
        return cuentaClient.getCuentaById(request.getCuentaOrigenId())
                .flatMap(cuentaOrigenResponse -> {
                    BigDecimal saldoActual = cuentaOrigenResponse.getData().getSaldo();

                    // Calcula el nuevo saldo después del depósito
                    BigDecimal nuevoSaldo = saldoActual.add(request.getMonto());

                    // Crea la entidad de transacción
                    TransaccionEntity transaccionEntity = TransaccionEntity.builder()
                            .tipo("Deposito")
                            .monto(request.getMonto())
                            .fecha(LocalDateTime.now())
                            .cuentaOrigenId(request.getCuentaOrigenId())
                            .saldoOrigen(nuevoSaldo) // Agregar el saldo actualizado después del depósito
                            .build();

                    // Guarda la transacción
                    return itransaccionRepository.save(transaccionEntity)
                            .flatMap(savedTransaccion -> {
                                // Actualizar el saldo en el microservicio de cuentas
                                return this.cuentaClient.actualizarSaldo(request.getCuentaOrigenId(), nuevoSaldo)
                                        .then(Mono.just(this.toDto(savedTransaccion)
                                                .withSaldoOrigen(nuevoSaldo))); // Devuelves el DTO con el saldo actualizado
                            });
                })
                .switchIfEmpty(Mono.error(new Exception("Cuenta de origen no encontrada")));
    }

    @Override
    public Mono<TransaccionDto> realizarRetiro(CreateTransaccionRequest request) {
        return cuentaClient.getCuentaById(request.getCuentaOrigenId())
                .flatMap(cuentaOrigenResponse -> {
                    BigDecimal saldoActual = cuentaOrigenResponse.getData().getSaldo();

                    // Verifica si hay saldo suficiente para el retiro
                    if (saldoActual.compareTo(request.getMonto()) < 0) {
                        return Mono.error(new Exception("Saldo insuficiente para el retiro"));
                    }

                    BigDecimal nuevoSaldo = saldoActual.subtract(request.getMonto());

                    TransaccionEntity transaccionEntity = TransaccionEntity.builder()
                            .tipo("Retiro")
                            .monto(request.getMonto())
                            .fecha(LocalDateTime.now())
                            .cuentaOrigenId(request.getCuentaOrigenId())
                            .saldoOrigen(nuevoSaldo)
                            .build();

                    // Guarda la transacción
                    return itransaccionRepository.save(transaccionEntity)
                            .flatMap(savedTransaccion -> {
                                // Actualiza el saldo en el microservicio de cuentas
                                return this.cuentaClient.actualizarSaldo(request.getCuentaOrigenId(), nuevoSaldo)
                                        .then(Mono.just(this.toDto(savedTransaccion)
                                                .withSaldoOrigen(nuevoSaldo))); // Devuelve el DTO con el saldo actualizado
                            })
                            .onErrorResume(e -> Mono.error(new Exception("Error al actualizar el saldo: " + e.getMessage())));
                })
                .switchIfEmpty(Mono.error(new Exception("Cuenta de origen no encontrada")));
    }

    @Override
    public Mono<TransaccionDto> realizarTransferencia(CreateTransaccionRequest request) {
        return cuentaClient.getCuentaById(request.getCuentaOrigenId())
                .flatMap(cuentaOrigenResponse -> {
                    BigDecimal saldoOrigen = cuentaOrigenResponse.getData().getSaldo();

                    // Verifica si hay saldo suficiente para la transferencia
                    if (saldoOrigen.compareTo(request.getMonto()) < 0) {
                        return Mono.error(new Exception("Saldo insuficiente para la transferencia"));
                    }

                    // Calcula el nuevo saldo de la cuenta de origen
                    BigDecimal nuevoSaldoOrigen = saldoOrigen.subtract(request.getMonto());

                    // Obtiene el saldo de la cuenta de destino
                    return cuentaClient.getCuentaById(request.getCuentaDestinoId())
                            .flatMap(cuentaDestinoResponse -> {
                                BigDecimal saldoDestino = cuentaDestinoResponse.getData().getSaldo();
                                BigDecimal nuevoSaldoDestino = saldoDestino.add(request.getMonto());

                                // Crea la entidad de transacción
                                TransaccionEntity transaccionEntity = TransaccionEntity.builder()
                                        .tipo("Transferencia")
                                        .monto(request.getMonto())
                                        .fecha(LocalDateTime.now())
                                        .cuentaOrigenId(request.getCuentaOrigenId())
                                        .cuentaDestinoId(request.getCuentaDestinoId())
                                        .saldoOrigen(nuevoSaldoOrigen) // Saldo de la cuenta de origen después de la transacción
                                        .saldoDestino(nuevoSaldoDestino) // Saldo de la cuenta de destino después de la transacción
                                        .build();

                                // Guarda la transacción
                                return itransaccionRepository.save(transaccionEntity)
                                        .flatMap(savedTransaccion -> {
                                            // Actualizar el saldo en el microservicio de cuentas (origen y destino)
                                            Mono<Void> actualizarSaldoOrigen = cuentaClient.actualizarSaldo(request.getCuentaOrigenId(), nuevoSaldoOrigen);
                                            Mono<Void> actualizarSaldoDestino = cuentaClient.actualizarSaldo(request.getCuentaDestinoId(), nuevoSaldoDestino);

                                            return Mono.when(actualizarSaldoOrigen, actualizarSaldoDestino)
                                                    .then(Mono.just(toDto(savedTransaccion)
                                                            .withSaldoOrigen(nuevoSaldoOrigen)
                                                            .withSaldoDestino(nuevoSaldoDestino)));
                                        });
                            })
                            .onErrorResume(e -> Mono.error(new Exception("Error al obtener el saldo de la cuenta de destino: " + e.getMessage())));
                })
                .switchIfEmpty(Mono.error(new Exception("Cuenta de origen no encontrada")));
    }

    @Override
    public Mono<List<TransaccionDto>> obtenerHistorial() {
        return itransaccionRepository.findAll()
                .collectList()
                .map(transacciones -> transacciones.stream()
                        .map(this::toDto)
                        .toList()
                );
    }

    private TransaccionDto toDto(TransaccionEntity entity) {
        return TransaccionDto.builder()
                .tipo(entity.getTipo())
                .monto(entity.getMonto())
                .fecha(entity.getFecha())
                .tipoTransaccion(entity.getTipo())
                .cuentaOrigenId(entity.getCuentaOrigenId())
                .cuentaDestinoId(entity.getCuentaDestinoId())
                .saldoOrigen(entity.getSaldoOrigen())
                .saldoDestino(entity.getSaldoDestino())
                .build();
    }


}
