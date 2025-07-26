package com.springboot.zhiyi.job.datax;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 全量同步 MySQL 数据到 Hive
 */
//@Component
@Slf4j
public class DataXComposeSyncApplication {

    /**
     * 每天凌晨 2 点执行一次全量同步任务
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void run() {
        log.info("FullSyncMysqlToHive task start");

        // DataX 的路径和配置文件路径
        String dataxPath = "/path/to/datax/bin/datax.py"; // 替换为 DataX 的实际路径
        String jobFilePath = "/path/to/job/mysql_to_hive.json"; // 替换为 DataX JSON 配置文件的实际路径

        // 调用 DataX 执行同步任务进程
        ProcessBuilder processBuilder = new ProcessBuilder("python", dataxPath, jobFilePath);
        processBuilder.redirectErrorStream(true); // 将错误输出和标准输出合并

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line); // 将 DataX 的输出记录到日志
            }

            int exitCode = process.waitFor();
            log.info("DataX job exited with code : {}", exitCode);

            if (exitCode != 0) {
                log.error("DataX job failed with exit code {}", exitCode);
            } else {
                log.info("FullSyncMysqlToHive task completed successfully");
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error occurred during DataX job execution", e);
        }
    }
}