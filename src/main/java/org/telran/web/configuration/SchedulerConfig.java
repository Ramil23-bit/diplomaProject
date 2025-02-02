package org.telran.web.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfig {
    @Value("${task.fixedRate}")
    private long fixedRate;

    public long getFixedRate() {
        return fixedRate;
    }
}
