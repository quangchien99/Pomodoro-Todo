package com.chpham.pomodoro_todo.base.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * Abstract base class for activities in an Android application that uses view binding.
 * @param Binding The view binding class for the activity.
 *
 * @since March 25, 2023
 * @version 1.0
 * @authoredBy Chien.Ph
 * © copyright 2023 Chien.Ph. All rights reserved.
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<Binding : ViewBinding> : AppCompatActivity() {

    // The view binding instance for this activity.
    private var _binding: ViewBinding? = null

    // Getter for the view binding instance. This will always return a Binding instance,
    // because the _binding field is always cast to Binding before being returned.
    protected val binding: Binding get() = (_binding!! as Binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the view binding.
        _binding = initViewBinding()
        // Set the content view to the root view of the view binding.
        setContentView(binding.root)
    }

    /**
     * Initializes the view binding for this activity.
     * @return The view binding instance for this activity, or null if view binding is not used.
     */
    abstract fun initViewBinding(): ViewBinding?

    /**
     * Clean up the view binding instance to prevent memory leaks.
     */
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
