package taotao.com.search.service;

import taotao.com.common.pojo.SearchResult;

public interface SearchService {
    
	SearchResult search(String queryString ,int page ,int rows);
}
