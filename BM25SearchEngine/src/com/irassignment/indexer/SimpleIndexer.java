package com.irassignment.indexer;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SimpleIndexer {
	
	public static BM25Similarity similarity = new BM25Similarity();

	private static final String indexDirectory = "/Users/ankitjena/Downloads/LuceneIndex";

	private static final String dirToBeIndexed = "/Users/ankitjena/Downloads/Data";

	public static void IndexLauncher(int fraction) throws Exception {
		

		File indexDir = new File(indexDirectory);
		
		if(!indexDir.exists())
		{
			indexDir.mkdir();
		}

		File dataDir = new File(dirToBeIndexed);
		
		if(!dataDir.exists())
		{
			dataDir.mkdir();
		}

		SimpleIndexer indexer = new SimpleIndexer();

		int numIndexed = indexer.index(indexDir, dataDir, fraction);

		System.out.println("Total files indexed " + numIndexed);
	}

	private int index(File indexDir, File dataDir, int fraction) throws IOException {

		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,
				analyzer);
		
		config.setSimilarity(similarity);

		IndexWriter indexWriter = new IndexWriter(FSDirectory.open(indexDir),
				config);

		File[] files = dataDir.listFiles();
		
		int fileCount = files.length;
		
		int count = Math.round((fraction * fileCount) / 100);

		for (File f : files) {
			
			System.out.println("Indexing file " + f.getCanonicalPath());

			Document doc = new Document();

			doc.add(new TextField("content", new FileReader(f)));

			doc.add(new StoredField("fileName", f.getCanonicalPath()));

			indexWriter.addDocument(doc);
			
			count--;
			
			if(count == 0)
			{
				break;
			}
		}

		int numIndexed = indexWriter.maxDoc();

		indexWriter.close();

		return numIndexed;

	}

}
