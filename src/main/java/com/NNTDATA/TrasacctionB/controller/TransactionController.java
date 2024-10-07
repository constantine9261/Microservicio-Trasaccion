package com.NNTDATA.TrasacctionB.controller;

import com.NNTDATA.TrasacctionB.Model.api.client.CreateTransaccionRequest;
import com.NNTDATA.TrasacctionB.Model.api.client.TransaccionDto;
import com.NNTDATA.TrasacctionB.business.service.ITransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/transacciones")
@Tag(name = "Transacciones", description = "Operaciones de transacciones bancarias")
public class TransactionController {

    @Autowired
    private ITransaccionService transactionService;

    @PostMapping("/deposito")
    @Operation(summary = "Registrar un depósito",
            description = "Permite registrar un depósito en la cuenta especificada.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Depósito registrado con éxito"),
                    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
                    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    public Mono<ResponseEntity<TransaccionDto>> registrarDeposito(@RequestBody CreateTransaccionRequest request) {
        return transactionService.realizarDeposito(request)
                .map(transaction -> new ResponseEntity<>(transaction, HttpStatus.CREATED))
                .onErrorResume(ex -> {
                    TransaccionDto errorResponse = new TransaccionDto();
                    errorResponse.setOnError(ex.getMessage()); // Asegúrate de que TransaccionDto tenga un campo para el mensaje de error.
                    return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST));
                });
    }

    @PostMapping("/retiro")
    @Operation(summary = "Registrar un retiro",
            description = "Permite registrar un retiro de la cuenta especificada.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Retiro registrado con éxito"),
                    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
                    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    public Mono<ResponseEntity<TransaccionDto>> registrarRetiro(@RequestBody CreateTransaccionRequest request) {
        return transactionService.realizarRetiro(request)
                .map(transaction -> new ResponseEntity<>(transaction, HttpStatus.CREATED))
                .onErrorResume(ex -> {
                    TransaccionDto errorResponse = new TransaccionDto();
                    errorResponse.setOnError(ex.getMessage());
                    return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST));
                });
    }

    @PostMapping("/transferencia")
    @Operation(summary = "Registrar una transferencia",
            description = "Permite registrar una transferencia entre cuentas.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Transferencia registrada con éxito"),
                    @ApiResponse(responseCode = "400", description = "Solicitud incorrecta"),
                    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    public Mono<ResponseEntity<TransaccionDto>> registrarTransferencia(@RequestBody CreateTransaccionRequest request) {
        return transactionService.realizarTransferencia(request)
                .map(transaction -> new ResponseEntity<>(transaction, HttpStatus.CREATED))
                .onErrorResume(ex -> {
                    TransaccionDto errorResponse = new TransaccionDto();
                    errorResponse.setOnError(ex.getMessage());
                    return Mono.just(new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST));
                });
    }

    @GetMapping("/historial")
    @Operation(summary = "Consultar historial de transacciones",
            description = "Permite consultar el historial de transacciones realizadas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Historial consultado con éxito"),
                    @ApiResponse(responseCode = "404", description = "No se encontraron transacciones"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            })
    public Mono<ResponseEntity<List<TransaccionDto>>> consultarHistorial() {
        return transactionService.obtenerHistorial()
                .map(transacciones -> new ResponseEntity<>(transacciones, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
