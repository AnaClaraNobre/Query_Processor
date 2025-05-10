package queryProcessor.plan;

import java.util.ArrayList;
import java.util.List;

public class ExecutionPlanOptimizer {
    public List<String> optimize(List<String> originalPlan) {
        // Aqui você implementará as heurísticas de otimização
        List<String> optimized = new ArrayList<>();
        for (String step : originalPlan) {
            optimized.add("[OTIMIZADO] " + step);
        }
        return optimized;
    }
} 