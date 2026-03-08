package com.zhidao.demo.forum.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LocalSummarizer {

    // Very small heuristic summarizer: pick up to `maxSentences` earliest non-empty sentences,
    // join them and trim to maxChars.
    public String summarize(String text, int maxSentences, int maxChars) {
        if (text == null || text.isBlank()) return "";
        String[] sentences = text.split("(?<=[.!?])\\s+");
        List<String> chosen = new ArrayList<>();
        for (String s : sentences) {
            String t = s.trim();
            if (!t.isEmpty()) {
                chosen.add(t);
                if (chosen.size() >= maxSentences) break;
            }
        }
        String joined = String.join(" ", chosen);
        if (joined.length() > maxChars) {
            return joined.substring(0, Math.max(0, maxChars - 3)) + "...";
        }
        return joined;
    }
}
