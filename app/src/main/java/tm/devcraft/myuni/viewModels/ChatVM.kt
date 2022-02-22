package tm.devcraft.myuni.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import tm.devcraft.myuni.data.*
import tm.devcraft.myuni.utils.FirebaseUtil
import java.util.*
import kotlin.collections.HashMap

class ChatVM(): ViewModel() {
    //LiveData
    var listPossibleDialogData: MutableLiveData<MutableList<userData>> = MutableLiveData(
        mutableListOf())
    var existingDialogsData: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    var dialogData: MutableLiveData<Dialogs> = MutableLiveData(Dialogs(HashMap()))
    var receivers: MutableLiveData<HashMap<String,userData>> = MutableLiveData(hashMapOf())

    //
    lateinit var userData: userData
    var users = FirebaseUtil.firebaseFirestore.collection("users")
    var messages = FirebaseUtil.firebaseFirestore.collection("messages")
    var uid : String = FirebaseUtil.getCurrentUid().toString()

    var dialogsCurrentUser =
        FirebaseUtil.getCurrentUid()
            ?.let { FirebaseUtil.firebaseFirestore.collection("messages").document(it)}

    var fb = FirebaseUtil.firebaseAuth

    //listeners
    private lateinit var existingDialosIdsListener : ListenerRegistration
    fun removeExistiingDialogsIdsListener(){
        existingDialosIdsListener.remove()
        println("Listener EXISTIDS removed")
    }

    private lateinit var existingDialogsListener: ListenerRegistration
    fun removeExistingDialogsListener(){
        existingDialogsListener.remove()
        println("existingDialogsListener removed")
    }

    private lateinit var userdataOfExistingDialogsListener : ListenerRegistration
    fun removeUserOfExistingDialogsListener(){
        userdataOfExistingDialogsListener.remove()
        println("userdataOfExistingDialogsListener removed")
    }

    //Set listeners

    fun listenerExistingDialogs(){
        existingDialogsData.value?.forEach {
           dialogData?.value?.dialog?.put(it, Dialog())
            //println("UUUUA ${dialogData.value?.dialog?.get(it)}")
           // dialogData.value = dialogData.value
            existingDialogsListener =
                dialogsCurrentUser?.collection(it)?.addSnapshotListener{ snapshot, e ->
                    if(e!=null){
                        return@addSnapshotListener
                    }
                    if(snapshot!=null){
                        for(dc in snapshot.documentChanges){
                            when(dc.type){
                                DocumentChange.Type.ADDED -> {
                                    println("MESSAGES ${dc.document.data}")
                                    var id = dc.document.reference.parent.id
                                    dialogData?.value?.dialog?.get(id)?.dialogs?.add(dc.document.toObject(
                                        Message::class.java))
                                    dialogData?.value = dialogData?.value
                                    println("DIALOG ${dialogData?.value}")
                                }
                                DocumentChange.Type.MODIFIED -> {
                                    println("MODIFIED MESSAGE ${dc.document.data} ${dc.document.id}")
                                }
                            }
                        }
                    }
                }!!
        }
    }

    fun listenerExistingDialogsIds(){
        FirebaseUtil.getCurrentUid()?.let {
            var list = mutableListOf<Dialog>()
            existingDialosIdsListener = dialogsCurrentUser?.addSnapshotListener { snapshot, e ->
                if(e!=null){
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    var doc = snapshot.toObject(Receivers::class.java)
                    //println(doc?.dialogs)
                    var newDialogs = doc?.dialogs?.filter { !existingDialogsData.value?.contains(it)!! }
                    newDialogs?.forEach {
                        fetchUserOfExistingDialogs(it)
                    }
                    existingDialogsData.postValue(doc?.dialogs)
                }
            }!!

        }
    }



    fun sendMessage(receiver: String, message: SendingMessage){
        println(message)

        var idMessage =  UUID.randomUUID().toString().replace("-","")

        messages.document(uid).collection(receiver).document(idMessage).set(message).addOnCompleteListener {
            if(it.isSuccessful){
                messages.document(receiver).collection(uid).document(idMessage).set(message).addOnCompleteListener{
                    if(it.isSuccessful){
                        println(it.result)
                        println("URA")
                    }
                }
            }
            else{
                println(it.exception)
            }
        }

        //var idMessage =  FirebaseUtil.firebaseFirestore.collection("messages").document(uid).collection(receiver).document().id.toString()
        /*FirebaseUtil.firebaseFirestore.collection("messages").document(uid).collection(receiver).document(idMessage).set(message).addOnSuccessListener {
            println("Successful sending")
            /*FirebaseUtil.firebaseFirestore.collection("messages").document(receiver).collection(uid).document(idMessage).set(message).addOnSuccessListener {
            }.addOnFailureListener {
                   // fromCopy.delete()
                    println(it.localizedMessage+"!!!!!!!!!!!!!!!!!!!!!!!")
                }*/
        }
            .addOnFailureListener {
                println("Error sending")
            }*/
    }

    fun fetchUserOfExistingDialogs( uid: String){
        users.document(uid).get().addOnSuccessListener { doc ->
            val mDoc = doc.toObject(userData::class.java)
            if (mDoc != null) {
                receivers.value?.put(mDoc.uid,mDoc)
                receivers.postValue(receivers.value)
            }
        }
    }

    /* fun loadExistingDialogs(){

         FirebaseUtil.getCurrentUid()?.let {
             dialogsCurrentUser?.addOnSuccessListener { doc ->
                 if (doc != null) {
                     println(doc)
                     println(doc.toObject<Dialogs>())
                 }
             }

         }
     } */


    fun loadPossibleData(){
        listPossibleDialogData.value?.clear()
        var peoplePossibleDialogs : ArrayList<String> = arrayListOf()
        for((index, people) in userData.savedPeople.asReversed().withIndex()){
            if(index <= 3){
                peoplePossibleDialogs.add(people)
            }
        }
        val list = listPossibleDialogData.value

        var single = Single.create<List<userData>> {emmiter ->
            var listener = users.addSnapshotListener{ snapshot,e ->
                if(e!=null){
                    emmiter.onError(e)
                    return@addSnapshotListener
                }
                if(snapshot != null){
                    val docObjs = snapshot.toObjects(tm.devcraft.myuni.data.userData::class.java)
                    emmiter.onSuccess(docObjs)
                }

            }
            emmiter.setCancellable {
                listener.remove()
                println("EMMITER CANCEL")
            }
        }   .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.filter { peoplePossibleDialogs.contains(it.uid) && !existingDialogsData.value?.contains(it.uid)!!  && uid != it.uid} }
            .subscribe(object: SingleObserver<List<userData>>{
                override fun onSubscribe(d: Disposable) {

                }

                override fun onSuccess(t: List<userData>) {
                    listPossibleDialogData.postValue(t as MutableList<userData>?)
                   println("POSSIBLE DIALOGS ${listPossibleDialogData.value}")
                }

                override fun onError(e: Throwable) {

                }

            }
            )

        /*var  obs : @NonNull Disposable = Observable.create<userData> {
            var emmiter = it
            peoplePossibleDialogs.forEach {
                db.collection("users").document(it).get().addOnSuccessListener { document ->
                    if (document != null) {

                        if (list != null) {
                            emmiter.onNext(document.toObject<userData>()!!)
                            //emmiter.onComplete()
                            //list.add(document.toObject<userData>()!!)
                        }
                        //listPossibleDialog.value = list
                    } else {

                    }
                }
                    .addOnFailureListener { exception ->

                    }
            }
        }.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (list != null) {
                    println("GREEEEEEEEEEEEE" + it)
                    list.add(it)
                    listPossibleDialog.value = list
                }
            },{},
                {
                    println(""+list)
                    listPossibleDialog.value = list
                })*/


        //fbLoadData(peoplePossibleDialogs)

    }

    override fun onCleared() {
        super.onCleared()
    }
}