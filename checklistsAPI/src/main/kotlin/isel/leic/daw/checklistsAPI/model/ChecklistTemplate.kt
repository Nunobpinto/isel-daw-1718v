package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModelProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "checklist_template")
data class ChecklistTemplate(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @ApiModelProperty("The database generated Template identifier")
        @Column(name = "checklist_template_id")
        val checklistTemplateId: Long = -1,

        @ApiModelProperty("Templates name")
        @Column(name = "checklist_template_name")
        val checklistTemplateName: String = "",

        @ApiModelProperty("Templates description")
        @Column(name = "checklist_template_description")
        val checklistTemplateDescription: String = "",

        @JsonIgnore
        @ApiModelProperty("Checklists that have used this Template")
        @OneToMany(mappedBy = "template")
        val checklists: MutableSet<Checklist> = mutableSetOf(),

        @JsonIgnore
        @ApiModelProperty("Items that belong to this Template")
        @OneToMany(mappedBy = "checklistTemplate", cascade = [(CascadeType.ALL)], orphanRemoval = true)
        val items: MutableSet<ItemTemplate> = mutableSetOf(),

        @JsonIgnore
        @ApiModelProperty("Username of the creator of this Template")
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "username", nullable = false)
        val user: User? = User()
) : Serializable {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                return if (other !is ChecklistTemplate) false else checklistTemplateId == other.checklistTemplateId
        }

        override fun hashCode(): Int = 32
}