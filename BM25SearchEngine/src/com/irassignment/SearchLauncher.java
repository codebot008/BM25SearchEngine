package com.irassignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import com.irassignment.indexer.SimpleIndexer;
import com.irassignment.searcher.SimpleSearcher;

public class SearchLauncher {
	
	private static final String indexDirectory = "/Users/ankitjena/Downloads/LuceneIndex";
	
	private static void deleteIndexFolder(File indexFolder)
	{
		File files[] = indexFolder.listFiles();
		for(File file : files)
		{
			file.delete();
		}
		indexFolder.delete();
	}

	public static void main(String[] args) throws Exception {
		File file = new File(indexDirectory);
		if(file.exists())
		{
			deleteIndexFolder(file);
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter percentage of corpus to be indexed : ");
		int indexFraction = Integer.parseInt(br.readLine());
		System.out.print("Enter search query : ");
		String query = br.readLine();
		SimpleIndexer.IndexLauncher(indexFraction);
		SimpleSearcher.SearchLauncher(query);
	}
}
