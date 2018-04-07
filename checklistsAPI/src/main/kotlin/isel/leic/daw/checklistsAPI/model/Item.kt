package isel.leic.daw.checklistsAPI.model

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModelProperty
import org.apache.commons.lang3.builder.EqualsBuilder
import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "item")
data class Item(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @ApiModelProperty("The database generated Item Identifier")
        @Column(name = "item_id")
        val itemId: Long = -1,

        @ApiModelProperty("Items Name")
        @Column(name = "item_name")
        val itemName: String = "",

        @ApiModelProperty("Items Description")
        @Column(name = "item_description")
        val itemDescription: String = "",

        @ApiModelProperty("The State of the Item, Completed or Uncompleted")
        @Enumerated(EnumType.STRING)
        @Column(name = "state")
        val itemState: State = State.Uncompleted,

        @JsonIgnore
        @ApiModelProperty("The Identifier of the Checklist where the Item belongs")
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "checklist_id", nullable = false)
        val checklist: Checklist? = Checklist()
) : Serializable {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                return if (other !is Item) false else itemId == other.itemId
        }

        override fun hashCode(): Int = 31
}