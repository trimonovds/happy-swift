package com.github.trimonovds.happyswift

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.jetbrains.swift.SwiftParserTypes
import com.jetbrains.swift.psi.SwiftTypeDeclaration

internal fun doRenameRefactoring(element: PsiElement, rename: (String) -> String) {
    val declaration: SwiftTypeDeclaration = element.findSwiftTypeDeclaration()!!
    declaration.setName(rename(declaration.name!!))
}

internal fun underscoresToCamelCase(name: String): String {
    return name.split('_')
        .filter { it.isNotEmpty() }
        .mapIndexed { i, word ->
            if (i == 0) word else word.replaceFirstChar { it.uppercaseChar() }
        }
        .joinToString("")
}

internal fun SwiftTypeDeclaration.hasUnderscores() = name?.contains('_')

internal fun PsiElement.findSwiftTypeDeclaration(): SwiftTypeDeclaration? {
    val isOnTypeDeclarationIdentifier =
        this is LeafPsiElement &&
                elementType == SwiftParserTypes.IDENTIFIER &&
                (parent as? SwiftTypeDeclaration) != null
    return if (isOnTypeDeclarationIdentifier) parent as SwiftTypeDeclaration else null
}

internal fun PsiElement.isInSwiftFile(): Boolean {
    val fileType = (containingFile?.fileType as? LanguageFileType) ?: return false
    return fileType.language.id.lowercase() == "swift"
}