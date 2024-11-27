package com.example.lab8working

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GoalsAdapter : RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {

    private var goals: MutableList<Goal> = mutableListOf()
    private lateinit var goalsDao: GoalsDao

    fun updateGoals(goals: List<Goal>) {
        this.goals.clear()
        this.goals.addAll(goals)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.goal_view, parent, false)
        return GoalViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        val currentGoal = goals[position]
        holder.goalTextView.text = currentGoal.goalText
    }

    override fun getItemCount(): Int {
        return goals.size
    }

    inner class GoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val goalTextView: TextView = itemView.findViewById(R.id.goalTextView)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteGoalButton)

        init {
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val goal = goals[position]
                    CoroutineScope(Dispatchers.IO).launch {
                        val database = AppDatabase.getDatabase(itemView.context) // Get database instance
                        goalsDao = database.GoalsDao() // Get GoalsDao instance
                        goalsDao.deleteGoal(goal)
                        withContext(Dispatchers.Main) {
                            goals.removeAt(position)
                            notifyItemRemoved(position)
                        }
                    }
                }
            }
        }
    }
}