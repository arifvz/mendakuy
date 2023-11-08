package com.arif.mendakuy.ui.guide

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.arif.mendakuy.data.model.Guide
import com.arif.mendakuy.databinding.FragmentGuideBinding
import com.arif.mendakuy.ui.adapter.GuideAdapter

class GuideFragment : Fragment() {

    private lateinit var guideViewModel: GuideViewModel
    private var _binding : FragmentGuideBinding? = null

    private val binding get() = _binding!!

    private val guideAdapter = GuideAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        guideViewModel = ViewModelProvider(this)[GuideViewModel::class.java]

        _binding = FragmentGuideBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {
            recycleView.adapter = guideAdapter
            recycleView.layoutManager = LinearLayoutManager(context)

        }

        guideViewModel.getGuideList()
        guideViewModel.guide.observe(viewLifecycleOwner) {
            guideAdapter.setData(it)
        }
    }

    private fun onShowMountDetail(guide: List<Guide>){
        guideAdapter.setData(guide)
    }

}