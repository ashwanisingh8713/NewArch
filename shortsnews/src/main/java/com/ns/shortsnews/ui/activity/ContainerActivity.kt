package com.ns.shortsnews.ui.activity

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ns.shortsnews.R
import com.ns.shortsnews.databinding.ActivityContainerBinding
import com.ns.shortsnews.domain.models.ProfileData
import com.ns.shortsnews.ui.fragment.EditProfileFragment
import com.ns.shortsnews.ui.fragment.InterestsFragment

class ContainerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        val view = binding.root
        window.statusBarColor = Color.parseColor("#000000")
        window.navigationBarColor = Color.parseColor("#000000")
        when(intent.getStringExtra("to")){
             "interests" -> {
                launchPersonaliseFragment()
             }
            "edit_profile" -> {
                val userData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra("profile_data", ProfileData::class.java)
                } else {
                    intent.getParcelableExtra("profile_data")
                }
                if (userData != null) {
                    launchEditProfileFragment(userData)
                }
            }
        }

        setContentView(view)

    }
    private fun launchPersonaliseFragment(){
        val fragment = InterestsFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fc, fragment).commit()

    }

    private fun launchEditProfileFragment(profileData: ProfileData) {
        val fragment = EditProfileFragment()
        val bundle = Bundle()
        bundle.putParcelable("profile", profileData)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fc, fragment)
            .commit()
    }
}