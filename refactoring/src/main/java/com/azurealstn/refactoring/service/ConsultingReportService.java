package com.azurealstn.refactoring.service;

import com.azurealstn.refactoring.dto.ConsultingReportDto;
import com.azurealstn.refactoring.repository.ConsultingReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConsultingReportService {

    private final ConsultingReportRepository consultingReportRepository;

    public String getConsultingReportListExcel(ConsultingReportDto consultingReportDto) {
        ConsultingReportDto typeDto = consultingReportRepository.findById(consultingReportDto.getId());
        String fxdForm = typeDto.getFxdForm();
        if ("consultingReportType01".equals(fxdForm)) {
            log.info("많은 로직이 있다고 가정");
            return "Type1 엑셀 정보를 가져온다.";
        } else if ("consultingReportType02".equals(fxdForm)) {
            log.info("많은 로직이 있다고 가정");
            return "Type2 엑셀 정보를 가져온다.";
        } else if ("consultingReportType03".equals(fxdForm)) {
            log.info("많은 로직이 있다고 가정");
            return "Type3 엑셀 정보를 가져온다.";
        }

        return null;
    }
}
