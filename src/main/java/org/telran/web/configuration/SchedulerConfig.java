package org.telran.web.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for scheduling tasks.
 * Loads fixed rate configuration from application properties.
 */
@Configuration
public class SchedulerConfig {

    @Value("${task.fixedRate}")
    private long fixedRate;

    /**
     * Retrieves the fixed rate for scheduled tasks.
     *
     * @return The fixed rate in milliseconds.
     */
    public long getFixedRate() {
        return fixedRate;
    }
}
