package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "item_template")
data class ItemTemplate(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name = "item_template_id")
        val itemTemplateId: Long = -1,

        @Column(name = "item_template_name")
        val itemTemplateName: String? = null,

        @Column(name = "item_template_description")
        val itemTemplateDescription: String? = null,

        @Enumerated(EnumType.STRING)
        @Column(name = "item_template_state")
        val itemTemplateState: State = State.Uncompleted,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "checklist_template_id", nullable = false)
        val checklistTemplate: ChecklistTemplate? = null
) : Serializable {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                return if (other !is ItemTemplate) false else itemTemplateId == other.itemTemplateId
        }

        override fun hashCode(): Int = 30
}
