package com.azurealstn.refactoring.service;

import com.azurealstn.refactoring.ConsultingReport;
import com.azurealstn.refactoring.dto.ConsultingReportDto;
import com.azurealstn.refactoring.repository.ConsultingReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsultingReportType01 implements ConsultingReport {

    @Override
    public String process() {
        log.info("많은 로직이 있다고 가정");
        return "Type1 엑셀 정보를 가져온다.";
    }
}
