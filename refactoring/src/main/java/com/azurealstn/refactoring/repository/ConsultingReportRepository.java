package com.azurealstn.refactoring.repository;

import com.azurealstn.refactoring.dto.ConsultingReportDto;

public interface ConsultingReportRepository {

    void save(ConsultingReportDto consultingReportDto);

    ConsultingReportDto findById(Long id);
}
