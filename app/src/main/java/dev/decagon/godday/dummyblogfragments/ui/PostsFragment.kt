package dev.decagon.godday.dummyblogfragments.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import dev.decagon.godday.dummyblogfragments.R
import dev.decagon.godday.dummyblogfragments.adapter.PostAdapter
import dev.decagon.godday.dummyblogfragments.databinding.FragmentPostsBinding
import dev.decagon.godday.dummyblogfragments.utils.setVisibility
import dev.decagon.godday.dummyblogfragments.viewmodel.SharedViewModel

/**
 * This fragment represent the first screen in the application. It displays a list of post
 * with the post id, title and body using recycler view. Clicking on a post takes the
 * user to the post detail page where they can view all the comments on the post as well
 * as add new comments to the post. This application follows the MVVM architecture design pattern.
 */

class PostsFragment : Fragment() {

    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedViewModel by activityViewModels()
    lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostsBinding.inflate(layoutInflater)
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
            postFragment = this@PostsFragment
        }

        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

        /**
         * Initialize and setup the recyclerview adapter, implement the click listener
         * which it takes as a parameter, define the logic for navigating to the next
         * fragment and pass in the necessary data needed for the next fragment
         */
        adapter = PostAdapter(PostAdapter.OnClickListener{
            sharedViewModel.setPost(it)
            findNavController().navigate(R.id.commentsFragment, null, options)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.post_menu, menu)
    }

    /**
     * Setup the menu items to handle click events
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> { setVisibility(binding.textField) }
            R.id.filter -> { setVisibility(binding.filterField) }
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}