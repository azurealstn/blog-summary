package com.azurealstn.refactoring.service;

import com.azurealstn.refactoring.ConsultingReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsultingReportType03 implements ConsultingReport {

    @Override
    public String process() {
        log.info("많은 로직이 있다고 가정");
        return "Type3 엑셀 정보를 가져온다.";
    }
}
