package br.slmm.com.loginvesp

import Disciplina
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*

class crud : Fragment() {

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crud, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance().getReference("disciplinas")

        // Load disciplines from the database
        loadDisciplinas()
    }

    private fun loadDisciplinas() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get the list of disciplines from the snapshot
                val disciplinas = dataSnapshot.children.mapNotNull { it.getValue(Disciplina::class.java) }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun editDisciplina(disciplina: Disciplina) {
        // Update the discipline in the database
        database.child(disciplina.id!!).setValue(disciplina)
    }

    private fun deleteDisciplina(disciplina: Disciplina) {
        // Remove the discipline from the database
        database.child(disciplina.id!!).removeValue()
    }
}