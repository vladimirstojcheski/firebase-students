package com.example.firebasestudents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasestudents.adapter.StudentAdapter
import com.example.firebasestudents.databinding.FragmentFirstBinding
import com.example.firebasestudents.model.Student
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    var database = FirebaseDatabase.getInstance()
    var studentsReference = database.getReference("students")


    private lateinit var etIndex: EditText
    private lateinit var etName: EditText
    private lateinit var etSurname: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etAddress: EditText

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as MainActivity

        etIndex = view.findViewById(R.id.etIndexId)
        etName = view.findViewById(R.id.etNameId)
        etSurname = view.findViewById(R.id.etSurnameId)
        etPhoneNumber = view.findViewById(R.id.etNumberId)
        etAddress = view.findViewById(R.id.etAddressId)
        etIndex.isEnabled = true

        if (!activity.getIndex().isNullOrEmpty()) {
            val studentListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for(data in dataSnapshot.children)
                    {
                        val student = data.getValue(Student::class.java)
                        if (student?.index == activity.getIndex()) {
                            etIndex.setText(student!!.index)
                            etIndex.isEnabled = false
                            etName.setText(student.name)
                            etSurname.setText(student.surname)
                            etPhoneNumber.setText(student.phoneNumber)
                            etAddress.setText(student.address)
                            break
                        }
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message

                }
            }

            studentsReference.addValueEventListener(studentListener)
        }


        val addStudentButton = view.findViewById<Button>(R.id.btnAddStudentId)

        val listStudentsButton = view.findViewById<Button>(R.id.btnListAllStudentsId)

        addStudentButton.setOnClickListener {
            val index = etIndex.text.toString()
            val name = etName.text.toString()
            val surname = etSurname.text.toString()
            val phoneNumber = etPhoneNumber.text.toString()
            val address = etAddress.text.toString()


            if(index.isEmpty() || name.isEmpty() || surname.isEmpty()
                || phoneNumber.isEmpty() || address.isEmpty())
            {
                return@setOnClickListener
            }

            addStudent(index, name, surname, phoneNumber, address)

        }

        listStudentsButton.setOnClickListener {
            activity.setIndex("")
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

    }

    private fun addStudent(index: String, name: String, surname: String, phoneNumber: String,
                           address: String) {

        val activity = activity as MainActivity

        val currentStudent = Student(index, name, surname, phoneNumber, address)

        if (!activity.getIndex().isNullOrEmpty()) {
            val studentListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (data in dataSnapshot.children) {
                        val student = data.getValue(Student::class.java)
                        if (student?.index == activity.getIndex()) {
                            val studentQuery = studentsReference.orderByChild("index").equalTo(student!!.index.toString())

                            studentQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (studentSnapshot in snapshot.children)
                                    {
                                        studentSnapshot.ref.setValue(currentStudent)
                                    }
                                    Toast.makeText(activity, "Edit is successful", Toast.LENGTH_SHORT).show()
                                    etIndex.setText("")
                                    etIndex.isEnabled = true
                                    etName.setText("")
                                    etSurname.setText("")
                                    etPhoneNumber.setText("")
                                    etAddress.setText("")
                                    activity.setIndex("")
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }

                            })

                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {


                }
            }
            studentsReference.addValueEventListener(studentListener)
        }

        else {

            studentsReference.push().setValue(currentStudent)
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(activity, "Add is successful", Toast.LENGTH_SHORT).show()
                        etIndex.setText("")
                        etName.setText("")
                        etSurname.setText("")
                        etPhoneNumber.setText("")
                        etAddress.setText("")
                        activity.setIndex("")
                    } else {
                        Toast.makeText(
                            activity, "Error: " + task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}