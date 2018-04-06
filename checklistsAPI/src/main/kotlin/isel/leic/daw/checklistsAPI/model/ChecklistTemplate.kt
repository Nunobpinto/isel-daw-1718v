package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "checklist_template")
data class ChecklistTemplate(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name = "checklist_template_id")
        val checklistTemplateId: Long = -1,

        @Column(name = "checklist_template_name")
        val checklistTemplateName: String = "",

        @Column(name = "checklist_template_description")
        val checklistTemplateDescription: String = "",

        @JsonIgnore
        @OneToMany(mappedBy = "template")
        val checklists: MutableSet<Checklist> = mutableSetOf(),

        @JsonIgnore
        @OneToMany(mappedBy = "checklistTemplate", cascade = [(CascadeType.ALL)], orphanRemoval = true)
        val items: MutableSet<ItemTemplate> = mutableSetOf(),

        @JsonIgnore
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