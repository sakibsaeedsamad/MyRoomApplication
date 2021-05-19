package com.sssakib.myapplication.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sssakib.myapplication.R
import com.sssakib.myapplication.model.User
import kotlinx.android.synthetic.main.list_user_recyclerview.view.*

class ListOfUserAdapter(val listener: RowClickListener) :
    RecyclerView.Adapter<ListOfUserAdapter.MyViewHolder>() {

    var items = ArrayList<User>()

    fun setListData(data: ArrayList<User>) {
        this.items = data
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListOfUserAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_user_recyclerview, parent, false)
        return MyViewHolder(inflater, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ListOfUserAdapter.MyViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            listener.onItemClickListener(items[position])
        }
        holder.bind(items[position])


    }


    class MyViewHolder(view: View, val listener: RowClickListener) : RecyclerView.ViewHolder(view) {


        val nameTextView = view.nameTextView
        val ageTextView = view.ageTextView
        val phoneTextView = view.phoneTextView
        val genderTextView = view.genderTextView
        val locationTextView = view.locationTextView
        val imageView = view.imageView


        fun bind(data: User) {
            nameTextView.text = "Name: " + data.name
            ageTextView.text = "Age: " + data.age
            phoneTextView.text = "Phone: " + data.phone
            genderTextView.text = "Gender: " + data.gender
            locationTextView.text = "Location: " + data.location
            imageView.setImageBitmap(convertStringToBitmap(data.image))


        }





        fun convertStringToBitmap(string: String?): Bitmap {
            val byteArray =
                Base64.decode(string, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }

    interface RowClickListener {
        fun onItemClickListener(user: User)
    }
}