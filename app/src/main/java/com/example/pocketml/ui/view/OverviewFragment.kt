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

    private lateinit var binding: FragmentDatasetManagerOverviewBinding

    private lateinit var overviewAdapter: OverviewAdapter

    private fun onBind() {
        val manager = GridLayoutManager(activity, 3)
        binding.datasetList.layoutManager = manager
        overviewAdapter = OverviewAdapter(DImageClickListener { dImage ->
            viewModel.onDImageClicked(dImage)
        })
        binding.datasetList.adapter = overviewAdapter

        binding.addToDatasetFab.setOnClickListener {
            findNavController().navigate(
                OverviewFragmentDirections.actionOverviewFragmentToDetailFragment(null)
            )
        }
    }

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
        onBind()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dImages.observe(viewLifecycleOwner) { dImages: List<DImagesQuery.DImage> ->
            overviewAdapter.submitList(dImages)
        }

        viewModel.selectedDImage.observe(viewLifecycleOwner) { dImageId: String? ->
            dImageId?.let {
                findNavController().navigate(
                    OverviewFragmentDirections.actionOverviewFragmentToDetailFragment(dImageId)
                )
                viewModel.doneDImageDetailNavigated()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO: fetchOnlyNewDImage
        viewModel.loadDImages()
    }
}
