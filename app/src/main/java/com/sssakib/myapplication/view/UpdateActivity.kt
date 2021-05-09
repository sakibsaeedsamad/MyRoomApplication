package com.sssakib.myapplication.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.sssakib.myapplication.R
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_update.*
import kotlinx.android.synthetic.main.activity_update.locationUpdateSpinner
import java.io.ByteArrayOutputStream

class UpdateActivity : AppCompatActivity() {

    val RequestPermissionCode = 1


    var uId = 0
    var uName: String? = null
    var uPhone: String? = null
    var uGender: String? = null
    var uLocation: String? = null
    var uImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        EnableRuntimePermission()

        val intent = intent
        uId=intent.extras!!.getInt("id")
        uName = intent.extras!!.getString("name")
        uPhone = intent.extras!!.getString("phone")
        uGender = intent.extras!!.getString("gender")
        uLocation = intent.extras!!.getString("location")
        uImage = intent.extras!!.getString("image")

        nameUpdateET.setText(uName)
        phoneUpdateET.setText(uPhone)
        profileUpdateImageView.setImageBitmap(convertStringToBitmap(uImage))



        updateImageButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 7)
        })

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 7 && resultCode == Activity.RESULT_OK) {
            val captureImage = data!!.extras!!["data"] as Bitmap?
            profileUpdateImageView!!.setImageBitmap(captureImage)
            uImage = captureImage?.let { convertBitmapToString(it) }
        }
    }

    fun EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@UpdateActivity,
                Manifest.permission.CAMERA
            )
        ) {
            Toast.makeText(
                this@UpdateActivity,
                "CAMERA permission allows us to Access CAMERA app",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this@UpdateActivity, arrayOf(
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
                    this@UpdateActivity,
                    "Permission Granted, Now your application can access CAMERA.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@UpdateActivity,
                    "Permission Canceled, Now your application cannot access CAMERA.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun convertStringToBitmap(string: String?): Bitmap {
        val byteArray =
            Base64.decode(string, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    }
    fun convertBitmapToString(bitmap: Bitmap): String? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


}