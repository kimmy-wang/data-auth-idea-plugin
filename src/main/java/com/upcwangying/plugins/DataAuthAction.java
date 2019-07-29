package com.upcwangying.plugins;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

/**
 * @author WANGY
 * @date 2019-07-20 21:48
 */
public class DataAuthAction extends AnAction {

    /**
     * @param e
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        //获取Editor和Project对象
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if (editor == null || project == null) {
            return;
        }

        //获取SelectionModel和Document对象
        Document document = editor.getDocument();

        //获取当前事件触发时，光标所在的元素
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        CaretModel caretModel = editor.getCaretModel();
        int offset = caretModel.getOffset();
        int lineNumber = document.getLineNumber(offset);
        int preLineStartOffset = document.getLineStartOffset(lineNumber);

        //如果光标选择的是类，弹出对话框提醒
        if (psiElement instanceof PsiClass || psiElement instanceof PsiMethod) {
            String name = psiElement instanceof PsiClass ? ((PsiClass) psiElement).getName() : ((PsiMethod) psiElement).getName();
            String insertString = psiElement instanceof PsiClass
                    ? "@MapperDesc(id = \"NAME\", title = \"NAME\")\n".replaceAll("NAME", name)
                    : "\t@DataAuthSupport\n\t@DataAuth\n\t@MapperDesc(id = \"NAME\", title = \"NAME\")\n".replaceAll("NAME", name);

            Runnable runnable = () -> document.insertString(preLineStartOffset, insertString);
            //加入任务，由IDEA调度执行这个任务
            WriteCommandAction.runWriteCommandAction(project, runnable);
        }

    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Editor editor = e.getData(PlatformDataKeys.EDITOR);

        if (editor != null) {
            e.getPresentation().setEnabled(true);
        } else {
            e.getPresentation().setEnabled(false);
        }
    }

}
