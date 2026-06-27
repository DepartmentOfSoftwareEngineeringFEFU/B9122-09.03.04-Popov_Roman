package plugin.prep.assessment.feature.readiness.config;

import java.math.*;

public final class ReadinessCalculationConfig {

    public static final int MIN_ANSWERS_PER_TOPIC = 1;

    public static final BigDecimal MASTERED_ACCURACY_THRESHOLD = BigDecimal.valueOf(70);

    public static final BigDecimal CRITICAL_ACCURACY_THRESHOLD = BigDecimal.valueOf(40);

    public static final BigDecimal MIN_COVERAGE_THRESHOLD = BigDecimal.valueOf(50);

    public static final BigDecimal READY_INDEX_THRESHOLD = BigDecimal.valueOf(70);

    public static final BigDecimal PROGRESS_DELTA_THRESHOLD = BigDecimal.valueOf(5);

    private ReadinessCalculationConfig() {
    }

}
