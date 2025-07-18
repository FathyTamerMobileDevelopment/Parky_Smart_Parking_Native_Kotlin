package com.example.parkyapplication.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.parkyapplication.R
import com.example.parkyapplication.data.onboardingData.OnboardingItem
import com.example.parkyapplication.databinding.ActivityOnboardingBinding
import com.example.parkyapplication.ui.login.LoginScreenActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var adapter: OnboardingAdapter
    private lateinit var items: List<OnboardingItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        instantGoneBackBtn()
        initOnboardingItems()
        setupViewPager()
        setupButtons()
        handlePageChange()
    }

    private fun instantGoneBackBtn() {
        binding.btnBack.visibility = View.GONE
    }

    private fun initOnboardingItems() {
        items = listOf(
            OnboardingItem(
                R.drawable.screens,
                "Never Lose Your Perfect Parking Spot Again",
                "Save your favorite parking spots for later"
            ),
            OnboardingItem(
                R.drawable.screens1,
                "Track your parking booking the easy way!",
                "The Easiest Way to track Your Parking Booking"
            ),
            OnboardingItem(
                R.drawable.screens2,
                "Find Nearby Parking Spots with Ease",
                "Discover parking spots nearby effortlessly."
            )
        )
    }

    private fun setupViewPager() {
        adapter = OnboardingAdapter(items)
        binding.viewPager.adapter = adapter
        binding.dotsIndicator.setViewPager2(binding.viewPager)
    }

    private fun setupButtons() {
        binding.btnBack.setOnClickListener {
            val current = binding.viewPager.currentItem
            if (current > 0) {
                binding.viewPager.currentItem = current - 1
            }
        }

        binding.btnNext.setOnClickListener {
            val current = binding.viewPager.currentItem
            if (current < items.lastIndex) {
                binding.viewPager.currentItem = current + 1
            } else {
                startActivity(Intent(this, LoginScreenActivity::class.java))
                finish()
            }
        }
    }

    private fun handlePageChange() {
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position == 0) {
                    binding.btnBack.visibility = View.GONE
                } else {
                    binding.btnBack.visibility = View.VISIBLE
                }

            }
        })
    }
}
