package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModelProperty
import java.io.Serializable
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "checklist")
data class Checklist(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @ApiModelProperty(notes = "The database generated Checklist identifier")
        @Column(name = "checklist_id")
        val checklistId: Long = -1,

        @ApiModelProperty(notes = "Checklists name")
        @Column(name = "checklist_name")
        val checklistName: String = "",

        @ApiModelProperty("Checklists description")
        @Column(name = "checklist_description")
        val checklistDescription: String = "",

        @ApiModelProperty("Checklists Completion Date")
        @Column(name = "checklist_completion_date")
        val completionDate: LocalDate = LocalDate.MAX,

        @JsonIgnore
        @ApiModelProperty("The Template used to create this Checklist")
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "checklist_template_id", nullable = true)
        var template: ChecklistTemplate? = ChecklistTemplate(),

        @JsonIgnore
        @ApiModelProperty("Username of the owner and creator of this Checklist")
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "sub", nullable = false)
        val user: User? = User(),

        @JsonIgnore
        @ApiModelProperty("Items that belong to this Checklist")
        @OneToMany(mappedBy = "checklist", fetch = FetchType.LAZY, cascade = [(CascadeType.ALL)], orphanRemoval = true)
        val items: MutableSet<Item> = mutableSetOf()
) : Serializable {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                return if (other !is Checklist) false else checklistId == other.checklistId
        }

        override fun hashCode(): Int = 33
}