package com.github.trimonovds.happyswift.inspections

import com.github.trimonovds.happyswift.doRenameRefactoring
import com.github.trimonovds.happyswift.hasUnderscores
import com.github.trimonovds.happyswift.underscoresToCamelCase
import com.intellij.codeHighlighting.HighlightDisplayLevel
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.childrenOfType
import com.jetbrains.swift.SwiftParserTypes
import com.jetbrains.swift.codeinsight.highlighting.inspections.SwiftInspection
import com.jetbrains.swift.psi.SwiftTypeDeclaration
import com.jetbrains.swift.psi.SwiftVisitor

class RenameSwiftTypeToUseCamelCaseInspection : SwiftInspection() {
    override fun getDisplayName(): String = "Rename types containing '_' to use camel case"
    override fun getDefaultLevel(): HighlightDisplayLevel = HighlightDisplayLevel.WARNING

    private class QuickFix : LocalQuickFix {
        override fun getFamilyName() = "Rename to use camel case"
        override fun startInWriteAction(): Boolean = true
        override fun applyFix(project: Project, problemDescriptor: ProblemDescriptor) {
            doRenameRefactoring(problemDescriptor.psiElement, ::underscoresToCamelCase)
        }
    }

    override fun buildVisitor(holder: ProblemsHolder): SwiftVisitor {
        return object : SwiftVisitor() {
            override fun visitTypeDeclaration(declaration: SwiftTypeDeclaration) {
                if (declaration.hasUnderscores() == true) {
                    val identifier = declaration.childrenOfType<LeafPsiElement>()
                        .first { it.elementType == SwiftParserTypes.IDENTIFIER }
                    holder.registerProblem(identifier, "Type name '${declaration.name}' has '_'", QuickFix())
                }
            }
        }
    }
}