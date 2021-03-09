package dev.decagon.godday.dummyblogfragments.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dev.decagon.godday.dummyblogfragments.adapter.CommentAdapter
import dev.decagon.godday.dummyblogfragments.databinding.FragmentCommentsBinding
import dev.decagon.godday.dummyblogfragments.viewmodel.SharedViewModel


/**
 * This class represents the comment fragment. It displays a post with a list
 * of all it comments and an interface for the users to add new comments to
 * the post.
 */
class CommentsFragment : Fragment() {

    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!
    val adapter = CommentAdapter()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Setup data binding
         */
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            commentFragment = this@CommentsFragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}