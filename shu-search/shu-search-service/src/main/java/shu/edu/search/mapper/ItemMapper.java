package shu.edu.search.mapper;

import java.util.List;

import shu.edu.common.pojo.SearchItem;

public interface ItemMapper {

	List<SearchItem> getItemList();
	SearchItem getItemById(long itemId);
}
