package com.example.mvvm_dagger.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm_dagger.CountriesApplication
import com.example.mvvm_dagger.CountryListAdapter
import com.example.mvvm_dagger.OnClickListener
import com.example.mvvm_dagger.R
import com.example.mvvm_dagger.databinding.ActivityMainBinding
import com.example.mvvm_dagger.model.Country
import com.example.mvvm_dagger.viewmodel.MainViewModel
import com.example.mvvm_dagger.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel

    private lateinit var countryList: ArrayList<Country>
    private lateinit var countriesAdapter: CountryListAdapter

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory //Dagger will provide the object to

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (application as CountriesApplication).applicationComponent.inject(this)

        setSupportActionBar(binding.toolbar)

        /*val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)*/

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }

        // what the above code do is, It will check the file for any of the @Inject property and if there are any
        // it will inject the correct object to them. Here it is mainViewModelFactory

        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

        countryList = arrayListOf()
        mainViewModel.refresh()

        countriesAdapter = CountryListAdapter(arrayListOf(), OnClickListener { item ->
            Toast.makeText(this, "Clicked Country is  ${item.countryName}", Toast.LENGTH_LONG)
                .show()
        })

        binding.content.countriesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }

        binding.content.swipeRefreshLayout.setOnRefreshListener {
            binding.content.swipeRefreshLayout.isRefreshing = false
            mainViewModel.refresh()
        }

        observerViewModel()
    }

    private fun observerViewModel() {
        mainViewModel.countriesLiveData.observe(this) { countries ->
            countries?.let {
                binding.content.countriesList.visibility = View.VISIBLE
                countryList.addAll(it)
                countriesAdapter.updateCountries(it)
            }
        }

        mainViewModel.countryLoadError.observe(this) { isError ->
            isError?.let {
                binding.content.listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

        mainViewModel.loading.observe(this) { isLoading ->
            isLoading?.let {
                binding.content.loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.content.countriesList.visibility = View.GONE
                    binding.content.listError.visibility = View.GONE
                }
            }
        }
    }

    /*override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }*/
}