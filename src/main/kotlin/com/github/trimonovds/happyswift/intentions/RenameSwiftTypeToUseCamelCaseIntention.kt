package com.github.trimonovds.happyswift.intentions

import com.github.trimonovds.happyswift.*
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

class RenameSwiftTypeToUseCamelCaseIntention : PsiElementBaseIntentionAction() {
    override fun getText() = "Rename to use camel case"
    override fun getFamilyName() = "RenameSwiftTypeToUseCamelCaseIntention"

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement) =
        element.isInSwiftFile() && element.findSwiftTypeDeclaration()?.hasUnderscores() == true

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        if (editor != null) {
            doRenameRefactoring(element, ::underscoresToCamelCase)
        }
    }
}
