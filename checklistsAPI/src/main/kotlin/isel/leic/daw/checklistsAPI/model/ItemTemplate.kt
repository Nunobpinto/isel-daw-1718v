package isel.leic.daw.checklistsAPI.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name="item_template")
data class ItemTemplate (
    @Column(name = "item_template_name")
    val itemTemplateName : String,

    @Column(name = "item_template_description")
    val itemTemplateDescription : String,

    @Column(name = "item_template_state")
    val itemTemplateState: State ,

    @EmbeddedId
    val itemTemplateComposeKey: ItemTemplateComposeKey
) : Serializable

@Embeddable
data class ItemTemplateComposeKey(
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "checklist_template_id")
        val checklistTemplateId: ChecklistTemplate,

        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name="item_template_id")
        val itemTemplateId : Long
) : Serializable