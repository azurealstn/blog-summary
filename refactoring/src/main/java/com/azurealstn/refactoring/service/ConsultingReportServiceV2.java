package com.azurealstn.refactoring.service;

import com.azurealstn.refactoring.ConsultingReport;
import com.azurealstn.refactoring.dto.ConsultingReportDto;
import com.azurealstn.refactoring.repository.ConsultingReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConsultingReportServiceV2 {

    private final ConsultingReportFactory consultingReportFactory;

    public String getConsultingReportListExcel(ConsultingReportDto consultingReportDto) {
        ConsultingReport consultingReport = consultingReportFactory.getConsultingReport(consultingReportDto);
        return consultingReport.process();
    }
}
