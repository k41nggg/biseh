package com.zhidao.demo.forum;

import com.zhidao.demo.forum.service.LocalSummarizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalSummarizerTests {

    @Test
    public void testSummarizeShort() {
        LocalSummarizer s = new LocalSummarizer();
        String t = "这是第一句。 这是第二句。 这是第三句。 这是第四句。";
        String out = s.summarize(t, 2, 100);
        Assertions.assertTrue(out.contains("第一句") || out.contains("第二句"));
    }
}
