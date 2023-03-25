package com.chpham.pomodoro_todo.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * A base class for fragments that use View Binding to bind layout views to their respective fields.
 *
 * @param Binding the type of the generated View Binding class for the associated layout file.
 *
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<Binding : ViewBinding> : Fragment() {

    /**
     * The backing field for the [binding] property.
     */
    private var _binding: ViewBinding? = null

    /**
     * The View Binding object for the associated layout file. This property is lazily initialized
     * by [initViewBinding], which must be implemented by subclasses.
     */
    protected val binding: Binding get() = (_binding!! as Binding)

    /**
     * Called to create the view for this fragment. Initializes the [binding] property by calling
     * [initViewBinding], and returns the root view of the binding.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = initViewBinding(inflater, container)
        return binding.root
    }

    /**
     * Called to initialize the View Binding object for the associated layout file. This method must be
     * implemented by subclasses to return the generated View Binding object for the fragment's layout.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @return The View Binding object for the fragment's layout.
     */
    abstract fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding?

    /**
     * Called when the view previously created by [onCreateView] has been detached from the fragment.
     * Resets the [binding] property to null.
     */
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
