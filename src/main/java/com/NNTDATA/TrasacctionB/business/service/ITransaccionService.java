package com.NNTDATA.TrasacctionB.business.service;



import com.NNTDATA.TrasacctionB.Model.api.client.CreateTransaccionRequest;
import com.NNTDATA.TrasacctionB.Model.api.client.TransaccionDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ITransaccionService {

    Mono<TransaccionDto> realizarDeposito(CreateTransaccionRequest request);

    Mono<TransaccionDto> realizarRetiro(CreateTransaccionRequest request);

    Mono<TransaccionDto> realizarTransferencia(CreateTransaccionRequest request);

    Mono<List<TransaccionDto>> obtenerHistorial();
}