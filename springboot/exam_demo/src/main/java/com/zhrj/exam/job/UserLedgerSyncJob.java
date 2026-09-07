package com.zhrj.exam.job;

import com.zhrj.exam.service.UserLedgerSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserLedgerSyncJob {

    private final UserLedgerSyncService syncService;

    public UserLedgerSyncJob(UserLedgerSyncService syncService) {
        this.syncService = syncService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runOnStartup() {
        log.info("startup ledger sync begin");
        runSafely();
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void runAtThree() {
        log.info("cron 03:00 ledger sync begin");
        runSafely();
    }

    private void runSafely() {
        try {
            syncService.syncOnce();
        } catch (Exception ex) {
            log.error("ledger sync failed", ex);
        }
    }
}
