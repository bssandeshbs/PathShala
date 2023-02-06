package com.nyc.schools.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nyc.schools.R
import com.nyc.schools.data.model.SchoolInfo
import java.util.*

/*
 Adapter which builds list of schools
 */
class SchoolListAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var schoolList: MutableList<SchoolInfo> = LinkedList()
    lateinit var onSchoolClick: ((SchoolInfo) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem: View = inflater.inflate(R.layout.school_item_row, parent, false)
        return SchoolViewHolder(viewItem, onSchoolClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val school = schoolList[position]
        val schoolViewHolder = holder as SchoolViewHolder
        schoolViewHolder.bind(school)
    }

    override fun getItemCount(): Int {
        return schoolList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(schoolInfoList: List<SchoolInfo>) {
        schoolList.clear()
        notifyDataSetChanged()
        schoolList.addAll(schoolInfoList)
    }

    fun addAll(schoolInfoList: List<SchoolInfo>) {
        val position: Int = schoolList.size
        schoolList.clear()
        schoolList.addAll(schoolInfoList)
        notifyItemRangeInserted(position, schoolInfoList.size)
    }

    class SchoolViewHolder(itemView: View, val onSchoolClick: ((SchoolInfo) -> Unit)) :
        RecyclerView.ViewHolder(itemView) {
        private val schoolName: TextView
        private val website: TextView
        private val neighbourhood: TextView
        private val city: TextView
        private val finalGrades: TextView

        init {
            schoolName = itemView.findViewById<View>(R.id.school_name) as TextView
            neighbourhood = itemView.findViewById<View>(R.id.neighbourhood) as TextView
            city = itemView.findViewById<View>(R.id.city) as TextView
            website = itemView.findViewById<View>(R.id.website) as TextView
            finalGrades = itemView.findViewById<View>(R.id.final_grades) as TextView
        }

        fun bind(schoolInfo: SchoolInfo) {
            schoolName.text = schoolInfo.schoolName
            finalGrades.text = schoolInfo.finalGrades
            website.text = schoolInfo.website
            city.text = schoolInfo.city
            neighbourhood.text = schoolInfo.neighborhood
            itemView.setOnClickListener { onSchoolClick(schoolInfo) }
        }
    }
}
