package com.github.dogsunny.beetlsqlidea.contributor

import com.github.dogsunny.beetlsqlidea.*
import com.github.dogsunny.beetlsqlidea.data.FindSqlFileResult
import com.github.dogsunny.beetlsqlidea.data.FindSqlScriptResult
import com.github.dogsunny.beetlsqlidea.data.SqlFileId
import com.github.dogsunny.beetlsqlidea.icons.MyIcons
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.impl.source.tree.JavaElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.util.ProcessingContext

class InvokerRefSql : PsiReferenceContributor() {
    companion object {
        const val magic = "IntellijIdeaRulezzz "
    }

    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(PsiLiteralExpression::class.java),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference>
                = getReferences(element)
            }
        )
    }

    fun getReferences(
        element: PsiElement
    ): Array<PsiReference> {
        val literalExpression = element as PsiLiteralExpression
        val (fileName, scriptName, scriptRange) = literalExpression.getFileScriptName()?: return PsiReference.EMPTY_ARRAY
        val fileNameRange = TextRange(1, fileName.length + 1)
        return arrayOf<PsiReference>(FileReference(element, fileNameRange, fileName)).run {
            scriptRange?:return this
            scriptName?:return this
            plusElement(ScriptReference(element, scriptRange, fileName, scriptName))
        }
    }

    private fun PsiLiteralExpression.getFileScriptName(): Triple<String, String?, TextRange?>? {
        value?: return null
        if (value !is String) return null
        val stringValue = value as String
        val psiExpressionList = parent
        val methodName = PsiTreeUtil.getPrevSiblingOfType(psiExpressionList, PsiReferenceExpression::class.java)?.text?:return null

        fun parseSqlId():Triple<String, String?, TextRange?>? {
            if (value !is String) return null
            val realSqlId = stringValue.replace(magic, "")
            val split = realSqlId.split(".")
            if (split.size > 2) return null
            val fileName = split[0]
            val scriptName = split.getOrNull(1)
            val scriptRange = scriptName?.let { TextRange(fileName.length + 2, (fileName + it).length + 2) }
            return Triple(fileName, scriptName, scriptRange)
        }

        return if (methodName == "SqlId.of") {
            val literals= PsiTreeUtil.getChildrenOfType(psiExpressionList, PsiLiteralExpression::class.java)?: return null
            val first = literals[0]
            val second = literals.getOrNull(1)
            if (second != null && second == this) Triple(first.value as String, stringValue, TextRange(1, stringValue.length + 1))
            else parseSqlId()
        } else if (
            methodName.endsWith(".select") ||
            methodName.endsWith(".update") ||
            methodName.endsWith(".insert") ||
            methodName.endsWith(".getScript")
        ) {
            // 判断是否为第一个参数
            val prevToken = PsiTreeUtil.getPrevSiblingOfType(this, PsiJavaToken::class.java)
            if (prevToken.elementType == JavaTokenType.LPARENTH) parseSqlId() else return null
        }
        else null
    }

    class FileReference(element: PsiElement, textRange: TextRange, private val fileName: String)
        : PsiReferenceBase<PsiElement?>(element, textRange), PsiPolyVariantReference
    {
        // 多解析
        override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
            return find(true).map { PsiElementResolveResult(it.psiElement) }.toTypedArray()
        }

        override fun resolve(): PsiElement? {
            val resolveResults = multiResolve(false)
            return if (resolveResults.size == 1) resolveResults[0].element else null
        }

        // 基本代码完成列表 C:\Users\wgyag\IdeaProjects\github\beetlsql-idea\src\main\resources\messages\resources\MyBundle.properties
        override fun getVariants(): Array<LookupElementBuilder> {
            return find(false)
                .map {
                    LookupElementBuilder
                        .create(it.sqlId.fileName)
                        .withIcon(MyIcons.icon)
                        .withTypeText(it.psiElement.originalFile.virtualFile.path.replaceFirst(Regex(".+?resources/"), ""))
                }.toTypedArray()
        }

        private fun find(strict: Boolean): List<FindSqlFileResult> {
            val module = myElement!!.module()?: return emptyList()
            return module.findAllSqlFiles()
                .filter { it.sqlId.fileName.run { if (strict) equals(fileName) else contains(fileName) } }
        }
    }


    class ScriptReference(element: PsiElement, textRange: TextRange, private val fileName: String, private val scriptName: String)
        : PsiReferenceBase<PsiElement?>(element, textRange), PsiPolyVariantReference
    {
        // 多解析
        override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
            return find(true)
                .map { PsiElementResolveResult(it.psiElement) }
                .toTypedArray()
        }

        override fun resolve(): PsiElement? {
            val resolveResults = multiResolve(false)
            return if (resolveResults.size == 1) resolveResults[0].element else null
        }

        // 基本代码完成列表
        override fun getVariants(): Array<LookupElementBuilder> {
            return find(false)
                //.map { it.psiElement.containingFile }
                .map { LookupElementBuilder.create(it.sqlId.scriptName).withIcon(MyIcons.icon).withTypeText(it.psiElement.containingFile.name) }
                .toTypedArray()
        }

        private fun find(strict: Boolean): List<FindSqlScriptResult> {
            val module = myElement!!.module()?: return emptyList()
            return module.findSqlFiles(SqlFileId(fileName))
                .flatMap { findSqlScripts(it) }
                .filter { it.sqlId.scriptName.run { if (strict) equals(scriptName) else contains(scriptName) } }
        }
    }
}