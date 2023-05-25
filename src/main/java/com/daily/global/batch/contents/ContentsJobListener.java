package com.daily.global.batch.contents;

import com.daily.domain.content.repository.ContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class ContentsJobListener implements JobExecutionListener {

    private final ContentRepository contentRepository;

    public ContentsJobListener(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
//        Collection<ContentsEntity> contents = contentsRepository.findAllByUpdDtm(LocalDate.now());

        // Job 수행시간
        long time = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();

        log.info("New 컨텐츠 업데이트 배치 프로그램");
        log.info("--------------------------------");
        log.info("처리 시간 {}millis", time);

    }
}
