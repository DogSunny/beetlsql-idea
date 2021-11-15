package com.github.dogsunny.beetlsqlidea.contributor

import com.github.dogsunny.beetlsqlidea.*
import com.github.dogsunny.beetlsqlidea.data.FindSqlScriptResult
import com.github.dogsunny.beetlsqlidea.data.SqlFileId
import com.github.dogsunny.beetlsqlidea.icons.MyIcons
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext

class InvokerRefSql : PsiReferenceContributor() {
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
        // 第一种情况 SqlId.of("calculate.queryBjContractSzByDateJD1") SqlId.of("calculate","queryBjContractSzByDateJD1")
        // TODO 第二种情况 sqlServerManager.select("calculate.queryBjContractSzByDateJD1", BjContractSz.class, params)
        val literalExpression = element as PsiLiteralExpression
        // ("aaa.bbb")
        val expressionList = literalExpression.parent
        val methodName = PsiTreeUtil.getPrevSiblingOfType(expressionList, PsiReferenceExpression::class.java)
        if (methodName?.text != "SqlId.of") return PsiReference.EMPTY_ARRAY
        val value: String = (literalExpression.value.takeIf { it is String } as String?) ?: return PsiReference.EMPTY_ARRAY

        val (fileName, scriptName) = (value.split(".").takeIf { it.size <= 2 } ?: return PsiReference.EMPTY_ARRAY)
            .run { Pair(get(0), getOrNull(1)) }

        val fileNameRange = TextRange(1, fileName.length + 1)
        return arrayOf<PsiReference>(FileReference(element, fileNameRange, fileName)).run {
            val textRange = TextRange(fileName.length + 1, value.length + 1)
            if (scriptName == null) this else plusElement(ScriptReference(element, textRange, fileName, scriptName))
        }
    }

    class FileReference(element: PsiElement, textRange: TextRange, private val fileName: String)
        : PsiReferenceBase<PsiElement?>(element, textRange), PsiPolyVariantReference
    {
        // 多解析
        override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
            return find().map { PsiElementResolveResult(it) }.toTypedArray()
        }

        override fun resolve(): PsiElement? {
            val resolveResults = multiResolve(false)
            return if (resolveResults.size == 1) resolveResults[0].element else null
        }

        // 基本代码完成列表
        override fun getVariants(): Array<LookupElementBuilder> {
            return find()
                .map { LookupElementBuilder.create(it).withIcon(MyIcons.icon).withTypeText(it.name) }
                .toTypedArray()
        }

        private fun find(): List<PsiFile> {
            val module = myElement!!.module()?: return emptyList()
            return module.findAllSqlFiles()
                .map { it.psiElement }
                .filter { it.name.contains(fileName) }
        }
    }


    class ScriptReference(element: PsiElement, textRange: TextRange, private val fileName: String, private val scriptName: String)
        : PsiReferenceBase<PsiElement?>(element, textRange), PsiPolyVariantReference
    {
        // 多解析
        override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
            return find()
                .map { PsiElementResolveResult(it.psiElement) }
                .toTypedArray()
        }

        override fun resolve(): PsiElement? {
            val resolveResults = multiResolve(false)
            return if (resolveResults.size == 1) resolveResults[0].element else null
        }

        // 基本代码完成列表
        override fun getVariants(): Array<LookupElementBuilder> {
            return find()
                .map { it.psiElement.containingFile }
                .map { LookupElementBuilder.create(it).withIcon(MyIcons.icon).withTypeText(it.name) }
                .toTypedArray()
        }

        private fun find(): List<FindSqlScriptResult> {
            val module = myElement!!.module()?: return emptyList()
            return module.findSqlFiles(SqlFileId(fileName))
                .flatMap { findSqlScripts(it).apply { println(size) } }
                .filter { it.sqlId.scriptName.contains(scriptName).apply { println(this) } }
        }
    }
}