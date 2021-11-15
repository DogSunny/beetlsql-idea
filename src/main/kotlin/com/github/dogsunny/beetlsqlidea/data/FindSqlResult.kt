package com.github.dogsunny.beetlsqlidea.data

import com.github.dogsunny.beetlsqlidea.enum.SqlTargetType
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile

interface FindSqlResult {
    val sqlId: SqlId
    val psiElement: PsiElement
    val resultType: SqlTargetType
    val sqlFileType: SqlFileType
}
data class FindSqlFileResult(
    override val sqlId: SqlId,
    override val psiElement: PsiFile,
    override val sqlFileType: SqlFileType
    ): FindSqlResult
{
    companion object {
        fun fromPsiFile(sqlFileId: SqlId, file: PsiFile): FindSqlFileResult? {
            val sqlFileType = SqlFileType.fromExtension(file.virtualFile.extension) ?: return null
            return FindSqlFileResult(sqlFileId, file, sqlFileType)
        }
    }
    override val resultType = SqlTargetType.FILE
}
data class FindSqlScriptResult(
    override val sqlId: SqlScriptId,
    override val psiElement: PsiElement,
    override val sqlFileType: SqlFileType
    ): FindSqlResult
{
    override val resultType = SqlTargetType.SCRIPT
}
enum class SqlFileType(val extension: String) {
    MARKDOWN("md"), SQL_FILE("sql");
    fun fullName(name: String) = "$name.$extension"
    companion object {
        fun fromExtension(extension: String?): SqlFileType? {
            extension?: return null
            return values().find { it.extension == extension }
        }
    }
}