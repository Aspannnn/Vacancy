package kz.aspan.vacancy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kz.aspan.vacancy.R
import kz.aspan.vacancy.databinding.ItemVacancyDetailBinding
import kz.aspan.vacancy.domain.model.Data
import javax.inject.Inject

class JobListAdapter @Inject constructor() :
    ListAdapter<Data, JobListAdapter.SimpleDataViewHolder>(DataItemDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleDataViewHolder {
        return SimpleDataViewHolder(
            ItemVacancyDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SimpleDataViewHolder, position: Int) {
        val data = getItem(position)
        val context = holder.itemView.context
        holder.binding.apply {
            professionTv.text = data.profession
            companyTv.text = data.company
            industryTv.text = data.industry
            descriptionTv.text = data.description
            responseButton.setOnClickListener {
                onVacancyClickListener?.let { click ->
                    click(data)
                }
            }

            for (skill in data.skills) {
                addNewChip(context, skillsChipGroup, skill)
            }
        }
    }


    class SimpleDataViewHolder(val binding: ItemVacancyDetailBinding) :
        RecyclerView.ViewHolder(binding.root)

    class DataItemDiffCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }


    private var onVacancyClickListener: ((Data) -> Unit)? = null

    fun setOnVacancyClickListener(listener: (Data) -> Unit) {
        onVacancyClickListener = listener
    }

    private fun addNewChip(context: Context, parent: ChipGroup, text: String) {
        val chip = LayoutInflater.from(context).inflate(R.layout.single_chip, parent, false) as Chip
        chip.text = text
        parent.addView(chip)
    }
}