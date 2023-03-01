package com.mo7ammedtabasi.assignment_1

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mo7ammedtabasi.assignment_1.databinding.ActivityMainBinding
import com.mo7ammedtabasi.assignment_1.databinding.DialogAddContactsBinding

class MainActivity : AppCompatActivity(),OnLongClick{

    private lateinit var binding: ActivityMainBinding
    private val db = Firebase.firestore
    private lateinit var contactList: ArrayList<Contact>
    private lateinit var adapterContact: AdapterContact
    private lateinit var listener: OnLongClick

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listener = this
        contactList = ArrayList()
        showContactInRecyclerView()

        binding.fab.setOnClickListener {
            showAlertDialogButtonClicked()
        }

    }

    private fun setContactInRecyclerView(){
        adapterContact = AdapterContact(this@MainActivity,contactList,listener)
        binding.recyclerView.adapter = adapterContact
    }
    private fun showContactInRecyclerView() {

        db.collection("Contacts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.id
                    val name = document.get("uName").toString()
                    val phone = document.get("uPhone").toString()
                    val address = document.get("uAddress").toString()
                    val contact = Contact(id,name, phone, address)
                    contactList.add(contact)
                }
                setContactInRecyclerView()
            }
            .addOnFailureListener { exception ->
                Log.w("firestormTAG", "Error getting documents.", exception)
            }
    }

    private fun showAlertDialogButtonClicked() {
        val bindingDialog = DialogAddContactsBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)
        builder.setView(bindingDialog.root)
        val dialog = builder.create()
        dialog.show()

        bindingDialog.btnSave.setOnClickListener {
            val uName = bindingDialog.uName.text.toString()
            val uPhone = bindingDialog.uNumber.text.toString()
            val uAddress = bindingDialog.uAddress.text.toString()

            if (uName.isNotEmpty() && uPhone.isNotEmpty() && uAddress.isNotEmpty()) {

                val person = hashMapOf(
                    "uName" to uName,
                    "uPhone" to uPhone,
                    "uAddress" to uAddress
                )

                // TODO add in firebase
                db.collection("Contacts")
                    .add(person)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(this@MainActivity, "Save", Toast.LENGTH_SHORT).show()
                        Log.d(
                            "firestormTAG",
                            "DocumentSnapshot added with ID: ${documentReference.id}"
                        )
                        bindingDialog.uName.text?.clear()
                        bindingDialog.uNumber.text?.clear()
                        bindingDialog.uAddress.text?.clear()
                        dialog.cancel()
                        contactList.clear()
                        showContactInRecyclerView()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_SHORT).show()
                        Log.w("firestormTAG", "Error adding document", e)
                    }
            } else {
                Toast.makeText(this@MainActivity, "some failed is empty", Toast.LENGTH_SHORT).show()

            }

        }

    }

    override fun onClick(id: String) {
        val builder=AlertDialog.Builder(this)
        builder.setTitle("warning")
        builder.setMessage("Delete Contact")
        builder.setIcon(R.drawable.baseline_delete_24)
        builder.setPositiveButton("Yes"){ _: DialogInterface, _:Int->

            db.collection("Contacts")
                .document(id).delete().addOnCompleteListener {
                    contactList.clear()
                    showContactInRecyclerView()
                }
        }
        builder.setNegativeButton("No"){_:DialogInterface,_Int->

        }
        val dialog=builder.create()
        dialog.show()



    }


}