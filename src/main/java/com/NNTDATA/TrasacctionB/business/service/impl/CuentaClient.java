package com.NNTDATA.TrasacctionB.business.service.impl;

import com.NNTDATA.TrasacctionB.Model.business.CuentaResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
public class CuentaClient {
    private final WebClient webClient;

    public CuentaClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/cuentas").build();
    }

    public Mono<CuentaResponse> getCuentaById(Integer cuentaId) {
        return webClient.get()
                .uri("/{id}", cuentaId)
                .retrieve()
                .bodyToMono(CuentaResponse.class);
    }

    // New method to update the account balance
    public Mono<Void> actualizarSaldo(Integer cuentaId, BigDecimal nuevoSaldo) {
        return webClient.put()
                .uri("/cuentas/{id}/saldo", cuentaId)
                .bodyValue(nuevoSaldo)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
