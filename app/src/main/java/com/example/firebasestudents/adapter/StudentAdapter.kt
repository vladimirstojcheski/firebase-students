package com.example.firebasestudents.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasestudents.R
import com.example.firebasestudents.model.Student

class StudentAdapter (var allStudents: MutableList<Student>) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val index: TextView
        val name: TextView
        val surname: TextView

        val editStudent: Button
        val deleteStudent: Button

        init {
            index = view.findViewById(R.id.tvIndexId)
            name = view.findViewById(R.id.tvNameId)
            surname = view.findViewById(R.id.tvSurnameId)
            editStudent = view.findViewById(R.id.btnEditStudentId)
            deleteStudent = view.findViewById(R.id.btnDeleteStudentId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentStudent = allStudents[position]

        holder.index.text = currentStudent.index.toString()
        holder.name.text = currentStudent.name.toString()
        holder.surname.text = currentStudent.surname.toString()

        holder.editStudent.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Edit " + currentStudent.index.toString(), Toast.LENGTH_SHORT).show()
        }

        holder.deleteStudent.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Delete " + currentStudent.index.toString(), Toast.LENGTH_SHORT).show()
        }


    }

    override fun getItemCount(): Int {
        return allStudents.size
    }

    fun updateData(student: Student)
    {
        this.allStudents.add(student)
    }
}