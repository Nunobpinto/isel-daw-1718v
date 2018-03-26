package isel.leic.daw.checklistsAPI.model

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name="item")
data class Item(
        @Column(name="item_name")
        val itemName : String,

        @Column(name = "item_description")
        val itemDescription : String,

        @Column(name="state")
        val itemState: State,

        @EmbeddedId
        val itemComposeKey: ItemComposeKey
) : Serializable

@Embeddable
data class ItemComposeKey(
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        @Column(name="item_id")
        val itemId : Long,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "checklist_id")
        val checklist: Checklist
):Serializable