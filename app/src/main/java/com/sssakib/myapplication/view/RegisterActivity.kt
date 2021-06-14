package com.sssakib.myapplication.view

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.sssakib.myapplication.R
import com.sssakib.myapplication.model.User
import com.sssakib.myapplication.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream
import java.util.*


class RegisterActivity : AppCompatActivity() {

    lateinit var name: String
    lateinit var phone: String
    var tempAge = 0
    lateinit var age: String
    lateinit var genderString: String
    lateinit var locationString: String
    lateinit var genderRadioButton: RadioButton
    lateinit var viewModel: UserViewModel


    var imageResult: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_YEAR)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        //Initially set Male Radiobutton and genderString
        maleRadioButton.isChecked = true
        genderString = "Male"


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

            requestCameraPermission()

        })
        uploadImageButton.setOnClickListener {
            requestStoragePermission()

        }
        bdatePickBTN.setOnClickListener {
            val dpkr = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { datePicker: DatePicker, mYear: Int, mMonth: Int, mDay: Int ->

                    tempAge = (year - mYear)
                    age = tempAge.toString()
                    ageTV.setText("Your age is: " + age)

                },
                year,
                month,
                day
            )

            dpkr.show()

        }

        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener() { radioGroup: RadioGroup, checkedId: Int ->

            when (checkedId) {
                R.id.maleRadioButton -> genderString = "Male"
                R.id.femaleRadioButton -> genderString = "Female"
            }
        });


    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
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

    private fun setUser() {
        name = nameRegistrationET.getText().toString().trim()
        phone = phoneRegistrationET.getText().toString().trim()
        genderString
        var isAgree = agreeCheckbox.isChecked

        if (name.length <= 2) {
            Toast.makeText(
                this,
                "Please enter your name.",
                Toast.LENGTH_LONG
            ).show()
        }
        if (phone.length < 11 || phone.length > 11) {
            Toast.makeText(
                this,
                "Please give 11 digit number...",
                Toast.LENGTH_LONG
            ).show()
        }
        if (genderString.isEmpty()) {
            Toast.makeText(
                this,
                "Please select gender",
                Toast.LENGTH_LONG
            ).show()
        }
//        if (maleRadioButton.isChecked == false && femaleRadioButton.isChecked == false) {
//            Toast.makeText(
//                this,
//                "Please select gender",
//                Toast.LENGTH_LONG
//            ).show()
//        }

        if (imageResult.isNullOrEmpty()) {
            Toast.makeText(
                this,
                "Please take or select a photo",
                Toast.LENGTH_LONG
            ).show()
        }

        if (tempAge == 0 || tempAge <= 17) {

            Toast.makeText(
                this,
                "Under aged user!",
                Toast.LENGTH_LONG
            ).show()
        }
        if (!isAgree) {

            Toast.makeText(
                this,
                "Please agree to register!",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val user = User(0, name, age, phone, genderString, locationString, imageResult)
            viewModel.insertUserInfo(user)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            Toast.makeText(
                this,
                "User Saved!",
                Toast.LENGTH_LONG
            ).show()

        }


    }


    private fun requestStoragePermission() {
        Dexter.withActivity(this)
            .withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    // permission is granted
                    openGallary()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    // check for permanent denial of permission
                    if (response.isPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun requestCameraPermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    // permission is granted
                    openCamera()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    // check for permanent denial of permission
                    if (response.isPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@RegisterActivity)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton(
            "GOTO SETTINGS",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
                openSettings()
            })
        builder.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 7)
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

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val captureImage = data?.data
            val mBitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(this.contentResolver, captureImage)
            profileImageView!!.setImageBitmap(mBitmap)
            imageResult = captureImage?.let { convertBitmapToString(mBitmap) }
        }
    }

    private fun openGallary() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100)
    }

    fun convertBitmapToString(bitmap: Bitmap): String? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


}