package tm.devcraft.myuni.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class FirebaseUtil {


    companion object {
        var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        var firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()


        fun getCurrentUid(): String? {
            return firebaseAuth.currentUser!!.uid
        }

        //get current user
        fun getCurrentUser(): FirebaseUser? {
            return firebaseAuth.currentUser
        }
    }

}