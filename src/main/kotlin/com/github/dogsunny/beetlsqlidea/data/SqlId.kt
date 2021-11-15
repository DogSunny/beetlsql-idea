package com.github.dogsunny.beetlsqlidea.data

import com.github.dogsunny.beetlsqlidea.enum.SqlTargetType

interface SqlId {
    val fileName: String
    val scriptName: String
    val type: SqlTargetType
}
data class SqlFileId(override val fileName: String) : SqlId {
    override val scriptName: String = "NONE"
    override val type: SqlTargetType = SqlTargetType.FILE
}
data class SqlScriptId(override val fileName: String, override val scriptName: String) : SqlId {
    override val type: SqlTargetType = SqlTargetType.SCRIPT
}
class SqlIdNone: SqlId{
    override val fileName: String = "NONE"
    override val scriptName: String = "NONE"
    override val type: SqlTargetType = SqlTargetType.NONE
}