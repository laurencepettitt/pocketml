package com.example.pocketml.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pocketml.DImagesQuery
import com.example.pocketml.R
import com.example.pocketml.databinding.FragmentDatasetManagerOverviewBinding
import com.example.pocketml.ui.viewmodel.OverviewViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Data binding
        val binding: FragmentDatasetManagerOverviewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_dataset_manager_overview, container, false
        )
        binding.lifecycleOwner = this

        // RecyclerView layout manager
        val manager = GridLayoutManager(activity, 3)
        binding.datasetList.layoutManager = manager

        // RecyclerView Adapter
        val adapter = OverviewAdapter(DImageClickListener { dImage ->
            viewModel.onDImageClicked(dImage)
        })
        binding.datasetList.adapter = adapter

        viewModel.dImages.observe(viewLifecycleOwner) { dImages: List<DImagesQuery.DImage> ->
            adapter.submitList(dImages)
        }

        viewModel.selectedDImage.observe(viewLifecycleOwner) { dImageId: String? ->
            dImageId?.let{
                findNavController().navigate(
                    OverviewFragmentDirections.actionOverviewFragmentToDetailFragment(dImageId)
                )
                viewModel.doneDImageDetailNavigated()
            }
        }

        binding.addToDatasetFab.setOnClickListener {
            findNavController().navigate(
                OverviewFragmentDirections.actionOverviewFragmentToDetailFragment(null)
            )
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // TODO: fetchOnlyNewDImage
        viewModel.loadDImages()
    }
}
