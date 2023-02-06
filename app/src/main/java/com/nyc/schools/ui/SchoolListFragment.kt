package com.nyc.schools.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nyc.schools.BaseFragment
import com.nyc.schools.R
import com.nyc.schools.ViewModelProviderFactory
import com.nyc.schools.data.model.SchoolInfo
import com.romainpiel.shimmer.Shimmer
import com.romainpiel.shimmer.ShimmerTextView
import javax.inject.Inject

class SchoolListFragment @Inject constructor() : BaseFragment() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    private lateinit var schoolViewModel: SchoolViewModel
    private lateinit var schoolListAdapter: SchoolListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var shimmerTextView: ShimmerTextView

    private var isDataLoading = false
    private var isSearchRequest = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_school_list, container, false)
        recyclerView = root.findViewById(R.id.school_recycler_view)
        shimmerTextView = root.findViewById(R.id.shimmer)
        return root
    }

    private fun onSchoolSelected(schoolInfo: SchoolInfo) {
        val action = SchoolListFragmentDirections.actionToSchoolScores(schoolInfo)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        Shimmer().start(shimmerTextView)

        schoolViewModel =
            ViewModelProvider(requireActivity(), providerFactory)[SchoolViewModel::class.java]
        schoolListAdapter = SchoolListAdapter()
        schoolListAdapter.onSchoolClick = ::onSchoolSelected

        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = schoolListAdapter
            addOnScrollListener(pageScrollListener)
        }

        schoolViewModel.getSchoolList()
        schoolViewModel.schoolList().observe(viewLifecycleOwner) { schoolList ->
            schoolList?.let { updateSchoolList(schoolList) }
        }

        schoolViewModel.error().observe(viewLifecycleOwner) {
            it?.let {
                Snackbar.make(recyclerView, getString(it.errorMessage), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun updateSchoolList(schoolList: List<SchoolInfo>) {
        schoolList.let {
            if (!isSearchRequest) {
                isDataLoading = false
                schoolListAdapter.addAll(schoolList)
                setListVisible()
            } else {
                schoolListAdapter.update(schoolList)
            }
        }
    }

    private fun fetchMoreSchools() {
        schoolViewModel.getSchoolList(true)
    }

    private fun setListVisible() {
        Shimmer().cancel()
        shimmerTextView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.action_search) {
                    search(menuItem)
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun search(menuItem: MenuItem) {
        val searchView = menuItem.actionView as? SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                // If query text length is greater than filter or else get all schools
                if (query != null && query.length > 1) {
                    isSearchRequest = true
                    schoolViewModel.filterValues(query)
                } else {
                    isSearchRequest = false
                    schoolViewModel.getSavedSchools()
                }
                return true
            }
        })
    }

    private val pageScrollListener by lazy {
        object :
            PaginationScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                // Don't fetch new records if an existing request is already in progress
                // or if its search job
                if (!isDataLoading && !isSearchRequest) {
                    isDataLoading = true
                    fetchMoreSchools()
                }
            }
            override var isLoading: Boolean = isDataLoading
        }
    }
}