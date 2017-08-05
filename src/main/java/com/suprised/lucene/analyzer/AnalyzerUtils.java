package com.suprised.lucene.analyzer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class AnalyzerUtils {

    private static final String[] tokensFromAnalyzers(Analyzer analyzer, String text) throws IOException {
        TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));
        List<String> results = new ArrayList<String>();
        stream.reset();
        while(stream.incrementToken()) {
            CharTermAttribute cta =  stream.getAttribute(CharTermAttribute.class);
            results.add(cta.toString());
        }
        stream.end();
        stream.close();
        return results.toArray(new String[0]);
    }
    
    /**
     * 将分词的结果显示出来
     * @param analyzer
     * @param text
     * @throws IOException
     */
    public static final void displayTokens(Analyzer analyzer, String text) throws IOException {
        String[] tokens = tokensFromAnalyzers(analyzer, text);
        for (String token : tokens) {
            System.out.print("[" + token + "]  ");
        }
    }
    
}
