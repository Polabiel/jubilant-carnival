package br.slmm.com.loginvesp

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import android.Manifest
import br.slmm.com.loginvesp.databinding.FragmentMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.android.gms.location.LocationServices

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return _binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }

        binding.btnGoCrud.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_crud2)
        }

    }
}