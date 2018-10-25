package com.hamletleon.moviewratings.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hamletleon.moviewratings.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {
    private lateinit var mViewModel: MainViewModel
    private lateinit var mNavController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mNavController = Navigation.findNavController(requireActivity(), R.id.navigationFragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        setupWithNavController(navigation, mNavController)
    }

    // TODO: Unnecessary code until Google adds the bundle on the BottomNavigationView method
    private fun setupWithNavController(bottomNavigationView: BottomNavigationView, navController: NavController) {
        bottomNavigationView.setOnNavigationItemSelectedListener { item -> this.onNavDestinationSelected(item, navController, true)  }
        navController.addOnNavigatedListener { _, destination ->
            val menu = bottomNavigationView.menu
            var h = 0
            val size = menu.size()
            while (h < size) {
                val item = menu.getItem(h)
                if (matchDestination(destination, item.itemId)) {
                    item.isChecked = true
                }
                h++
            }
        }
    }
    private fun onNavDestinationSelected(item: MenuItem, navController: NavController, popUp: Boolean, bundle: Bundle? = null): Boolean {
        if (item.isChecked) return false
        val builder = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setEnterAnim(R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
        if (popUp) {
            builder.setPopUpTo(findStartDestination(navController.graph)?.id ?: 0, false)
        }
        val options = builder.build()
        return try {
            navController.navigate(item.itemId, bundle, options)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
    private fun findStartDestination(graph: NavGraph): NavDestination? {
        var startDestination: NavDestination? = graph
        while (startDestination is NavGraph) {
            val parent = startDestination as NavGraph?
            startDestination = parent!!.findNode(parent.startDestination)
        }
        return startDestination
    }
    private fun matchDestination(destination: NavDestination, @IdRes destId: Int): Boolean {
        var currentDestination: NavDestination? = destination
        while (currentDestination!!.id != destId && currentDestination.parent != null) {
            currentDestination = currentDestination.parent
        }
        return currentDestination.id == destId
    }
}