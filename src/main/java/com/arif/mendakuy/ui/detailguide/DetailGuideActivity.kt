package com.arif.mendakuy.ui.detailguide

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.arif.mendakuy.data.model.Guide
import com.arif.mendakuy.databinding.ActivityDetailGuideBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class DetailGuideActivity : AppCompatActivity() {
    private lateinit var detailGuideViewModel: DetailGuideViewModel
    private var _binding : ActivityDetailGuideBinding? = null

    private val binding get() = _binding!!

    private lateinit var progressDialog: ProgressDialog

    val guide = Guide()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailGuideViewModel = ViewModelProvider(this)[DetailGuideViewModel::class.java]

        _binding = ActivityDetailGuideBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        onViewCreated()
    }

    private fun onViewCreated() {

        binding.apply {
            detailGuideViewModel.getDetailGuide(
                intent.getStringExtra("id")
            )

            detailGuideViewModel.state.observe(this@DetailGuideActivity) { state ->
                when (state) {
                    is DetailGuideViewState.Progress -> onProgress(state.isLoading)
                    else -> {}
                }
        }

            detailGuideViewModel.detailGuide.observe(this@DetailGuideActivity) { guide ->

                titleGuide.text = guide.guideTitle
                descGuide.text = guide.guideDesc
                Glide.with(ivGuideBanner).load(guide.guideImg).transform(RoundedCorners(20)).into(ivGuideBanner)
            }
    }
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

}

    private fun onProgress(loading: Boolean){
        if (loading) {
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }
    }
