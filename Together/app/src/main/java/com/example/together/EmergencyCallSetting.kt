package com.example.together

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.ContentResolver
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.emergency_call_list.view.*
import java.util.*

class EmergencyCallSetting : Fragment() {



    val data = arrayListOf(
            Contact(1,
                    "엄마",
                    "01011112222",
                    R.drawable.ic_location),
            Contact(2,
                    "아빠",
                    "01011112223",
                    R.drawable.ic_location),
            Contact(3,
                    "동생",
                    "01011112224",
                    R.drawable.ic_location)
    );

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        val view = inflater.inflate(R.layout.emergency_call_setting, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = CallAdapter(data, view.context)

        view.findViewById<Button>(R.id.add_number).setOnClickListener {
            val contactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(contactIntent, 10)

            fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (resultCode == Activity.RESULT_OK) {

                    val cursor: Cursor = ContentResolver.query(
                            arrayOf(
                                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                            ), null, null, null
                    )

                    cursor.moveToFirst();
                    var Name = cursor.getString(0);
                    var Phone = cursor.getString(1);

                    //data.
                    cursor.close();

                }

            }
            getActivity()?.finish();
        }

        }
        return view;
    }
}