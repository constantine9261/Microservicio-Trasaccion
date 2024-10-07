package com.NNTDATA.TrasacctionB.Model.business;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {
    private boolean success;
    private int status;
    private String businessId;
    private String message;
    private Integer totalRecords;
    private Integer pageSize;
    private Integer pageNumber;
    private Integer totalPages;
}
