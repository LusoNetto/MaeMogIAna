package com.ibrahimethemsen.geminiai.text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ibrahimethemsen.geminiai.databinding.FragmentTextBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TextFragment : Fragment() {
    private var _binding : FragmentTextBinding? = null
    private val binding : FragmentTextBinding get() = _binding!!
    private val viewModel by viewModels<TextViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTextBinding.inflate(inflater,container,false)
        return binding.root
    }
}