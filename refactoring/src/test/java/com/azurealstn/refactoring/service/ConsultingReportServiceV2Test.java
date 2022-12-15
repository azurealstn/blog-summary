package com.azurealstn.refactoring.service;

import com.azurealstn.refactoring.dto.ConsultingReportDto;
import com.azurealstn.refactoring.repository.ConsultingReportRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConsultingReportServiceV2Test {

    @Autowired
    private ConsultingReportServiceV2 consultingReportServiceV2;

    @Autowired
    private ConsultingReportRepository consultingReportRepository;

    @Test
    @DisplayName("Factory를 이용한 분기문 처리 Type1")
    void consultingReportType01() {
        //given
        ConsultingReportDto consultingReportDto = new ConsultingReportDto(1L, "consultingReportType01");
        consultingReportRepository.save(consultingReportDto);

        ConsultingReportDto findConsultingReportDto = consultingReportRepository.findById(1L);

        //when
        String result = consultingReportServiceV2.getConsultingReportListExcel(findConsultingReportDto);

        //then
        assertThat(result).isEqualTo("Type1 엑셀 정보를 가져온다.");
    }

    @Test
    @DisplayName("Factory를 이용한 분기문 처리 Type2")
    void consultingReportType02() {
        //given
        ConsultingReportDto consultingReportDto = new ConsultingReportDto(1L, "consultingReportType02");
        consultingReportRepository.save(consultingReportDto);

        ConsultingReportDto findConsultingReportDto = consultingReportRepository.findById(1L);

        //when
        String result = consultingReportServiceV2.getConsultingReportListExcel(findConsultingReportDto);

        //then
        assertThat(result).isEqualTo("Type2 엑셀 정보를 가져온다.");
    }

    @Test
    @DisplayName("Factory를 이용한 분기문 처리 Type3")
    void consultingReportType03() {
        //given
        ConsultingReportDto consultingReportDto = new ConsultingReportDto(1L, "consultingReportType03");
        consultingReportRepository.save(consultingReportDto);

        ConsultingReportDto findConsultingReportDto = consultingReportRepository.findById(1L);

        //when
        String result = consultingReportServiceV2.getConsultingReportListExcel(findConsultingReportDto);

        //then
        assertThat(result).isEqualTo("Type3 엑셀 정보를 가져온다.");
    }
}