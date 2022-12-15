package com.azurealstn.refactoring.dto;

import lombok.Data;

@Data
public class ConsultingReportDto {

    private Long id;
    private String fxdForm;

    public ConsultingReportDto(Long id, String fxdForm) {
        this.id = id;
        this.fxdForm = fxdForm;
    }
}
