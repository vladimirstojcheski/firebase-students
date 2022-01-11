package com.example.firebasestudents.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasestudents.MainActivity
import com.example.firebasestudents.R
import com.example.firebasestudents.model.Student
import com.google.firebase.database.*

class StudentAdapter (var allStudents: MutableList<Student>) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val studentReference: DatabaseReference

        val index: TextView
        val name: TextView
        val surname: TextView

        val editStudent: Button
        val deleteStudent: Button
        val activity: MainActivity

        init {
            index = view.findViewById(R.id.tvIndexId)
            name = view.findViewById(R.id.tvNameId)
            surname = view.findViewById(R.id.tvSurnameId)
            editStudent = view.findViewById(R.id.btnEditStudentId)
            deleteStudent = view.findViewById(R.id.btnDeleteStudentId)
            studentReference = FirebaseDatabase.getInstance().getReference("students")
            activity = view.context as MainActivity

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
            holder.activity.setIndex(currentStudent.index!!)
            Navigation.findNavController(holder.itemView).navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        holder.deleteStudent.setOnClickListener {
            val studentQuery = holder.studentReference.orderByChild("index").equalTo(currentStudent.index.toString())

            studentQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (studentSnapshot in snapshot.children)
                    {
                        studentSnapshot.ref.removeValue()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

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