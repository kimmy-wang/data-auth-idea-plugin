package com.upcwangying.plugins.groups

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.upcwangying.plugins.icons.DataAuthIcons

/**
 * @author WANGY
 * @date 2019-07-21 18:56
 */
class DataAuthGroup : DefaultActionGroup {

    constructor() : this(null, true)

    constructor(shortName: String?, popup: Boolean) : super(shortName, popup)

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isVisible = true
        e.presentation.isEnabled = editor != null
        e.presentation.icon = DataAuthIcons.HUADI
    }
}
