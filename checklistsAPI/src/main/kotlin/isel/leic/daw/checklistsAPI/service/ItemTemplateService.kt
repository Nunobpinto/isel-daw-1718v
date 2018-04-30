package isel.leic.daw.checklistsAPI.service

import isel.leic.daw.checklistsAPI.model.ChecklistTemplate
import isel.leic.daw.checklistsAPI.model.ItemTemplate

interface ItemTemplateService {

    fun getItemsByTemplate(checklistTemplate: ChecklistTemplate): List<ItemTemplate>

    fun getItemsByTemplatePaginated(checklistTemplate: ChecklistTemplate, offset:Int, limit:Int) : List<ItemTemplate>

    fun getItemTemplateByIdAndTemplate(checklistTemplate: ChecklistTemplate, itemId: Long): ItemTemplate

    fun saveItemTemplate(itemTemplate: ItemTemplate): ItemTemplate

    fun saveAllItemTemplates(itemTemplates: Iterable<ItemTemplate>): MutableIterable<ItemTemplate>

    fun deleteItemByIdAndTemplate(checklistTemplate: ChecklistTemplate, itemId: Long): Long

    fun deleteAllItemsByTemplate(checklistTemplate: ChecklistTemplate): Long

}