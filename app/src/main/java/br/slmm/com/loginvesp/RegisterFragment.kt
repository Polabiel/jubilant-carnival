package br.slmm.com.loginvesp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import br.slmm.com.loginvesp.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return _binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmailRG.text.toString()
            val pwd   = binding.edtPwdRG.text.toString()

            auth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(activity, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }

        }

        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}