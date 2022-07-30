package com.unatxe.quicklist.domain.entities

import ExcludeFromJacocoGeneratedReport

data class QListItem(val id: Int = 0, val text: String, val checked: Boolean, val qList: QList){

    @ExcludeFromJacocoGeneratedReport
    override fun toString(): String {
        return """
            QListItem(
                id=$id,
                text='$text',
                checked=$checked,
                listId={$qList.id}
            )
        """.trimIndent()
    }
}
