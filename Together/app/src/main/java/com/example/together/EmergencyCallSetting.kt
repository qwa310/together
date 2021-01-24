package com.example.together

import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EmergencyCallSetting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emergency_call_setting)

        findViewById<FloatingActionButton>(R.id.add_number).setOnClickListener {
            val contactIntent = Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI)
            startActivity(contactIntent)
            finish()
        }
    }
}