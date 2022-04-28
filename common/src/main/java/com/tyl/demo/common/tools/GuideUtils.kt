package com.tyl.demo.common.tools

import com.tyl.base_lib.provider.impl.kvprovider.KVOwner
import com.tyl.base_lib.provider.impl.kvprovider.KVProvider
import com.tyl.base_lib.provider.impl.kvprovider.kvBoolean
import com.tyl.base_lib.provider.providers

object GuideUtils : KVOwner {
    var isFirstIn by kvBoolean()
}