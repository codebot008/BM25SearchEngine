package com.irassignment.searcher;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;
import org.apache.lucene.util.Version;

import com.irassignment.indexer.SimpleIndexer;

public class SimpleSearcher {
	
	private static final String indexDirectory = "/Users/ankitjena/Downloads/LuceneIndex";


	private static final int maxHits = 100;
	
	
	public static void SearchLauncher(String queryString) throws Exception
	{
		File indexDir = new File(indexDirectory);

		SimpleSearcher searcher = new SimpleSearcher();

		searcher.searchIndex(indexDir, queryString);
	}

	private void searchIndex(File indexDir, String queryStr)
			throws Exception {

		Directory directory = FSDirectory.open(indexDir);
		
		IndexReader  indexReader  = DirectoryReader.open(directory);

		IndexSearcher searcher = new IndexSearcher(indexReader);
		
		searcher.setSimilarity(SimpleIndexer.similarity);
		
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
		
		QueryBuilder builder = new QueryBuilder(analyzer);
		
		Query query = builder.createPhraseQuery("content", queryStr);
		
		TopDocs topDocs =searcher.search(query, maxHits);
		
		ScoreDoc[] hits = topDocs.scoreDocs;
		
        for (int i = 0; i < hits.length; i++) {
        	
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println(d.get("fileName") + " Score :" + hits[i].score);
            
        }
        
		System.out.println("Documents Found : " + hits.length);

	}
	
	public static void searchIndexWithPhraseQuery(String string1, String string2, int slop) throws IOException,ParseException {

		Directory directory = FSDirectory.open(new File(indexDirectory));

		IndexReader indexReader = DirectoryReader.open(directory);
		
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		Term term1 = new Term("filename", string1);
		Term term2 = new Term("filename", string2);
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.add(term1);
		phraseQuery.add(term2);
		phraseQuery.setSlop(slop);
		System.out.println(phraseQuery.toString());
		TopDocs topDocs = indexSearcher.search(phraseQuery, maxHits);
		ScoreDoc[] hits = topDocs.scoreDocs;
		
        for (int i = 0; i < hits.length; i++) {
        	
            int docId = hits[i].doc;
            Document d = indexSearcher.doc(docId);
            System.out.println(d.get("fileName") + " Score :" + hits[i].score);
            
        }
        
		System.out.println("Documents Found : " + hits.length);
		}

}
