package isel.leic.daw.checklistsAPI.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "checklist_template")
data class ChecklistTemplate(
        @Column(name = "checklist_template_name")
        val checklisttemplateName: String,

        @Column(name = "checklist_template_description")
        val checklisttemplateDescription: String,

        @OneToMany(mappedBy = "checklistId")
        val checklists:MutableSet<Checklist>,

        @OneToMany(mappedBy = "itemTemplateComposeKey.checklistTemplateId")
        val  items:MutableSet<ItemTemplate>,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "username")
        val user: User,

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name = "checklist_template_id")
        val checklisttemplateID: Long
) : Serializable