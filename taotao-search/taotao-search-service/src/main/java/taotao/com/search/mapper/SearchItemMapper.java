package taotao.com.search.mapper;

import java.util.List;

import taotao.com.common.pojo.SearchItem;

public interface SearchItemMapper {
    List<SearchItem> getItemList();
    SearchItem getItemById(long itemId);
}
