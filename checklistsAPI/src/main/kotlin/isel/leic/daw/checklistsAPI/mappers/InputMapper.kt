package isel.leic.daw.checklistsAPI.mappers

import isel.leic.daw.checklistsAPI.inputModel.single.*
import isel.leic.daw.checklistsAPI.model.*

class InputMapper {
    fun toUser(
            input: UserInputModel,
            checklists: MutableSet<Checklist> = mutableSetOf(),
            checklistTemplates: MutableSet<ChecklistTemplate> = mutableSetOf()
    ) =
            User(
                    sub = input.username,
                    checklists = checklists,
                    checklistTemplates = checklistTemplates
            )

    fun toItem(input: ItemInputModel, checklist: Checklist) =
            Item(
                    itemName = input.itemName,
                    itemId = input.itemId,
                    itemDescription = input.itemDescription,
                    itemState = State.valueOf(input.itemState),
                    checklist = checklist
            )

    fun toChecklist(
            input: ChecklistInputModel,
            user: User,
            items: MutableSet<Item> = mutableSetOf(),
            template: ChecklistTemplate? = null
    ) =
            Checklist(
                    checklistName = input.checklistName,
                    checklistDescription = input.checklistDescription,
                    checklistId = input.checklistId,
                    completionDate = input.completionDate,
                    items = items,
                    user = user,
                    template = template
            )

    fun toItemTemplate(input: ItemTemplateInputModel, template: ChecklistTemplate) =
            ItemTemplate(
                    itemTemplateName = input.itemTemplateName,
                    itemTemplateId = input.itemTemplateId,
                    itemTemplateDescription = input.itemTemplateDescription,
                    itemTemplateState = State.valueOf(input.itemTemplateState),
                    checklistTemplate = template
            )

    fun toChecklistTemplate(input: ChecklistTemplateInputModel, user: User, items: MutableSet<ItemTemplate>) =
            ChecklistTemplate(
                    checklistTemplateId = input.checklistTemplateId,
                    checklistTemplateName = input.checklistTemplateName,
                    checklistTemplateDescription = input.checklistTemplateDescription,
                    user = user
            )
}