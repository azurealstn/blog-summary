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
public class ConsultingReportFactory {

    private final String CONSULTING_REPORT_TYPE_01 = "consultingReportType01";
    private final String CONSULTING_REPORT_TYPE_02 = "consultingReportType02";
    private final String CONSULTING_REPORT_TYPE_03 = "consultingReportType03";

    private final ConsultingReportType01 consultingReportType01;
    private final ConsultingReportType02 consultingReportType02;
    private final ConsultingReportType03 consultingReportType03;

    private final ConsultingReportRepository consultingReportRepository;

    public ConsultingReport getConsultingReport(ConsultingReportDto consultingReportDto) {
        ConsultingReport consultingReport = null;
        ConsultingReportDto typeDto = consultingReportRepository.findById(consultingReportDto.getId());
        String fxdForm = typeDto.getFxdForm();
        if (CONSULTING_REPORT_TYPE_01.equals(fxdForm)) {
            consultingReport = consultingReportType01;
        } else if (CONSULTING_REPORT_TYPE_02.equals(fxdForm)) {
            consultingReport = consultingReportType02;
        } else if (CONSULTING_REPORT_TYPE_03.equals(fxdForm)) {
            consultingReport = consultingReportType03;
        }

        return consultingReport;
    }
}
