package kz.aspan.vacancy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kz.aspan.vacancy.R
import kz.aspan.vacancy.databinding.ItemVacancyListBinding
import kz.aspan.vacancy.domain.model.Data
import kz.aspan.vacancy.domain.model.SimpleData
import javax.inject.Inject

class VacancyListAdapter @Inject constructor() :
    ListAdapter<SimpleData, VacancyListAdapter.SimpleDataViewHolder>(DataItemDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleDataViewHolder {
        return SimpleDataViewHolder(
            ItemVacancyListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SimpleDataViewHolder, position: Int) {
        val data = getItem(position)
        holder.binding.apply {
            titleTv.text = data.title
            vacancyCount.text =
                holder.itemView.context.getString(R.string.count_vacancies, data.numberOfVacancies)
            root.setOnClickListener {
                onVacancyClickListener?.let { click ->
                    click(data)
                }
            }
        }
    }


    class SimpleDataViewHolder(val binding: ItemVacancyListBinding) :
        RecyclerView.ViewHolder(binding.root)

    class DataItemDiffCallback : DiffUtil.ItemCallback<SimpleData>() {
        override fun areItemsTheSame(oldItem: SimpleData, newItem: SimpleData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SimpleData, newItem: SimpleData): Boolean {
            return oldItem == newItem
        }
    }


    private var onVacancyClickListener: ((SimpleData) -> Unit)? = null

    fun setOnVacancyClickListener(listener: (SimpleData) -> Unit) {
        onVacancyClickListener = listener
    }
}