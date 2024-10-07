package com.NNTDATA.TrasacctionB.Model.business;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaResponse {

    private Metadata metadata;
    private com.NNTDATA.TrasacctionB.Model.business.Data data;
}
