package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.Api
import io.swagger.annotations.ApiModelProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "item_template")
data class ItemTemplate(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @ApiModelProperty("The database generated ItemTemplate Identifier")
        @Column(name = "item_template_id")
        val itemTemplateId: Long = -1,

        @ApiModelProperty("The ItemTemplate Name")
        @Column(name = "item_template_name")
        val itemTemplateName: String = "",

        @ApiModelProperty("The ItemTemplate Description")
        @Column(name = "item_template_description")
        val itemTemplateDescription: String = "",

        @ApiModelProperty("The State of the ItemTemplate, Completed or Uncompleted")
        @Enumerated(EnumType.STRING)
        @Column(name = "item_template_state")
        val itemTemplateState: State = State.Uncompleted,

        @JsonIgnore
        @ApiModelProperty("The Identifier of the Checklist where the ItemTemplate belongs")
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "checklist_template_id", nullable = false)
        val checklistTemplate: ChecklistTemplate? = ChecklistTemplate()
) : Serializable {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                return if (other !is ItemTemplate) false else itemTemplateId == other.itemTemplateId
        }

        override fun hashCode(): Int = 30
}
