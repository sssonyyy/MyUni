package tm.devcraft.myuni.dataSource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class FirebaseAuthSource {
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var firebaseFirestore: FirebaseFirestore? = FirebaseFirestore.getInstance()
    var firebaseStorage: FirebaseStorage? = FirebaseStorage.getInstance()

    fun getCurrentUid(): String? {
        return firebaseAuth!!.currentUser!!.uid
    }


    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth!!.currentUser
    }


}