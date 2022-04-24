package com.tyl.base_lib.widget.paging

import androidx.lifecycle.LiveData
import androidx.paging.*
import kotlinx.coroutines.CoroutineScope

class LazyPager<V : Any>(private val scope: CoroutineScope, private val block: suspend (index: Int, size: Int) -> List<V>) : Lazy<LiveData<PagingData<V>>> {

    private var cached: Pager<Int, V>? = null

    override val value: LiveData<PagingData<V>>
        get() = Pager(getDefaultPagingConfig()) {
            object : PagingSource<Int, V>() {

                override fun getRefreshKey(state: PagingState<Int, V>): Int {
                    return INITIAL_INDEX
                }

                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> {
                    return try {
                        val index = params.key ?: INITIAL_INDEX
                        val data = block(index, params.loadSize)
                        LoadResult.Page(data, getPagePrevKey(index), getPageNextKey(index, data.size, params.loadSize))
                    } catch (e: Exception) {
                        LoadResult.Error(e)
                    }
                }

            }
        }.liveData

    override fun isInitialized(): Boolean {
        return cached != null
    }

}