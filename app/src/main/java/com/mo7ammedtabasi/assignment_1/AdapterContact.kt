package com.mo7ammedtabasi.assignment_1

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mo7ammedtabasi.assignment_1.databinding.ItemContactBinding

class AdapterContact(private var context: Context,private var contactList:ArrayList<Contact>,val listener : OnLongClick)
    : RecyclerView.Adapter<AdapterContact.MyViewHolder>(){

    class MyViewHolder(private val itemBinding: ItemContactBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(contact: Contact) {
            itemBinding.tvName.text = contact.name
            itemBinding.tvPhone.text = contact.phone
            itemBinding.tvAddress.text = contact.address

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = contactList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val contact= contactList[position]
        holder.bind(contact)
        holder.itemView.setOnClickListener {
            listener.onClick(contact.id)
        }
    }
}