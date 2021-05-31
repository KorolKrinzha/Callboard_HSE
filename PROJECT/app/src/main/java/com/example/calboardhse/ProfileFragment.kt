package com.example.calboardhse

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.callboardhse.NavigationController
import com.example.callboardhse.R

class ProfileFragment:Fragment(R.layout.activity_profile) {
    private lateinit var UserName: String
    private lateinit var usernametext:TextView
    var navigationController: NavigationController? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usernametext= view.findViewById(R.id.username)



    }
}