package com.taotao.search.mapper;

import java.util.List;

import com.taotao.search.SearchItem;

public interface SearchItemMapper {

	List<SearchItem> getItemList();

	SearchItem getItemById(long itemId);
}
