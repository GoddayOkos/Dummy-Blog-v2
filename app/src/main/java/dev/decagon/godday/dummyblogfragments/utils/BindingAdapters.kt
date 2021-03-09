package dev.decagon.godday.dummyblogfragments.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.decagon.godday.dummyblogfragments.R
import dev.decagon.godday.dummyblogfragments.adapter.CommentAdapter
import dev.decagon.godday.dummyblogfragments.adapter.PostAdapter
import dev.decagon.godday.dummyblogfragments.model.Comment
import dev.decagon.godday.dummyblogfragments.model.Post
import dev.decagon.godday.dummyblogfragments.ui.CommentsFragment
import dev.decagon.godday.dummyblogfragments.ui.PostsFragment
import dev.decagon.godday.dummyblogfragments.viewmodel.SharedViewModel


private lateinit var alertDialog: AlertDialog
private lateinit var dialogBuilder: AlertDialog

/**
 * Show custom alert dialog to add post when the floating action button is clicked
 */
@BindingAdapter("customOnClickListener")
fun View.customOnClickListener(viewModel: SharedViewModel) {
    setOnClickListener {
        showDialog(it, viewModel)
    }
}

/**
 * Show custom alert dialog to add comment when the floating action button is clicked
 */
@BindingAdapter("customClickListener")
fun View.customClickListener(viewModel: SharedViewModel) {
    setOnClickListener {
        buildDialog(it, viewModel)
    }
}

/**
 * Setup recyclerview for comment fragment
 */
@BindingAdapter(value = ["adapter", "sharedViewModel", "progressBar", "fragment"], requireAll = true)
fun RecyclerView.setAdapter(adapter: CommentAdapter, sharedViewModel: SharedViewModel,
                            progressBar: ProgressBar, fragment: CommentsFragment) {
    sharedViewModel.allComments.observe(fragment.viewLifecycleOwner, {
        progressBar.visibility = View.GONE
        adapter.loadData(it)
        this.adapter = adapter
        this.addItemDecoration(DividerItemDecoration(fragment.requireContext(), DividerItemDecoration.VERTICAL))
    })
}

@BindingAdapter("querySearch")
fun querySearch(view: TextInputEditText, sharedViewModel: SharedViewModel) {
    view.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {sharedViewModel.searchPosts(s.toString())}

    })
}

/**
 * Setup recyclerview for post fragment
 */
@BindingAdapter(value = ["adapter", "sharedViewModel", "progressBar", "fragment"], requireAll = true)
fun RecyclerView.addAdapter(adapter: PostAdapter, sharedViewModel: SharedViewModel,
                            progressBar: ProgressBar, fragment: PostsFragment) {
    sharedViewModel.posts.observe(fragment.viewLifecycleOwner, {
        progressBar.visibility = View.GONE
        adapter.loadData(it)
        this.adapter = adapter
    })
    sharedViewModel.filteredList.observe(fragment.viewLifecycleOwner, { adapter.loadData(it) })
    sharedViewModel.searchPost.observe(fragment.viewLifecycleOwner, { adapter.loadData(it) })
}

/**
 *  Configuration for filtering posts based on
 *  user id and return all post if invalid inputs are provided and notify the
 *  the user with a toast
 */
@BindingAdapter(value = ["viewModel", "adapter"], requireAll = true)
fun filter(view: TextInputLayout, viewModel: SharedViewModel, adapter: PostAdapter) {
    view.setEndIconOnClickListener {
        val input = view.editText!!.text.toString()
        if (input == "" || input == "0") {
            view.editText!!.text.clear()
            adapter.loadData(viewModel.posts.value!!)
            Toast.makeText(view.context, "Invalid input", Toast.LENGTH_LONG).show()
            return@setEndIconOnClickListener
        }
        val userId = input.toInt()
        if (userId !in 0..10) {
            view.editText!!.text.clear()
            Toast.makeText(view.context, "Invalid input", Toast.LENGTH_LONG).show()
            return@setEndIconOnClickListener
        }
        viewModel.loadPosts(userId)
        view.editText!!.text.clear()
    }
}

/**
 * This method defines the action that occurs when the floating action button is clicked.
 * It also validates the inputs and handles the logic for creating new post using the
 * helper method provided by the view model
 */
private fun showDialog(view: View, viewModel: SharedViewModel) {
    alertDialog = MaterialAlertDialogBuilder(view.context)
            .setTitle("Create a new post")
            .setView(R.layout.dialog_layout)
            .setCancelable(false)
            .setNegativeButton("CANCEL") { _, _ -> }
            .setPositiveButton("DONE") { _, _ ->
                val id: TextInputLayout = alertDialog.findViewById(R.id.user_id)!!
                val titleTemp: TextInputLayout = alertDialog.findViewById(R.id.title)!!
                val bodyTemp: TextInputLayout = alertDialog.findViewById(R.id.body)!!
                val userId = try {
                    id.editText!!.text.toString().toInt()
                } catch (e: NumberFormatException) {
                    0
                }
                val title = titleTemp.editText!!.text.toString().trim()
                val body = bodyTemp.editText!!.text.toString().trim()

                if (userId !in 1..10 || title.isEmpty() || body.isEmpty()) {
                    Toast.makeText(view.context, "Invalid inputs!", Toast.LENGTH_LONG).show()
                    return@setPositiveButton
                }
                val post = Post(userId, null, title, body)
                viewModel.createPost(post)
                Toast.makeText(view.context, "Post created!", Toast.LENGTH_LONG).show()
            }
            .show()

}

/**
 * This method defines the action that occurs when the floating action button is clicked.
 * It also validates the inputs and handles the logic for creating new comments using the
 * helper method provided by the view model
 */
private fun buildDialog(view: View, viewModel: SharedViewModel) {
        dialogBuilder = MaterialAlertDialogBuilder(view.context)
                .setTitle("Add a new comment")
                .setView(R.layout.comment_dialog)
                .setCancelable(false)
                .setNegativeButton("CANCEL") { _, _ -> }
                .setPositiveButton("DONE") { _, _ ->
                    val nameTemp: TextInputLayout = dialogBuilder.findViewById(R.id.name)!!
                    val emailTemp: TextInputLayout = dialogBuilder.findViewById(R.id.email)!!
                    val bodyTemp: TextInputLayout = dialogBuilder.findViewById(R.id.body)!!
                    val name = nameTemp.editText!!.text.toString().trim()
                    val email = emailTemp.editText!!.text.toString().trim()
                    val body = bodyTemp.editText!!.text.toString().trim()

                    if (name.isEmpty() || email.isEmpty() || body.isEmpty()) {
                        Toast.makeText(view.context, "Invalid inputs!", Toast.LENGTH_LONG).show()
                        return@setPositiveButton
                    }
                    val comment = Comment(viewModel.post.id!!, null, name, email, body)
                     viewModel.createComment(comment)
                    Toast.makeText(view.context, "Comment added!", Toast.LENGTH_LONG).show()
                }
                .show()
}

/**
 * Function to set the visibility of the search fields when menu items are clicked
 */
fun setVisibility(view: TextInputLayout) {
    if (view.visibility == View.GONE) {
        view.visibility = View.VISIBLE
    } else {
        view.editText!!.text.clear()
        view.visibility = View.GONE
    }
}


