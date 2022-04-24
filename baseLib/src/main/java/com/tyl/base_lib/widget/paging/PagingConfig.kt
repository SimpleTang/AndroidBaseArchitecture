package com.tyl.base_lib.widget.paging

import androidx.paging.PagingConfig

const val PAGE_SIZE = 20

const val INITIAL_INDEX = 1

fun getDefaultPagingConfig():PagingConfig = PagingConfig(PAGE_SIZE)