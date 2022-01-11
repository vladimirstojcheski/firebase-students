package com.example.firebasestudents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasestudents.adapter.StudentAdapter
import com.example.firebasestudents.databinding.FragmentSecondBinding
import com.example.firebasestudents.model.Student
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener




/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    private lateinit var studentRecyclerView: RecyclerView

    var database = FirebaseDatabase.getInstance()
    var studentsReference = database.getReference("students")

    var allStudents = mutableListOf(Student("34234","34234","34234","34234","34234"))

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val studentListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                val students = mutableListOf<Student>()

                for(data in dataSnapshot.children)
                {
                    val student = data.getValue(Student::class.java)
                    students.add(student!!)
                    //recyclerViewAdapter.updateData(data.getValue(Student::class.java)!!)
                }

                val adapter = StudentAdapter(students)
                studentRecyclerView = view.findViewById(R.id.allStudentsRecyclerViewId)
                studentRecyclerView.layoutManager = LinearLayoutManager(activity)
                studentRecyclerView.setHasFixedSize(true)
                studentRecyclerView.adapter = adapter

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }

        studentsReference.addValueEventListener(studentListener)



        binding.buttonSecond.setOnClickListener {
            val activty = activity as MainActivity
            activty.setIndex("")
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}