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

                    // Verificar si hay suficiente saldo para el retiro
                    if (saldoActual.compareTo(request.getMonto()) < 0) {
                        return Mono.error(new Exception("Saldo insuficiente para el retiro"));
                    }

                    TransaccionEntity transaccionEntity = TransaccionEntity.builder()
                            .tipo("Deposito")
                            .monto(request.getMonto())
                            .fecha(LocalDateTime.now())
                            .cuentaOrigenId(request.getCuentaOrigenId())
                            .build();

                    return itransaccionRepository.save(transaccionEntity)
                            .flatMap(savedTransaccion -> {
                                return Mono.just(toDto(savedTransaccion));
                            });
                })
                .switchIfEmpty(Mono.error(new Exception("Cuenta de origen no encontrada")));
    }

    @Override
    public Mono<TransaccionDto> realizarRetiro(CreateTransaccionRequest request) {
        return cuentaClient.getCuentaById(request.getCuentaOrigenId())
                .flatMap(cuentaOrigenResponse -> {
                    BigDecimal saldoActual = cuentaOrigenResponse.getData().getSaldo();

                    if (saldoActual.compareTo(request.getMonto()) < 0) {
                        return Mono.error(new Exception("Saldo insuficiente para el retiro"));
                    }

                    TransaccionEntity transaccionEntity = TransaccionEntity.builder()
                            .tipo("Retiro")
                            .monto(request.getMonto())
                            .fecha(LocalDateTime.now())
                            .cuentaOrigenId(request.getCuentaOrigenId())
                            .build();

                    return itransaccionRepository.save(transaccionEntity)
                            .flatMap(savedTransaccion -> {

                                return Mono.just(toDto(savedTransaccion));
                            });
                })
                .switchIfEmpty(Mono.error(new Exception("Cuenta de origen no encontrada")));
    }

    @Override
    public Mono<TransaccionDto> realizarTransferencia(CreateTransaccionRequest request) {
        return cuentaClient.getCuentaById(request.getCuentaOrigenId())
                .flatMap(cuentaOrigenResponse -> {
                    BigDecimal saldoActual = cuentaOrigenResponse.getData().getSaldo();

                    if (saldoActual.compareTo(request.getMonto()) < 0) {
                        return Mono.error(new Exception("Saldo insuficiente para la transferencia"));
                    }

                    return cuentaClient.getCuentaById(request.getCuentaDestinoId())
                            .flatMap(cuentaDestinoResponse -> {
                                TransaccionEntity transaccionEntity = TransaccionEntity.builder()
                                        .tipo("Transferencia")
                                        .monto(request.getMonto())
                                        .fecha(LocalDateTime.now())
                                        .cuentaOrigenId(request.getCuentaOrigenId())
                                        .cuentaDestinoId(request.getCuentaDestinoId())
                                        .build();

                                return itransaccionRepository.save(transaccionEntity)
                                        .flatMap(savedTransaccion -> {

                                            return Mono.just(toDto(savedTransaccion));
                                        });
                            })
                            .switchIfEmpty(Mono.error(new Exception("Cuenta de destino no encontrada")));
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
                .monto(entity.getMonto())
                .fecha(entity.getFecha())
                .tipoTransaccion(entity.getTipo())
                .cuentaOrigenId(entity.getCuentaOrigenId())
                .cuentaDestinoId(entity.getCuentaDestinoId())
                .tipo(entity.getTipo())
                .build();
    }
}
