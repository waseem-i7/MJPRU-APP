package `in`.wizion.mjpru.admin.AdminDashboardActivities

import `in`.wizion.mjpru.R
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_upload_image.*
import java.io.ByteArrayOutputStream

class UploadImage : AppCompatActivity() {


    //variable for pick image from gallery and fibase
    private var category : String = "Select Category"
    private val REQ = 1
    private var bitMap : Bitmap? = null
    private lateinit var storeReference : StorageReference
    private lateinit var reference: DatabaseReference
    private var downloadUrl : String = ""
    private lateinit var pd : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)

        //firebase
        reference = FirebaseDatabase.getInstance().reference
        storeReference= FirebaseStorage.getInstance().reference
        pd= ProgressDialog(this)

        //to remove status bar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        //to select image category
        val items  = arrayOf("Select Category","Convocation","Independence DaY","Other Events")
        image_category.adapter=ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items)
        image_category.onItemSelectedListener = object  : AdapterView.OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            /**
             * Callback method to be invoked when an item in this AdapterView has
             * been clicked.
             *
             *
             * Implementers can call getItemAtPosition(position) if they need
             * to access the data associated with the selected item.
             *
             * @param parent The AdapterView where the click happened.
             * @param view The view within the AdapterView that was clicked (this
             * will be a view provided by the adapter)
             * @param position The position of the view in the adapter.
             * @param id The row id of the item that was clicked.
             */
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            /**
             *
             * Callback method to be invoked when an item in this view has been
             * selected. This callback is invoked only when the newly selected
             * position is different from the previously selected position or if
             * there was no selected item.
             *
             * Implementers can call getItemAtPosition(position) if they need to access the
             * data associated with the selected item.
             *
             * @param parent The AdapterView where the selection happened
             * @param view The view within the AdapterView that was clicked
             * @param position The position of the view in the adapter
             * @param id The row id of the item that is selected
             */
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category=image_category.selectedItem.toString()
            }

            /**
             * Callback method to be invoked when the selection disappears from this
             * view. The selection can disappear for instance when touch is activated
             * or when the adapter becomes empty.
             *
             * @param parent The AdapterView that now contains no selected item.
             */
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        //to get image from gallery
        addGalleryImage.setOnClickListener {
            openGallery()
        }

        //btn to upload image
        uploadImagebtn.setOnClickListener {
            if (bitMap==null){
                Toast.makeText(this,"please upload Image",Toast.LENGTH_SHORT).show()
            }else if(category.equals("Select Category")){
                Toast.makeText(this,"please Select Image Category",Toast.LENGTH_SHORT).show()
            }else{
                uploadImage()
            }
        }

    }

    private fun uploadData() {

        reference = reference.child("Gallery").child(category)
        val uniqueKey = reference.push().key


        if (uniqueKey != null) {
            reference.child(uniqueKey).setValue(downloadUrl).addOnSuccessListener {
                pd.dismiss()
                Toast.makeText(this,"Image Uploaded", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                pd.dismiss()
                Toast.makeText(this,"Something Went Wrong", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun uploadImage() {

        pd.setMessage("Uploading...")
        pd.show()

        val  baos : ByteArrayOutputStream = ByteArrayOutputStream()
        bitMap?.compress(Bitmap.CompressFormat.JPEG,50,baos)
        val finalImage  : ByteArray = baos.toByteArray()

        //code for firebase to upload Image
        val filePath : StorageReference = storeReference.child("Gallery").child("$finalImage"+"jpg")
        val uploadTask = filePath.putBytes(finalImage)
        uploadTask.addOnCompleteListener(this, OnCompleteListener { task ->
            if (task.isSuccessful){
                uploadTask.addOnSuccessListener {takeSnapshot ->
                    filePath.downloadUrl.addOnSuccessListener { uri ->
                        downloadUrl = uri.toString()
                        uploadData()
                    }
                }
            }else{
                pd.dismiss()
                Toast.makeText(this,"Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun openGallery() {
        val pickImage = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickImage,REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQ && resultCode == RESULT_OK){
            val uri = data?.data
            bitMap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
            GalleryImageView.setImageBitmap(bitMap)
        }
    }
}