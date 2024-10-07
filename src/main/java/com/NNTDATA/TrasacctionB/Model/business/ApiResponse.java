package com.NNTDATA.TrasacctionB.Model.business;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private Metadata metadata;
    private CuentaResponse data;
}
