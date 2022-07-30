package `in`.wizion.mjpru.admin.AdminDashboardActivities

import `in`.wizion.mjpru.R
import `in`.wizion.mjpru.admin.AdminDashboardActivities.modelClass.NoticeData
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_upload_notice.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class UploadNotice : AppCompatActivity() {

    //variable for pick image from gallery and fibase
    private val REQ = 1
    private var bitMap : Bitmap? = null
    private lateinit var storeReference : StorageReference
    private lateinit var reference: DatabaseReference
    private var downloadUrl : String = ""
    private lateinit var pd : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_notice)

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

        addImage.setOnClickListener {
            openGallery()
        }

        uploadNoticebtn.setOnClickListener {
            if (noticeTitle.text.toString().isEmpty()){
                noticeTitle.setError("Empty")
                noticeTitle.requestFocus()
            }else if(bitMap == null){
                Toast.makeText(this,"please select Image",Toast.LENGTH_SHORT).show()
            }else{
                uploadImage()
            }
        }
    }

    private fun uploadData() {

        val title = noticeTitle.text.toString()
        reference = reference.child("Notice")
        val uniqueKey = reference.push().key


        val calForDate : Calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("dd-MM-yy")
        val date = currentDate.format(calForDate.time)

        val calForTime = Calendar.getInstance()
        val currentTime = SimpleDateFormat("hh:mm a")
        val time = currentTime.format(calForTime.time)

        val noticeData = uniqueKey?.let { NoticeData(title,downloadUrl,date,time, it) }

        if (uniqueKey != null) {
            reference.child(uniqueKey).setValue(noticeData).addOnSuccessListener {
                pd.dismiss()
                Toast.makeText(this,"Notice Uploaded",Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                pd.dismiss()
                Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_SHORT).show()
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
        val filePath : StorageReference = storeReference.child("Notice").child("$finalImage"+"jpg")
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
                Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun openGallery() {
        val pickImage = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickImage,REQ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQ && resultCode == RESULT_OK){
            val uri = data?.data
            bitMap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
            noticeImageView.setImageBitmap(bitMap)
        }
    }
}