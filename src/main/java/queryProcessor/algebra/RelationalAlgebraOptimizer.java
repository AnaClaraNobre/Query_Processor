package queryProcessor.algebra;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class RelationalAlgebraOptimizer {
    public String optimize(String originalAlgebra) {
        // Heurística avançada de push-down de seleções
        // 1. Detecta projeção no topo
        Pattern projPattern = Pattern.compile("π ([^(]+) \\((.+)\\)");
        Matcher projMatcher = projPattern.matcher(originalAlgebra);
        if (projMatcher.matches()) {
            String projAttrs = projMatcher.group(1).trim();
            String inner = projMatcher.group(2).trim();
            // 2. Detecta seleção logo abaixo da projeção
            Pattern selPattern = Pattern.compile("σ ([^\\(]+) \\((.+)\\)");
            Matcher selMatcher = selPattern.matcher(inner);
            if (selMatcher.matches()) {
                String conds = selMatcher.group(1).trim();
                String joinExpr = selMatcher.group(2).trim();
                // 3. Divide as condições por AND/∧/;
                String[] conditions = conds.split("\\s*(∧|AND|;)\\s*");
                // 4. Detecta joins aninhados
                // Exemplo: ((A ⨝ cond1 B) ⨝ cond2 C)
                // Para simplificação, vamos assumir joins binários aninhados
                // e que cada condição é do tipo TABELA.atributo = valor
                // 5. Tenta empurrar cada condição para o lado certo
                String optimizedJoin = pushDownSelections(joinExpr, conditions);
                return "π " + projAttrs + " (" + optimizedJoin + ")";
            }
        }
        // Fallback: retorna original
        return originalAlgebra;
    }

    private String pushDownSelections(String joinExpr, String[] conditions) {
        // Se for um join binário
        Pattern joinPattern = Pattern.compile("\\((.+) ⨝ ([^)]+)\\)");
        Matcher joinMatcher = joinPattern.matcher(joinExpr);
        if (joinMatcher.matches()) {
            String left = joinMatcher.group(1).trim();
            String right = joinMatcher.group(2).trim();
            List<String> leftConds = new ArrayList<>();
            List<String> rightConds = new ArrayList<>();
            List<String> joinConds = new ArrayList<>();
            for (String cond : conditions) {
                cond = cond.trim();
                // Se a condição menciona só a tabela da esquerda
                if (isConditionForSide(cond, left)) {
                    leftConds.add(cond);
                } else if (isConditionForSide(cond, right)) {
                    rightConds.add(cond);
                } else {
                    joinConds.add(cond);
                }
            }
            String leftExpr = left;
            if (!leftConds.isEmpty()) {
                leftExpr = "σ " + String.join(" ∧ ", leftConds) + " (" + left + ")";
            }
            String rightExpr = right;
            if (!rightConds.isEmpty()) {
                rightExpr = "σ " + String.join(" ∧ ", rightConds) + " (" + right + ")";
            }
            String joinExprFinal = "(" + leftExpr + " ⨝ " + rightExpr + ")";
            if (!joinConds.isEmpty()) {
                joinExprFinal = "σ " + String.join(" ∧ ", joinConds) + " (" + joinExprFinal + ")";
            }
            return joinExprFinal;
        }
        // Se não for join binário, retorna expressão original com seleções no topo
        if (conditions.length > 0 && !joinExpr.startsWith("σ ")) {
            return "σ " + String.join(" ∧ ", conditions) + " (" + joinExpr + ")";
        }
        return joinExpr;
    }

    private boolean isConditionForSide(String cond, String side) {
        // Heurística: se a condição contém o nome da tabela (antes do ponto)
        Pattern tablePattern = Pattern.compile("([A-Z_]+)\\.");
        Matcher matcher = tablePattern.matcher(cond);
        while (matcher.find()) {
            String table = matcher.group(1);
            if (side.contains(table)) {
                return true;
            }
        }
        return false;
    }
} 