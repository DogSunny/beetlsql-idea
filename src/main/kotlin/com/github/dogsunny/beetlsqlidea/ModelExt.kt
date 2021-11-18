package com.github.dogsunny.beetlsqlidea

import com.github.dogsunny.beetlsqlidea.data.*
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileSystemItem
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import org.intellij.plugins.markdown.lang.psi.impl.MarkdownHeaderImpl


private const val MARKDOWN_SQL_HEADER_TITLE = "==="
private const val RESOURCE_DIR = "resources"

fun PsiElement.module(): Module? {
    return ModuleUtil.findModuleForFile(containingFile.originalFile.virtualFile?:return null, project)
}

fun Module.findFilesByName(fileName: String, sqlFileType: SqlFileType): Array<out PsiFileSystemItem> {
    // aaa.xxx
    val fullName = sqlFileType.fullName(fileName)
    return FilenameIndex.getFilesByName(project, fullName, GlobalSearchScope.moduleScope(this), false)
}

fun Module.findVFilesByExt(type: SqlFileType): MutableCollection<VirtualFile> {
    return FilenameIndex.getAllFilesByExt(project, type.extension)
}

fun Module.findSqlFilesByExt(type: SqlFileType): List<FindSqlFileResult> {
    return findVFilesByExt(type)
        .filter { it.path.contains(RESOURCE_DIR) }
        .mapNotNull { PsiManager.getInstance(project).findFile(it) }
        .map { FindSqlFileResult(SqlFileId(it.name.removeSuffix("." + type.extension)), it, type) }
}
fun Module.findAllSqlFiles(): List<FindSqlFileResult> {
    // TODO 支持.sql
    return findSqlFilesByExt(SqlFileType.MARKDOWN)
}

fun Module.findSqlFile(sqlId: SqlId): FindSqlFileResult? {
    return findSqlFiles(sqlId).firstOrNull()
}

fun Module.findSqlFiles(sqlId: SqlId): List<FindSqlFileResult> {
    return findFilesByName(sqlId.fileName, SqlFileType.MARKDOWN).mapNotNull { FindSqlFileResult.fromPsiFile(sqlId, it.containingFile) }
}

fun Module.findSqlScripts(sqlId: SqlFileId): List<FindSqlScriptResult> {
    return findSqlScripts(findSqlFile(sqlId)?: return emptyList())
}

fun Module.findSqlScript(sqlScriptId: SqlScriptId): FindSqlScriptResult? {
    val (_, psiFile, sqlFileType) = findSqlFile(sqlScriptId)?: return null
    return when(sqlFileType) {
        SqlFileType.MARKDOWN -> findMarkdownSql(sqlScriptId, psiFile, sqlFileType)
        SqlFileType.SQL_FILE -> TODO("不支持")
    }
}

fun findSqlScripts(findSqlFileResult: FindSqlFileResult): List<FindSqlScriptResult> {
    val (sqlId, psiFile, sqlFileType) = findSqlFileResult
    val rootElement = psiFile.firstChild
    return (PsiTreeUtil.getChildrenOfType(rootElement, MarkdownHeaderImpl::class.java)?: emptyArray<PsiElement>())
        .mapNotNull { it.firstChild }
        .mapNotNull { it.firstChild } // leafElements
        .map { FindSqlScriptResult(SqlScriptId(sqlId.fileName, it.text), it, sqlFileType) }
}

fun findSqlScript(findSqlFileResult: FindSqlFileResult, scriptName: String): FindSqlScriptResult? {
    val (sqlId, psiFile, sqlFileType) = findSqlFileResult
    val sqlScriptId = SqlScriptId(sqlId.fileName, scriptName)
    return when(sqlFileType) {
        SqlFileType.MARKDOWN -> findMarkdownSql(sqlScriptId, psiFile, sqlFileType)
        SqlFileType.SQL_FILE -> TODO("不支持")
    }
}

private fun findMarkdownSql(sqlId: SqlScriptId, psiFile: PsiFile, sqlFileType: SqlFileType): FindSqlScriptResult? {
    val rootElement = PsiTreeUtil.firstChild(psiFile)
    val headerText = spliceHeader(sqlId.scriptName)
    val leafElement = PsiTreeUtil.getChildrenOfType(rootElement, MarkdownHeaderImpl::class.java)
        ?.firstOrNull { it.text == headerText }
        ?.firstChild
        ?.firstChild
        ?: return null

    return FindSqlScriptResult(sqlId, leafElement, sqlFileType)
}

private fun spliceHeader(scriptName: String) = scriptName + MARKDOWN_SQL_HEADER_TITLE