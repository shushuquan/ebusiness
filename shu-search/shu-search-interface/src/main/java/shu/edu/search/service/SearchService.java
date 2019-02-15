package shu.edu.search.service;

import shu.edu.common.pojo.SearchResult;

public interface SearchService {

	SearchResult search(String keyword, int page, int rows) throws Exception;
}
