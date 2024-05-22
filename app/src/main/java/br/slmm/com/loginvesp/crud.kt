package br.slmm.com.loginvesp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import br.slmm.com.loginvesp.databinding.FragmentCrudBinding
import br.slmm.com.loginvesp.databinding.FragmentLoginBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

data class Course(val id: String? = null, val name: String? = null)

class crud: Fragment() {

    private var _binding: FragmentCrudBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference

    private lateinit var editTextNameCourse: EditText
    private lateinit var btnSubmitNameCourse: Button
    private lateinit var editTextDeletebyId: EditText
    private lateinit var btnSearchByID: Button
    private lateinit var btnBack: Button
    private lateinit var editTextSearchById: EditText
    private lateinit var btnDeleteCoursebyid: Button
    private lateinit var textInfoDatabase: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_crud, container, false)

        _binding = FragmentCrudBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance().getReference("courses")

        // Initialize UI components
        editTextNameCourse = view.findViewById(R.id.editTextNameCourse)
        btnSubmitNameCourse = view.findViewById(R.id.btnSubmitNameCourse)
        editTextDeletebyId = view.findViewById(R.id.editTextDeletebyId)
        btnSearchByID = view.findViewById(R.id.btnSearchByID)
        btnBack = view.findViewById(R.id.btnBack)
        editTextSearchById = view.findViewById(R.id.editTextSearchById)
        btnDeleteCoursebyid = view.findViewById(R.id.btnDeleteCoursebyid)
        textInfoDatabase = view.findViewById(R.id.textInfoDatabase)

        btnSubmitNameCourse.setOnClickListener { createCourse() }
        btnSearchByID = view.findViewById(R.id.btnSearchByID)
        btnSearchByID.setOnClickListener { readCourseByName() }
        btnDeleteCoursebyid.setOnClickListener { deleteCourse() }

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_crud2_to_mainFragment)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun createCourse() {
        val name = editTextNameCourse.text.toString()
        val courseId = database.push().key

        if (!name.isBlank() && courseId != null) {
            val course = Course(id = courseId, name = name)
            database.child(courseId).setValue(course).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    textInfoDatabase.text = "Curso salvo com sucesso: $name"
                    editTextNameCourse.text.clear()
                } else {
                    textInfoDatabase.text = "Erro ao salvar curso: ${task.exception?.message}"
                }
            }
        } else {
            textInfoDatabase.text = "Por favor, insira o nome do curso."
        }
    }

    @SuppressLint("SetTextI18n")
    private fun readCourseByName() {
        val courseName = editTextSearchById.text.toString()
        if (courseName.isNotBlank()) {
            val query: com.google.firebase.database.Query = database.orderByChild("name").equalTo(courseName)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (courseSnapshot in snapshot.children) {
                            val course = courseSnapshot.getValue(Course::class.java)
                            if (course != null) {
                                textInfoDatabase.text = "Curso encontrado: ${course.name} (ID: ${course.id})"
                                return
                            }
                        }
                    } else {
                        textInfoDatabase.text = "Curso não encontrado."
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    textInfoDatabase.text = "Erro ao consultar curso: ${error.message}"
                }
            })
        } else {
            textInfoDatabase.text = "Por favor, insira o nome do curso para consulta."
        }
    }

    @SuppressLint("SetTextI18n")
    private fun deleteCourse() {
        val courseId = editTextDeletebyId.text.toString()
        if (courseId.isNotBlank()) {
            database.child(courseId).removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    textInfoDatabase.text = "Curso excluído com sucesso: $courseId"
                    editTextDeletebyId.text.clear()
                } else {
                    textInfoDatabase.text = "Erro ao excluir curso: ${task.exception?.message}"
                }
            }
        } else {
            textInfoDatabase.text = "Por favor, insira o ID do curso para exclusão."
        }
    }
}
