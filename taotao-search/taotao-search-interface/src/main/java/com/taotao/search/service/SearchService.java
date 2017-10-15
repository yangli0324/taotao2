package com.taotao.search.service;

import com.taotao.search.SearchResult;

public interface SearchService {

	SearchResult search(String queryString, Integer page, Integer rows) throws Exception;

}
