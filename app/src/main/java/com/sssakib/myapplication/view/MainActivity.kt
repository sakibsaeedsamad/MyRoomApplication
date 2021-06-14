package com.sssakib.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.sssakib.myapplication.R
import com.sssakib.myapplication.adapter.ListOfUserAdapter
import com.sssakib.myapplication.model.User
import com.sssakib.myapplication.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_dialog.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ListOfUserAdapter.OnRowClickListener {

    lateinit var listOfUserAdapter: ListOfUserAdapter
    lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        viewCustomerButton.setOnClickListener {

            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                listOfUserAdapter = ListOfUserAdapter(this@MainActivity)
                adapter = listOfUserAdapter
            }


            viewModel.getAllUsersObservers().observe(this, Observer {
                listOfUserAdapter.setListData(ArrayList(it))
                listOfUserAdapter.notifyDataSetChanged()
            })


        }


    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onUpdateClick(user: User) {
        val uId = user.id
        val uName = user.name
        val uAge = user.age
        val uPhone = user.phone
        val uGender = user.gender
        val uLocation = user.location
        val uImage = user.image


        val intent = Intent(this, UpdateActivity::class.java)
        intent.putExtra("id", uId)
        intent.putExtra("name", uName)
        intent.putExtra("age", uAge)
        intent.putExtra("phone", uPhone)
        intent.putExtra("gender", uGender)
        intent.putExtra("location", uLocation)
        intent.putExtra("image", uImage)
        startActivity(intent)

    }

    override fun onDeleteClick(user: User) {

        val dialog = MaterialDialog(this)
            .noAutoDismiss()
            .customView(R.layout.activity_dialog)

        dialog.userDeleteOkButton.setOnClickListener {
            viewModel.deleteUserInfo(user)
            dialog.dismiss()
        }
        dialog.show()
    }


}