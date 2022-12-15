package com.azurealstn.refactoring.repository;

import com.azurealstn.refactoring.dto.ConsultingReportDto;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ConsultingReportRepositoryImpl implements ConsultingReportRepository {

    private static Map<Long, ConsultingReportDto> store = new ConcurrentHashMap<>();

    @Override
    public void save(ConsultingReportDto consultingReportDto) {
        store.put(consultingReportDto.getId(), consultingReportDto);
    }

    @Override
    public ConsultingReportDto findById(Long id) {
        return store.get(id);
    }
}
