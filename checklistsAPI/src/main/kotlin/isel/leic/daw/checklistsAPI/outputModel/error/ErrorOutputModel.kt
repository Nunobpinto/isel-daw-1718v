package isel.leic.daw.checklistsAPI.outputModel.error

data class ErrorOutputModel(
        val title : String,
        val detail : String,
        val status : Int
)