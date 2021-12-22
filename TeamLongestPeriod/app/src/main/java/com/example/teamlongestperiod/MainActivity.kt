/**
 * Classname: Team Longest Period
 *
 * Version information: 1.0
 *
 * Date: 20.12.2021
 *
 * Copyright notice: Made by George Tasev
 */

package com.example.teamlongestperiod

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.*

class MainActivity : AppCompatActivity() {
    /**
     * We assign and initialize it later to it's rightful ID's from the XML design.
     */
    private lateinit var openButton: Button
    private lateinit var dataText: TextView //Reveals the data into text
    private lateinit var introText: TextView //Intro text that will be used for visibility

    /**
     * The Uri data will be used in order to open the text files from the device itself, instead of uploading it to Android Studio.
     */
    private lateinit var textUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        allPermissionsGranted()

        introText = findViewById(R.id.infoText)
        introText.visibility = View.VISIBLE

        dataText = findViewById(R.id.dataText)

        openButton = findViewById(R.id.openTextBtn)
        openButton.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "text/plain"
                startActivityForResult(intent, TEXT_SELECT_REQUEST)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, e.toString())
            }
        }
    }

    /**
     * This will check if the permissions within the app is granted, if not to ask the user to grant them.
     */
    private fun allPermissionsGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(PERMISSIONS, PERMISSION_READ_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TEXT_SELECT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    introText.visibility = View.GONE

                    textUri = data!!.data!!
                    var text = textUri.toString()

                    try {
                        //This will make an input data while an UI Interface for searching within the file explorer,
                            // using getContentResolver to open the file through inputStream and using the data Uri.
                        val inputStream: InputStream? = contentResolver.openInputStream(textUri)

                        //We make the size type Integer, because we want to make sure that the input stream data
                        // is available for putting it into the byte array.
                        val size: Int = inputStream!!.available()
                        val buffer = ByteArray(size)
                        inputStream.read(buffer)

                        //We use the buffer to read it on to the inputStream and them making the text variable to start
                        // reading from the buffer ByteArray.
                        text = String(buffer)
                        Log.d(TAG, text)

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    dataText.text = text
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e(TAG, e.toString())
                    Toast.makeText(this, "Failed to load text file.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * This object will make a list of const variables to make sure that this class is provided with
     * every single data that we can make and only within this class can have access to.
     */
    companion object {
        private const val PERMISSION_READ_CODE = 10
        private const val TEXT_SELECT_REQUEST = 2
        private val PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        private const val TAG = "TeamLongestPeriod"
    }
}



