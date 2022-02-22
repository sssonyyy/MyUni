package tm.devcraft.myuni.data

import com.google.firebase.firestore.FieldValue
import java.util.*

data class Receivers( var dialogs: MutableList<String> ?= mutableListOf())
data class Dialog(var dialogs : MutableList<Message>  ?= mutableListOf())
data class Message(val contentId: String ?= "", val from: String ?= "",  val createAt: Date ?= null, var isread : Boolean ?=false)
data class SendingMessage(val contentId: String ?= "", val from: String? = "", val createAt: FieldValue ?= FieldValue.serverTimestamp(), var isread : Boolean? =false)
data class Сщntent(val id: String, var text: String, var files: List<String>? = null)

data class Dialogs(var dialog: HashMap<String, Dialog>?)