package isel.leic.daw.checklistsAPI.mappers

import isel.leic.daw.checklistsAPI.inputModel.single.ChecklistInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ItemInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.ItemTemplateInputModel
import isel.leic.daw.checklistsAPI.inputModel.single.UserInputModel
import isel.leic.daw.checklistsAPI.model.*

class InputMapper {
    fun toUser(input: UserInputModel) =
            User(
                    username = input.username,
                    familyName = input.familyName,
                    givenName = input.givenName,
                    email = input.email,
                    password = input.password
            )

    fun toItem(input: ItemInputModel, checklist: Checklist) =
            Item(
                    itemName = input.itemName,
                    itemDescription = input.itemDescription,
                    itemState = State.valueOf(input.itemState),
                    checklist = checklist
            )

    fun toChecklist(input: ChecklistInputModel, user: User, template: ChecklistTemplate? = null) =
            Checklist(
                    checklistName = input.checklistName,
                    checklistDescription = input.checklistDescription,
                    checklistId = input.checklistId,
                    completionDate = input.completionDate,
                    user = user,
                    template = template
            )

    fun toItemTemplate(input: ItemTemplateInputModel, template: ChecklistTemplate) =
            ItemTemplate(
                    itemTemplateName = input.itemTemplateName,
                    itemTemplateDescription = input.itemTemplateDescription,
                    itemTemplateState = State.valueOf(input.itemTemplateState),
                    checklistTemplate = template
            )

    fun toChecklistTemplate(input: ChecklistTemplate, user: User) =
            ChecklistTemplate(
                    checklistTemplateName = input.checklistTemplateName,
                    checklistTemplateDescription = input.checklistTemplateDescription,
                    user = user
            )
}