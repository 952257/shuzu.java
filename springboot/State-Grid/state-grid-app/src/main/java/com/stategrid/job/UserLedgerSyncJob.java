package com.stategrid.job;

import com.stategrid.service.UserLedgerSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLedgerSyncJob implements ApplicationRunner {

    private final UserLedgerSyncService userLedgerSyncService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("应用启动，立即执行一次用户台账同步");
        try {
            userLedgerSyncService.syncOnce();
        } catch (Exception e) {
            log.warn("启动时同步失败（请确认 blade-mock 已先启动）: {}", e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void syncAtThreeAM() {
        userLedgerSyncService.syncOnce();
    }
}
