package isel.leic.daw.checklistsAPI.model

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.io.Serializable


class MyCustomPageable(private val offset: Int, private val limit: Int, private val sort: Sort) : Pageable, Serializable {

    init {
        if (offset < 0) {
            throw IllegalArgumentException("Offset index must not be less than zero!")
        }

        if (limit < 1) {
            throw IllegalArgumentException("Limit must not be less than one!")
        }
    }

    override fun getPageNumber(): Int = offset / limit

    override fun getPageSize(): Int = limit

    override fun getOffset(): Long = offset.toLong()

    override fun getSort(): Sort = sort


    override fun next(): Pageable {
        return MyCustomPageable((getOffset() + pageSize).toInt(), pageSize, getSort())
    }

    private fun previous(): Pageable {
        return if (hasPrevious()) MyCustomPageable((getOffset() - pageSize).toInt(), pageSize, getSort()) else this
    }


    override fun previousOrFirst(): Pageable {
        return if (hasPrevious()) previous() else first()
    }

    override fun first(): Pageable {
        return MyCustomPageable(0, pageSize, getSort())
    }

    override fun hasPrevious(): Boolean {
        return offset > limit
    }
}
