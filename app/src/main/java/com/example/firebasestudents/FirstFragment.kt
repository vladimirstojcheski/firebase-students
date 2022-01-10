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
import com.example.firebasestudents.databinding.FragmentFirstBinding
import com.example.firebasestudents.model.Student
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

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

        etIndex = view.findViewById(R.id.etIndexId)
        etName = view.findViewById(R.id.etNameId)
        etSurname = view.findViewById(R.id.etSurnameId)
        etPhoneNumber = view.findViewById(R.id.etNumberId)
        etAddress = view.findViewById(R.id.etAddressId)

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
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

    }

    private fun addStudent(index: String, name: String, surname: String, phoneNumber: String,
                           address: String) {

        val currentStudent = Student(index, name, surname, phoneNumber, address)

        studentsReference.push().setValue(currentStudent)
            .addOnCompleteListener(OnCompleteListener { task ->
                if(task.isSuccessful)
                {
                    Toast.makeText(activity, "Add is successful", Toast.LENGTH_SHORT).show()
                    etIndex.setText("")
                    etName.setText("")
                    etSurname.setText("")
                    etPhoneNumber.setText("")
                    etAddress.setText("")
                }
                else {
                    Toast.makeText(activity, "Error: " + task.exception!!.message,
                        Toast.LENGTH_SHORT).show()
                }
            })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}