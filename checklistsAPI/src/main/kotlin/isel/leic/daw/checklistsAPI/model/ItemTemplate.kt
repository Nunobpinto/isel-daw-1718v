package isel.leic.daw.checklistsAPI.model

import java.util.*
import javax.persistence.*

@Entity
@Table(name="item_template")
class ItemTemplate (
    @Column(name = "item_template_name")
    val itemTemplateName : String,

    @Column(name = "item_template_description")
    val itemTemplateDescription : String,

    @Column(name = "item_template_state")
    val itemTemplateState: State = State.Uncompleted,

    @EmbeddedId
    val itemTemplateComposeKey: ItemTemplateComposeKey){
    private constructor():this(
            "",
            "",
            State.Uncompleted,
            ItemTemplateComposeKey(ChecklistTemplate("","")))
}

@Embeddable
class ItemTemplateComposeKey(
        @ManyToOne
        @JoinColumn(name="template_id")
        val template: ChecklistTemplate,

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name="item_template_id")
        val itemId:Long = -1){
    private constructor():this(
            ChecklistTemplate("","")
    )
}