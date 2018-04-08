package isel.leic.daw.checklistsAPI.mappers

import isel.leic.daw.checklistsAPI.model.*
import isel.leic.daw.checklistsAPI.outputModel.single.*

class OutputMapper {
    fun toUserOutput(user: User) =
            UserOutputModel(
                    username = user.username,
                    familyName = user.familyName,
                    givenName = user.givenName,
                    email = user.email
            )

    fun toItemOutput(item: Item, checklistId: Long) =
            ItemOutputModel(
                    checklistId = checklistId,
                    itemId = item.itemId,
                    name = item.itemName,
                    description = item.itemDescription,
                    state = item.itemState.name
            )

    fun toChecklistOutput(checklist: Checklist, username: String) =
            ChecklistOutputModel(
                    checklistId = checklist.checklistId,
                    name = checklist.checklistName,
                    description = checklist.checklistDescription,
                    completionDate = checklist.completionDate.toString(),
                    username = username
            )

    fun toItemTemplateOutput(itemTemplate: ItemTemplate, checklistTemplateId: Long) =
            ItemTemplateOutputModel(
                    name = itemTemplate.itemTemplateName,
                    description = itemTemplate.itemTemplateDescription,
                    state = itemTemplate.itemTemplateState.toString(),
                    itemTemplateId = itemTemplate.itemTemplateId,
                    templateId = checklistTemplateId
            )

    fun toChecklistTemplateOutput(checklistTemplate: ChecklistTemplate, username: String) =
            ChecklistTemplateOutputModel(
                    templateId = checklistTemplate.checklistTemplateId,
                    name = checklistTemplate.checklistTemplateName,
                    description = checklistTemplate.checklistTemplateDescription,
                    username = username
            )
}