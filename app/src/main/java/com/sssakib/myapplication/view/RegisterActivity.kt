package com.sssakib.myapplication.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.sssakib.myapplication.R
import com.sssakib.myapplication.model.User
import com.sssakib.myapplication.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.list_user_recyclerview.*
import java.io.ByteArrayOutputStream

class RegisterActivity : AppCompatActivity() {

    lateinit var name: String
    lateinit var phone: String
    lateinit var genderString: String
    lateinit var locationString: String
    lateinit var genderRadioButton: RadioButton
    lateinit var viewModel: UserViewModel


    var imageResult: String? = null
    val RequestPermissionCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        EnableRuntimePermission()
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)



//access the items of the list
        val location = resources.getStringArray(R.array.locationAarray)
//access the spinner
        if (locationSpinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, location)
            locationSpinner.adapter = adapter

            locationSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    locationString = location[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
        }




        submitButton.setOnClickListener(View.OnClickListener {
            setUser()
        })


        takeImageButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 7)
        })


    }

    private fun setUser() {
        name = nameRegistrationET.getText().toString().trim()
        phone = phoneRegistrationET.getText().toString().trim()

        //For RadioButton
        val selectedId = radioGroup.checkedRadioButtonId
        genderRadioButton = findViewById<View>(selectedId) as RadioButton
        genderString = genderRadioButton.getText().toString()

        var isAgree = agreeCheckbox.isChecked

        if (name.isEmpty() && name.length <= 2) {
            nameRegistrationET.error = "Name Required"
            nameRegistrationET.requestFocus()
        }
        if (phone.isEmpty() || (phone.length < 11 && phone.length >=11) ) {

            phoneRegistrationET.error = "Valid Phone Number Required"
            phoneRegistrationET.requestFocus()
        }
        if (genderString.isEmpty()) {
            Toast.makeText(
                this@RegisterActivity,
                "Please select gender",
                Toast.LENGTH_LONG
            ).show()
        }

        if (imageResult.isNullOrEmpty()) {
            Toast.makeText(
                this@RegisterActivity,
                "Please take a photo",
                Toast.LENGTH_LONG
            ).show()
        }
        if (!isAgree) {

            Toast.makeText(
                this@RegisterActivity,
                "Please agree to register!",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val user = User(0, name, phone, genderString, locationString,imageResult)
            viewModel.insertUserInfo(user)
            Toast.makeText(
                this@RegisterActivity,
                "User Saved!",
                Toast.LENGTH_LONG
            ).show()

        }


    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 7 && resultCode == Activity.RESULT_OK) {
            val captureImage = data!!.extras!!["data"] as Bitmap?
            profileImageView!!.setImageBitmap(captureImage)
            imageResult = captureImage?.let { convertBitmapToString(it) }
        }
    }

    fun EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@RegisterActivity,
                Manifest.permission.CAMERA
            )
        ) {
            Toast.makeText(
                this@RegisterActivity,
                "CAMERA permission allows us to Access CAMERA app",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this@RegisterActivity, arrayOf(
                    Manifest.permission.CAMERA
                ), RequestPermissionCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        RC: Int,
        per: Array<String>,
        PResult: IntArray
    ) {
        when (RC) {
            RequestPermissionCode -> if (PResult.size > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Permission Granted, Now your application can access CAMERA.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@RegisterActivity,
                    "Permission Canceled, Now your application cannot access CAMERA.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun convertBitmapToString(bitmap: Bitmap): String? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


}