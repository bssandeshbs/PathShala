package com.nyc.schools.ui

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.nyc.schools.BaseFragment
import com.nyc.schools.R
import com.nyc.schools.ViewModelProviderFactory
import com.nyc.schools.data.model.SatScores
import com.nyc.schools.data.model.SchoolInfo
import com.nyc.schools.data.remote.RemoteError
import com.romainpiel.shimmer.Shimmer
import com.romainpiel.shimmer.ShimmerTextView
import javax.inject.Inject

class SchoolScoresFragment @Inject constructor() : BaseFragment() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    private lateinit var schoolViewModel: SchoolViewModel
    private var schoolInfo: SchoolInfo? = null

    // TODO: Replace all the findViewById with View Binding for cleaner code
    private lateinit var schoolNameTV: TextView
    private lateinit var schoolDescriptionTV: TextView
    private lateinit var satTestTakersTV: TextView
    private lateinit var satCriticalReadingScoreTV: TextView
    private lateinit var satWritingScoreTV: TextView
    private lateinit var satMathScoreTV: TextView
    private lateinit var shimmerTextView: ShimmerTextView
    private lateinit var scoresContent: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        schoolInfo = arguments?.getParcelable("schoolInfo")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_school_scores, container, false)
        shimmerTextView = root.findViewById(R.id.shimmer)
        schoolNameTV = root.findViewById(R.id.school_name)
        schoolDescriptionTV = root.findViewById(R.id.description)
        satTestTakersTV = root.findViewById(R.id.sat_test_takers)
        satCriticalReadingScoreTV = root.findViewById(R.id.sat_critical_reading_score)
        satWritingScoreTV = root.findViewById(R.id.sat_writing_score)
        satMathScoreTV = root.findViewById(R.id.sat_math_avg_score)
        scoresContent = root.findViewById(R.id.scores_content_view)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Shimmer().start(shimmerTextView)
        updateViews(false)

        schoolViewModel =
            ViewModelProvider(requireActivity(), providerFactory)[SchoolViewModel::class.java]

        if (schoolInfo != null && schoolInfo!!.dbn.isNotEmpty()) {
            schoolViewModel.getSatScore(schoolInfo!!.dbn)
        }

        schoolViewModel.satScores().observe(viewLifecycleOwner) {
            updateView(it)
        }

        schoolViewModel.error().observe(viewLifecycleOwner) {
            it?.let {
                // Few DBN Id's return null from API, Just show schoolInfo in that case
                // also show schoolInfo also when network is not present
                if ((it.errorCode == RemoteError.JsonParseError().errorCode)
                    || (it.errorCode == RemoteError.NoNetworkError().errorCode)) {
                    updateView(null)
                } else {
                    Snackbar.make(scoresContent, getString(it.errorMessage), Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun updateView(satScores: SatScores?) {
        schoolNameTV.text = schoolInfo?.schoolName
        schoolDescriptionTV.text = schoolInfo?.overview

        Shimmer().cancel()
        updateViews(true)

        satScores?.let {
            satTestTakersTV.text = satScores.satTestTakers
            satCriticalReadingScoreTV.text = satScores.satReadingAvg
            satWritingScoreTV.text = satScores.satWritingAvg
            satMathScoreTV.text = satScores.satMathAvg
        } ?: run {
            satTestTakersTV.text = getString(R.string.no_data_available)
            satCriticalReadingScoreTV.text = getString(R.string.no_data_available)
            satWritingScoreTV.text = getString(R.string.no_data_available)
            satMathScoreTV.text = getString(R.string.no_data_available)
        }
    }

    private fun updateViews(isDataShown: Boolean) {
        if (isDataShown) {
            shimmerTextView.visibility = View.GONE
            scoresContent.visibility = View.VISIBLE
        } else {
            shimmerTextView.visibility = View.VISIBLE
            scoresContent.visibility = View.GONE
        }
    }
}