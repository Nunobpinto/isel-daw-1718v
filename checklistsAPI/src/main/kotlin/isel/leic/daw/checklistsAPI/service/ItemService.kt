package isel.leic.daw.checklistsAPI.service

import isel.leic.daw.checklistsAPI.model.Checklist
import isel.leic.daw.checklistsAPI.model.Item

interface ItemService {

    fun getItemByChecklistAndItemId(checklist: Checklist, itemId: Long): Item

    fun getItemsByChecklist(checklist: Checklist): List<Item>

    fun getItemsByChecklistPaginated(checklist: Checklist, offset:Int, limit:Int): List<Item>

    fun saveItem(item: Item): Item

    fun deleteAllItemsByChecklist(checklist: Checklist): Long

    fun deleteItemByIdAndChecklist(checklist: Checklist, itemId: Long): Long

    fun saveAllItems(items: List<Item>): MutableIterable<Item>

}