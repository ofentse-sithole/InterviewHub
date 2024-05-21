package com.example.interviewhub.unity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewhub.R

class InterviewObservationAdapter : ListAdapter<SaveInterview, InterviewObservationAdapter.InterviewObservationViewHolder>(SaveBirdDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterviewObservationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.observation_item, parent, false)
        return InterviewObservationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InterviewObservationViewHolder, position: Int) {
        val observation = getItem(position)
        holder.bind(observation)
    }

    inner class InterviewObservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val question: TextView = itemView.findViewById(R.id.edtInterviewQ1)
        private val answer: TextView = itemView.findViewById(R.id.edtInterviewAnswer)
        private val tip: TextView = itemView.findViewById(R.id.edtInterviewTip)
        private val difficulty: TextView = itemView.findViewById(R.id.edtInterviewDifficulty)

        fun bind(observation: SaveInterview) {
            question.text = "Question-> ${observation.questions}"
            answer.text = "Answer -> ${observation.answers}"
            tip.text = "Tip -> ${observation.tips}"
            difficulty.text = "Difficulty -> ${observation.difficulties}"
        }
    }

    class SaveBirdDiffCallback : DiffUtil.ItemCallback<SaveInterview>() {
        override fun areItemsTheSame(oldItem: SaveInterview, newItem: SaveInterview): Boolean {
            return oldItem.questions == newItem.questions // Compare items based on their unique ID
        }

        override fun areContentsTheSame(oldItem: SaveInterview, newItem: SaveInterview): Boolean {
            return oldItem == newItem
        }
    }

}