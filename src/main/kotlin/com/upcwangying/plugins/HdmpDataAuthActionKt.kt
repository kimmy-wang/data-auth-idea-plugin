package com.upcwangying.plugins

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import javax.swing.Icon

/**
 * @author WANGY
 * @date 2019-07-21 13:53
 */
class HdmpDataAuthActionKt : AnAction {

    constructor() : this(null, null, null)

    constructor(text: String?, description: String?, icon: Icon?) : super(text, description, icon)

    override fun actionPerformed(e: AnActionEvent) {
        //获取Editor和Project对象
        val editor = e.getData(PlatformDataKeys.EDITOR) ?: return
        val project = e.getData(PlatformDataKeys.PROJECT) ?: return

        //获取SelectionModel和Document对象
        val document = editor.document

        //获取当前事件触发时，光标所在的元素
        val psiElement = e.getData(LangDataKeys.PSI_ELEMENT)
        val caretModel = editor.caretModel
        val offset = caretModel.offset
        val lineNumber = document.getLineNumber(offset)
        val preLineStartOffset = document.getLineStartOffset(lineNumber)

        //如果光标选择的是类，弹出对话框提醒
        if (psiElement is PsiClass || psiElement is PsiMethod) {
            val name: String = if (psiElement is PsiClass) psiElement.name!! else (psiElement as PsiMethod).name
            val insertString = if (psiElement is PsiClass)
                "@MapperDesc(id = \"$name\", title = \"$name\")\n"
            else
                "\t@DataAuthSupport\n\t@DataAuth\n\t@MapperDesc(id = \"$name\", title = \"$name\")\n"

            val runnable = { document.insertString(preLineStartOffset, insertString) }
            //加入任务，由IDEA调度执行这个任务
            WriteCommandAction.runWriteCommandAction(project, runnable)
        }
    }

}