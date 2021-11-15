package com.github.dogsunny.beetlsqlidea.spring.line.mark

import com.github.dogsunny.beetlsqlidea.data.SqlId
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.psi.PsiElement

typealias SqlIdLeaf = Pair<SqlId, PsiElement>

/** 暂时不实现，项目中没有用到 **/
class MapperMarkToSql : LineMarkerProviderDescriptor() {
    companion object {
        const val BASE_MAPPER_NAME = "BaseMapper"
        const val DEFAULT_SUFFIX = "Dao"
        const val TEXT = "Go to Sql"
    }

    override fun getName(): String = TEXT

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null

/*    override fun collectSlowLineMarkers(
        elements: MutableList<out PsiElement>,
        result: MutableCollection<in LineMarkerInfo<*>>
    ) {
        elements.flatMap { it.toSqlFileData() }.map { TODO() }.forEach { result.add(it) }
    }

    private fun PsiElement.toSqlFileData(): List<SqlIdLeaf> {
        // 仅解析psiClass
        if (this !is PsiClass) return emptyList()


    }*/

/*    override fun collectSlowLineMarkers(
        elements: MutableList<out PsiElement>,
        result: MutableCollection<in LineMarkerInfo<*>>
    ) {

        elements.find {  }
        if (element !is USimpleNameReferenceExpression) return null
        if (element.identifier != BASE_MAPPER_NAME) return null
        val classElement = element.getUastParentOfType<UClass>()
            ?.takeIf { it.isInterface && it.qualifiedName?.endsWith(DEFAULT_SUFFIX)?:false }
            ?: return null
        return NavigationGutterIconBuilder.create(MyIcons.icon)
            .setTarget(getTarget())
            .setTooltipText("Go to Sql.")
            .createLineMarkerInfo(element)
    }

    fun findClass() {
        Uas
    }

    fun findMethods() {

    }*/


    /* override fun getLineMarkerInfo(element: PsiElement): RelatedItemLineMarkerInfo<*>? {

     }*/
    /*fun getTarget(): PsiElement {
    }*/
}