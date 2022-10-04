package com.namasake.food.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.namasake.food.core.Constants.ALL_IMAGES
import com.namasake.food.data.network.RepoImpl
import com.namasake.food.databinding.ActivityAddFoodBinding
import com.namasake.food.domain.UseCase
import com.namasake.food.presentation.MainViewModel
import com.namasake.food.presentation.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_add_food.*
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class AddFood : AppCompatActivity() {
    private val viewModel by lazy { ViewModelProvider(this, MainViewModelFactory(UseCase(RepoImpl()))).get(MainViewModel::class.java)}
    private  lateinit var binding: ActivityAddFoodBinding
    lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = (10..50).random()

        btnAdd.setOnClickListener {
            uploadImage()
            if (binding.edtName.editText?.text?.isNotEmpty() == true && binding.edtdescript.editText?.text?.isNotEmpty() == true) {
                viewModel.addNewFood(
                    binding.edtName.editText!!.text.toString(),
                    binding.edtdescript.editText!!.text.toString(),
                    id.toString(),
                    imageUri.toString()
                )
                Intent(this,MainActivity::class.java).also {
                    startActivity(it)
                }
            }
            else{
                Toast.makeText(this, "Enter value", Toast.LENGTH_SHORT).show()
            }
        }

        btnChooseImage.setOnClickListener {
            chooseImage()
        }
    }
    fun chooseImage() {
        val intent = Intent()
        intent.type = ALL_IMAGES
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){
            imageUri = data?.data!!
        }
    }

    fun uploadImage(){
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                println("image added")
            }.addOnFailureListener{
                println("something wrong")
            }


    }
}